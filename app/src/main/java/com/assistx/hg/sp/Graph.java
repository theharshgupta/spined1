package com.assistx.hg.sp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class Graph extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        BarChart barchart;
        DatabaseHelper db = new DatabaseHelper(this);
        TextView mTextView;
        mTextView = (TextView) findViewById(R.id.reportSet);
        barchart = (BarChart) findViewById(R.id.bargraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        ArrayList<String> theDates = new ArrayList<>();

        int totalCount = 0, value = 0, compareValue = 0;
        try {
            totalCount = db.getContactCount();
            value = Integer.valueOf(db.getContact(totalCount).getBad());
            compareValue = Integer.valueOf(db.getContact(1).getBad());


            if (value == compareValue) {
                mTextView.setText("It's Day 1");
                barDataSet.setColor(R.color.graphColor);
                mTextView.setTextColor(Color.DKGRAY);
            } else if (value < compareValue) {
                mTextView.setText("Succeeding");
                mTextView.setTextColor(Color.GREEN);
                barDataSet.setColor(R.color.graphColorGreen);
            } else {
                mTextView.setText("Need Improvement");
                mTextView.setTextColor(Color.RED);
                mTextView.setTextSize(15);
                barDataSet.setColor(R.color.graphColor);
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }


        barEntries.add(new BarEntry(100, 0));
        theDates.add(0, "Threshhold");

        int sqlCount = db.getContactCount();
        if (sqlCount > 0) {
            for (int i = 1; i <= sqlCount; i++) {

                float enter = ((Float.valueOf(db.getContact(i).getBad()) / Float.valueOf(db.getContact(i).getGood())) * 100);
                barEntries.add(new BarEntry(enter, i));
                theDates.add(i, db.getContact(i).getDate());
            }
        }

        barDataSet.setColor(R.color.graphColor);


        BarData theData = new BarData(theDates, barDataSet);
        barchart.setData(theData);
        barchart.setDescription(null);
        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);

    }
}