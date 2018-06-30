package com.assistx.hg.sp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATA_SPINED";
    private static final String TABLE_POSTURE = "TABLE_SPINED";
    private static final String TABLE_POSTURE_DETAILED = "TABLE_SPINED_DETAILED";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "Date";
    private static final String KEY_GOOD = "Good";
    private static final String KEY_BAD = "Bad";
    private static final String STATUS = "Status";
    private NumberFormat formatter = new DecimalFormat("00");

    private SimpleDateFormat actualDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dailyChartDateFormatter = new SimpleDateFormat("dd MMM");
    private SimpleDateFormat monthlyChartDateFormatter = new SimpleDateFormat("MM yyyy");
    private SimpleDateFormat monthlyChartDateFormatter2 = new SimpleDateFormat("MMM yyyy");


    //BAD IS 1; GOOD IS 0
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        System.out.println("CREATE NEW TABLE");

        String CREATE_POSTURE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTURE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT," + KEY_BAD + " TEXT," + KEY_GOOD + " TEXT" + ")";

        db.execSQL(CREATE_POSTURE_TABLE);

        String CREATE_POSTURE_TABLE_DETAILED = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTURE_DETAILED + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " datetime default current_timestamp," + STATUS + " INTEGER" + ")";


        db.execSQL(CREATE_POSTURE_TABLE_DETAILED);
    }


//    public List<Contact> getAllContacts() {
//        List<Contact> contactList = new ArrayList<>();
//        String selectQuery = "SELECT  * FROM " + TABLE_POSTURE;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Contact contact = new Contact();
//                contact.setId(Integer.parseInt(cursor.getString(0)));
//                contact.setDate(cursor.getString(1));
//                contact.setBad(cursor.getString(2));
//                contact.setGood(cursor.getString(3));
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return contactList;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void addContact(String pDate, String bad, String good) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, pDate);
        values.put(KEY_BAD, bad);
        values.put(KEY_GOOD, good);
        db.insert(TABLE_POSTURE, null, values);
        db.close();
    }

    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTURE, new String[]{KEY_ID, KEY_DATE, KEY_BAD, KEY_GOOD}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return contact;
        } else
            return null;
    }
/*
    public void deleteContact(String nameROW) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = {nameROW};
        int count = db.delete(TABLE_POSTURE, DatabaseHelper.KEY_NAME + "=?", whereArgs);
                new String[]{String.valueOf(contact.getId())});
        db.close();


    }*/

    public void updateBadPosture(int i, String newBadPos) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_BAD, newBadPos);
//        values.put(KEY_PHONE, contact.getPhone_number());
            String[] whereArgs = new String[]{String.valueOf(i)};
            db.update(TABLE_POSTURE, values, DatabaseHelper.KEY_ID + "=?", whereArgs);

            ContentValues detailedTableValues = new ContentValues();
            detailedTableValues.put(DatabaseHelper.STATUS, 1);
            detailedTableValues.put(DatabaseHelper.KEY_DATE, getDateTime());
            db.insertOrThrow(TABLE_POSTURE_DETAILED, null, detailedTableValues);
        } catch (SQLiteException sqe) {
            onCreate(db);
        }


    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void updateTotalPosture(int i, String newGoodPos) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_GOOD, newGoodPos);
//        values.put(KEY_PHONE, contact.getPhone_number());
            String[] whereArgs = new String[]{String.valueOf(i)};
            db.update(TABLE_POSTURE, values, DatabaseHelper.KEY_ID + "=?", whereArgs);

            ContentValues detailedTableValues = new ContentValues();
            detailedTableValues.put(DatabaseHelper.STATUS, 0);
            detailedTableValues.put(DatabaseHelper.KEY_DATE, getDateTime());

            db.insertOrThrow(TABLE_POSTURE_DETAILED, null, detailedTableValues);
        } catch (SQLiteException sqe) {
            onCreate(db);
        }

    }

    public int getContactCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_POSTURE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getTodaysBadPosture() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTURE, new String[]{KEY_ID, KEY_DATE, KEY_BAD, KEY_GOOD}, KEY_DATE + "=?",
                new String[]{todate()}, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return Integer.valueOf(contact.getBad());
        } else
            return 0;
    }

    public int getCurrentMonthBadPosture() {
        SQLiteDatabase db = this.getReadableDatabase();
        String startDate = getStartDateOfMonth().get(Calendar.YEAR) + "-" + formatter.format(getStartDateOfMonth().get(Calendar.MONTH) + 1) + "-" + formatter.format(getStartDateOfMonth().get(Calendar.DATE));
        String endDate = getEndDateOfMonth().get(Calendar.YEAR) + "-" + formatter.format(getEndDateOfMonth().get(Calendar.MONTH) + 1) + "-" + formatter.format(getEndDateOfMonth().get(Calendar.DATE));
        String query = "SELECT * from " + TABLE_POSTURE + " WHERE DATE(substr(" + KEY_DATE + ",7,4)||'-'||substr(" + KEY_DATE + ",4,2)||'-'||substr(" + KEY_DATE + ",1,2)) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "')";


        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            int count = 0;
            while (cursor.moveToNext()) {
                String badCount = cursor.getString(2);
                if (badCount != null && !badCount.isEmpty()) count += Integer.parseInt(badCount);
            }
            cursor.close();
            return count;
        } else
            return 0;
    }

    public int getCurrentYearBadPosture() {
        SQLiteDatabase db = this.getReadableDatabase();

        String startDate = getStartDateOfYear().get(Calendar.YEAR) + "-" + formatter.format(getStartDateOfYear().get(Calendar.MONTH) + 1) + "-" + formatter.format(getStartDateOfYear().get(Calendar.DATE));
        String endDate = getEndDateOfYear().get(Calendar.YEAR) + "-" + formatter.format(getEndDateOfYear().get(Calendar.MONTH) + 1) + "-" + formatter.format(getEndDateOfYear().get(Calendar.DATE));
        String query = "SELECT * from " + TABLE_POSTURE + " WHERE DATE(substr(" + KEY_DATE + ",7,4)||'-'||substr(" + KEY_DATE + ",4,2)||'-'||substr(" + KEY_DATE + ",1,2)) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            int count = 0;
            while (cursor.moveToNext()) {
                String badCount = cursor.getString(2);
                if (badCount != null && !badCount.isEmpty()) count += Integer.parseInt(badCount);
            }
            cursor.close();
            return count;
        } else
            return 0;
    }

    public int getAllTimeBadPosture() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTURE, new String[]{KEY_ID, KEY_DATE, KEY_BAD, KEY_GOOD}, null,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            int count = 0;
            while (cursor.moveToNext()) {
                String badCount = cursor.getString(2);
                if (badCount != null && !badCount.isEmpty()) count += Integer.parseInt(badCount);
            }
            cursor.close();
            return count;
        } else
            return 0;
    }

    public ChartModel getDailyChartData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = getCalendarForNow();
        calendar.add(Calendar.DATE, 1);
        String endDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        calendar.add(Calendar.DATE, -11);
        String startDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        String query = "SELECT * from " + TABLE_POSTURE + " WHERE DATE(substr(" + KEY_DATE + ",7,4)||'-'||substr(" + KEY_DATE + ",4,2)||'-'||substr(" + KEY_DATE + ",1,2)) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "')";

        Cursor cursor = db.rawQuery(query, null);

        ChartModel result = new ChartModel();
        //List<ChartDataModel> result = new ArrayList<>(0);


        if (cursor != null && cursor.getCount() > 0) {
            int count = 1;
            while (cursor.moveToNext()) {
                String date = cursor.getString(1);
                String badCount = cursor.getString(2);

                ChartDataModel chartModel = new ChartDataModel();
                chartModel.setType("Daily");
                chartModel.setX(count);
                chartModel.setY(badCount != null ? Integer.valueOf(badCount) : 0);
                chartModel.setxLabel(formatDailyDate(date));
                result.getChartData().add(chartModel);

                count = (count + 1);
            }
            cursor.close();
            return result;
        } else
            return result;
    }

    public ChartModel getYearlyChartData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = getCalendarForNow();
        calendar.add(Calendar.DATE, 1);
        String endDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        calendar.add(Calendar.YEAR, -5);
        calendar.set(Calendar.DATE, 1);
        String startDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        String query = "SELECT sum(Bad) AS Bad,substr(\"" + KEY_DATE + "\",7,4) as YEAR FROM " + TABLE_POSTURE + " WHERE DATE(substr(" + KEY_DATE + ",7,4)||'-'||substr(" + KEY_DATE + ",4,2)||'-'||substr(" + KEY_DATE + ",1,2)) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') GROUP BY YEAR";

        Cursor cursor = db.rawQuery(query, null);

        ChartModel result = new ChartModel();
        //List<ChartDataModel> result = new ArrayList<>(0);


        if (cursor != null && cursor.getCount() > 0) {
            int count = 1;
            while (cursor.moveToNext()) {
                String date = cursor.getString(1);
                String badCount = cursor.getString(0);

                ChartDataModel chartModel = new ChartDataModel();
                chartModel.setType("Daily");
                chartModel.setX(count);
                chartModel.setY(badCount != null ? Integer.valueOf(badCount) : 0);
                chartModel.setxLabel(date);
                result.getChartData().add(chartModel);

                count = (count + 1);
            }
            cursor.close();
            return result;
        } else
            return result;
    }

    public ChartModel getMonthlyChartData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = getCalendarForNow();
        calendar.add(Calendar.DATE, 1);
        String endDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        calendar.add(Calendar.YEAR, -5);
        calendar.set(Calendar.DATE, 1);
        String startDate = calendar.get(Calendar.YEAR) + "-" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "-" + formatter.format(calendar.get(Calendar.DATE));

        String query = "SELECT sum(Bad) AS Bad,(substr(\"" + KEY_DATE + "\",4,2) || ' ' ||  substr(\"" + KEY_DATE + "\",7,4) ) as MONTH_YEAR FROM " + TABLE_POSTURE + " WHERE DATE(substr(" + KEY_DATE + ",7,4)||'-'||substr(" + KEY_DATE + ",4,2)||'-'||substr(" + KEY_DATE + ",1,2)) BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') GROUP BY MONTH_YEAR";

        Cursor cursor = db.rawQuery(query, null);

        ChartModel result = new ChartModel();

        if (cursor != null && cursor.getCount() > 0) {
            int count = 1;
            while (cursor.moveToNext()) {
                String date = cursor.getString(1);
                String badCount = cursor.getString(0);

                ChartDataModel chartModel = new ChartDataModel();
                chartModel.setType("Daily");
                chartModel.setX(count);
                chartModel.setY(badCount != null ? Integer.valueOf(badCount) : 0);
                chartModel.setxLabel(formatMonthlyDate(date));
                result.getChartData().add(chartModel);

                count = (count + 1);
            }
            cursor.close();
            return result;
        } else
            return result;
    }

    public boolean shouldSendNotification() {
        Log.i("CHECKING>>>>>>>>>>>>", "NOW");
        SQLiteDatabase db = this.getReadableDatabase();
        String goodPosturesSinceLast1HrQuery = "SELECT count(" + STATUS + ")   FROM " + TABLE_POSTURE_DETAILED + " where  " + KEY_DATE + " >=datetime('now', '-0.5 Hour') AND Status = 0";
        String badPosturesSinceLast1HrQuery = "SELECT count(" + STATUS + ")   FROM " + TABLE_POSTURE_DETAILED + " where  " + KEY_DATE + " >=datetime('now', '-0.5 Hour') AND Status = 1";

        int numberOfGoodPostures = -1, numberOfBadPostures = -1;

        Cursor cursor = db.rawQuery(goodPosturesSinceLast1HrQuery, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            numberOfGoodPostures = cursor.getInt(0);
        }

        cursor = db.rawQuery(badPosturesSinceLast1HrQuery, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            numberOfBadPostures = cursor.getInt(0);
        }

        int totalRecordsSinceLastHalfHr = numberOfGoodPostures + numberOfBadPostures;

        Log.i("GOOD POSTURE COUNTS :", numberOfGoodPostures + "");
        Log.i("BAD POSTURE COUNTS :", numberOfBadPostures + "");

        if (totalRecordsSinceLastHalfHr > 0) {
            float percentageOfBadPosture = (numberOfBadPostures * 100) / totalRecordsSinceLastHalfHr;
            Log.i("BAD POSTURE PER :", percentageOfBadPosture + "");
            if (percentageOfBadPosture >= 30) {
                return true;
            }
        }
        return false;


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

    private Calendar getStartDateOfYear() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.MONTH, 1);
        //setTimeToBeginningOfDay(calendar);
        return calendar;
    }

    private Calendar getEndDateOfYear() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.DATE, 31);
        //setTimeToEndofDay(calendar);
        return calendar;
    }

    private Calendar getStartDateOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar;
    }

    private Calendar getEndDateOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        return calendar;
    }

    private String formatDailyDate(String date) {
        try {
            Date actualDate = actualDateFormatter.parse(date);
            return dailyChartDateFormatter.format(actualDate);
        } catch (Exception e) {
            return "-";
        }

    }

    private String formatMonthlyDate(String date) {
        try {
            Date actualDate = monthlyChartDateFormatter.parse(date);
            return monthlyChartDateFormatter2.format(actualDate);
        } catch (Exception e) {
            return "-";
        }

    }
}
