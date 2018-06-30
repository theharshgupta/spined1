package com.assistx.hg.sp;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements SensorEventListener {
    public static final int NOTIFICATION_ID = 1;
    private static final String BAD_POSTURE_TEXT = "BAD POSTURE";
    private static final String GOOD_POSTURE_TEXT = "GOOD POSTURE";
    private static final String REALTIME_TEXT = "LONG PRESS TO SEE LIVE POSTURE";
    private static final String CHART_DAILY = "Daily";
    private static final String CHART_MONTHLY = "Monthly";
    private static final String CHART_YEARLY = "Yearly";
    private static int sessionDepth = 0;
    TextView random;
    TabLayout tabLayout;
    TabLayout.Tab badPosturesToday;
    TabLayout.Tab badPosturesMonthly;
    TabLayout.Tab badPosturesYearly;
    TabLayout.Tab badPosturesTotal;
    CardView realtimeCard;
    TextView realtimeText;
    MaterialBetterSpinner chartTypeDropdown;
    String[] ChartTypes = {CHART_DAILY, CHART_MONTHLY, CHART_YEARLY};
    DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
    float yValue;
    float xValue;
    float zValue;
    SensorManager sm;
    Sensor accelSensor;
    boolean isRealtimeButtonLongPressed = false;
    int goodPostureBackground, badPostureBackground, textColor, textColorDef, colorPrimaryDark;
    long lastSensorReading = 0;
    LineChart chart;
    private NotificationManager notificationManager;
    private long delayWhileAppInBackground = 3000,
            delayWhileAppInForeground = 1000,
            delayWhileLowBatterry = 5000,
            delayAfterNotificationAlertSent = 1800000,
            delayForNotificationChecking = 300000;
    private boolean isAppInBackground = false;

    @Override
    protected void onStart() {
        super.onStart();
        sessionDepth++;
        if (sessionDepth == 1) {
            //app came to foreground;
            isAppInBackground = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sessionDepth > 0)
            sessionDepth--;
        if (sessionDepth == 0) {
            // app went to background
            isAppInBackground = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Boolean isFirstTime;
        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);

        SharedPreferences.Editor editor = app_preferences.edit();

        isFirstTime = app_preferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            Intent onBoard = new Intent(this, firstTime.class);
            startActivity(onBoard);
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main);
        }


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        badPosturesToday = tabLayout.getTabAt(0);
        badPosturesMonthly = tabLayout.getTabAt(1);
        badPosturesYearly = tabLayout.getTabAt(2);
        badPosturesTotal = tabLayout.getTabAt(3);
        realtimeCard = (CardView) findViewById(R.id.realtimeCard);
        realtimeText = (TextView) findViewById(R.id.realtimeText);
        goodPostureBackground = ContextCompat.getColor(getBaseContext(), R.color.green);
        badPostureBackground = ContextCompat.getColor(getBaseContext(), R.color.colorAccent);
        textColor = ContextCompat.getColor(getBaseContext(), R.color.colorPrimary);
        textColorDef = ContextCompat.getColor(getBaseContext(), R.color.black);
        colorPrimaryDark = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);

        chartTypeDropdown = (MaterialBetterSpinner) findViewById(R.id.android_material_design_spinner);
        chartTypeDropdown.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ChartTypes));
        chartTypeDropdown.setSelection(0);

        chartTypeDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentChartType = chartTypeDropdown.getText().toString();
                if (currentChartType.equals(CHART_DAILY)) {
                    initChart(mDatabaseHelper.getDailyChartData());
                } else if (currentChartType.equals(CHART_MONTHLY)) {
                    initChart(mDatabaseHelper.getMonthlyChartData());
                } else if (currentChartType.equals(CHART_YEARLY)) {
                    initChart(mDatabaseHelper.getYearlyChartData());
                }
            }
        });

        chart = (LineChart) findViewById(R.id.chart);
        chart.setViewPortOffsets(100, 10, 100, 50);
        chart.setMinimumHeight(350);
        chart.setExtraBottomOffset(20);

        initChart(mDatabaseHelper.getDailyChartData());

        realtimepostureDetection();

        scheduleNotificationHandler();
    }

    private void initChart(final ChartModel model) {
        List<Entry> entries = new ArrayList<Entry>();
        List<String> labels = new ArrayList<>(model.getChartData().size());
        labels.add("");

        for (ChartDataModel data : model.getChartData()) {
            entries.add(new Entry(data.getY(), data.getX()));
            labels.add(data.getxLabel());
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(colorPrimaryDark);
        dataSet.setCircleColor(colorPrimaryDark);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColorHole(colorPrimaryDark);
        LineData data = new LineData(labels, dataSet);
        data.addDataSet(dataSet);


        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.setData(data);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setLabelsToSkip(0);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.getLegend().setEnabled(false);

        chart.setDescription("");
        //chart.getXAxis().setDrawLabels(false);
        chart.invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void realtimepostureDetection() {
        realtimeCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //implement onClick
                isRealtimeButtonLongPressed = true;
                if (yValue < 6) {

                    realtimeCard.setBackgroundColor(badPostureBackground);
                    realtimeText.setTextColor(textColor);
                    realtimeText.setText(BAD_POSTURE_TEXT);
                } else {
                    realtimeCard.setBackgroundColor(goodPostureBackground);
                    realtimeText.setTextColor(textColor);
                    realtimeText.setText(GOOD_POSTURE_TEXT);
                }

                return true;
            }
        });

        realtimeCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View pView, MotionEvent pEvent) {
                pView.onTouchEvent(pEvent);
                // We're only interested in when the button is released.
                if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                    // We're only interested in anything if our speak button is currently pressed.
                    if (isRealtimeButtonLongPressed) {
                        // Do something when the button is released.
                        isRealtimeButtonLongPressed = false;

                        realtimeCard.setBackgroundColor(textColor);
                        realtimeText.setTextColor(textColorDef);
                        realtimeText.setText(REALTIME_TEXT);
                    }
                }
                return false;
            }
        });
    }

    private void updateTrend() {
        if (badPosturesToday != null)
            badPosturesToday.setText(Html.fromHtml("<b><big>" + mDatabaseHelper.getTodaysBadPosture() + "</big></b><br><small>Today</small>"));
        if (badPosturesMonthly != null)
            badPosturesMonthly.setText(Html.fromHtml("<b><big>" + mDatabaseHelper.getCurrentMonthBadPosture() + "</big></b><br><small>Month</small>"));
        if (badPosturesYearly != null)
            badPosturesYearly.setText(Html.fromHtml("<b><big>" + mDatabaseHelper.getCurrentYearBadPosture() + "</big></b><br><small>Year</small>"));
        if (badPosturesTotal != null)
            badPosturesTotal.setText(Html.fromHtml("<b><big>" + mDatabaseHelper.getAllTimeBadPosture() + "</big></b><br><small>Total</small>"));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    public void onSensorChanged(final SensorEvent sensorEvent) {

        long curTime = System.currentTimeMillis();

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValue = sensorEvent.values[0];
            yValue = sensorEvent.values[1];
            zValue = sensorEvent.values[2];
        }
        if ((curTime - lastSensorReading) > delayToUse() && sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            lastSensorReading = curTime;

            KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            boolean isPhoneLocked = myKM.inKeyguardRestrictedInputMode();

            int checkCount = mDatabaseHelper.getContactCount();
            if (checkCount == 0) {
                mDatabaseHelper.addContact(todate(), "0", "1");
            }

            //Entering the total Number of Postures that have been Registered
            int newCount = mDatabaseHelper.getContactCount();
            Contact contact2 = mDatabaseHelper.getContact(newCount);
            String date2 = contact2.getDate();

            if (checkDate(date2)) {
                int addition = Integer.valueOf(contact2.getGood()) + 1;
                if (delayToUse() == delayWhileLowBatterry) {
                    addition = Integer.valueOf(contact2.getGood()) + 5;
                }
                if (delayToUse() == delayWhileAppInBackground) {
                    addition = Integer.valueOf(contact2.getGood()) + 3;
                }

                mDatabaseHelper.updateTotalPosture(contact2.getId(), String.valueOf(addition));
            } else {
                mDatabaseHelper.addContact(todate(), "1", "1");
            }

            //HARMFUL
            if (!isPhoneLocked) {
                if ((yValue < 6.5 && yValue > 0) || zValue > 6) {
                    //makeNotificationMild();

                    int entryCount = mDatabaseHelper.getContactCount();
                    Contact last = mDatabaseHelper.getContact(entryCount);
                    mDatabaseHelper.updateBadPosture(last.getId(), String.valueOf(Integer.valueOf(last.getBad()) + 1));
                } else {
                    NotificationManager trial = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (trial != null) trial.cancel(0);
                }
            } else {

                NotificationManager trial = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (trial != null) trial.cancel(0);
            }

            updateTrend();
        }
    }

    private long delayToUse() {
        int batteryLevel = getBatteryPercentage(MainActivity.this);
        if (batteryLevel > 50) {
            if (isAppInBackground) {
                return delayWhileAppInBackground;
            } else {
                return delayWhileAppInForeground;
            }
        } else {
            return delayWhileLowBatterry;
        }

    }

    public int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    private void scheduleNotificationHandler() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mDatabaseHelper.shouldSendNotification()) {
                    makeNotificationMild();
                    try {
                        Thread.currentThread().sleep(delayAfterNotificationAlertSent);
                    } catch (Exception e) {
                    }
                }
            }
        }, 0, delayForNotificationChecking);
    }

    public void makeNotificationMild() {

        try {
            Intent tatti = new Intent(MainActivity.this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, tatti, 0);
            Notification.Builder builder = new Notification.Builder(MainActivity.this);

            //Building Up the notification
            builder.setContentTitle("Posture Warning")
                    .setContentText("Your posture was not great in the last hour")
                    //.setContentText("Level: Needs Improvement")
                    .setSmallIcon(R.mipmap.notigreen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notigreen))
                    .setContentIntent(pi)
                    .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                    .setPriority(Notification.PRIORITY_MAX);

            if (notificationManager == null) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }

            notificationManager.notify(0, builder.build());
            builder.setAutoCancel(true);
        } catch (Exception e) {
            Log.e("Error WHILE SENDING", e.getMessage());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //PERFORM NOTHING
    }

    public boolean checkDate(String sDate1) {

        Date compareDate = null;
        try {
            compareDate = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date1 = new Date();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(compareDate);
        cal2.setTime(date1);


        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public String todate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        return dateFormat.format(date);
    }

    //These are all click Intents
    public void Exit(View view) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                NotificationManager trial = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                trial.cancel(0);

            }
        };
        Thread thread = new Thread(r);
        thread.start();

    }

    public void about(View view) {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    public void graph(View view) {
        Intent i = new Intent(this, Graph.class);
        startActivity(i);
    }

    public void listdata(View view) {
        Intent i = new Intent(this, Listdata.class);
        startActivity(i);
    }

}