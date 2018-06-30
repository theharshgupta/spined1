package com.assistx.hg.sp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> bad;
    private ArrayList<String> good;
    private ArrayList<String> dates;
    private Activity context;


    public CustomAdapter(Listdata context, ArrayList<String> bad, ArrayList<String> good, ArrayList<String> dates) {
        super((Context) context, R.layout.customlayout, bad);
        this.context = (Activity) context;
        this.dates = dates;
        this.bad = bad;
        this.good = good;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.customlayout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();

        }

        viewHolder.goodt.setText(good.get(position));
        viewHolder.badt.setText(bad.get(position));
        viewHolder.datest.setText(dates.get(position));

//        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
//        View customView = buckysInflater.inflate(R.layout.customlayout, parent, false);
//
//        String single = getItem(position);
//        TextView date = (TextView) customView.findViewById(R.id.dateTv);
//        date.setText(single);

        return r;
    }

    class ViewHolder {

        TextView badt;
        TextView goodt;
        TextView datest;

        ViewHolder(View v) {
            badt = (TextView) v.findViewById(R.id.badTv);
            goodt = (TextView) v.findViewById(R.id.goodTv);
            datest = (TextView) v.findViewById(R.id.dateTv);

        }


    }

}
