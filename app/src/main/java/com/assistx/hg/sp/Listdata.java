package com.assistx.hg.sp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Listdata extends BaseActivity {

    DatabaseHelper db = new DatabaseHelper(this);

    String text = "";
    TextView mTextView, goodtext, dateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        mTextView = (TextView) findViewById(R.id.badList);
//        goodtext = (TextView) findViewById(R.id.goodList);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "AvantGardeMdITCTT.ttf");
        mTextView.setTypeface(typeface);

        int count = db.getContactCount();
        Contact displayContact = db.getContact(count);
//        long display = Long.parseLong(displayContact.getBad());
//        long displayGood = Long.parseLong(displayContact.getGood());

        ArrayList<String> dates = new ArrayList<String>(count);
        ArrayList<String> bad = new ArrayList<String>(count);
        ArrayList<String> good = new ArrayList<String>(count);

        for (int add = 0; add < count; add++) {
            dates.add(add, db.getContact(add + 1).getDate());
            bad.add(add, db.getContact(add + 1).getBad());
            good.add(add, db.getContact(add + 1).getGood());
        }
        ListView buckysListView = (ListView) findViewById(R.id.listView);
        ListAdapter buckysAdapter = new CustomAdapter(this, bad, good, dates);
        buckysListView.setAdapter(buckysAdapter);
        float percent = ((Float.valueOf(displayContact.getBad()) / Float.valueOf(displayContact.getGood())) * 100);
        DecimalFormat f = new DecimalFormat("##.0");
        //display only the percent of bad posture not the number of postures
        String percentString = String.valueOf(f.format(percent)) + "%";
        mTextView.setText(percentString);


    }
}
