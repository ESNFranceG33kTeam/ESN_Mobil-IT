package org.esn.mobilit.utils.firstlaunch;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Spider on 15/12/14.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, ArrayList<String> datas) {
        super(context, android.R.layout.simple_list_item_1, datas);
    }

    public View getView(int position, View convertView,ViewGroup parent) {

        View v = super.getView(position, convertView, parent);

        ((TextView) v).setTextSize(16);

        return v;

    }

    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        View v = super.getDropDownView(position, convertView,parent);

        ((TextView) v).setGravity(Gravity.CENTER);

        return v;

    }
}