package com.example.ridewithme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class CustomAdapter extends ArrayAdapter<_7Data>  {
    public TextView name,phone,date_time,msg,extra;
    private int layoutResource;


    public CustomAdapter(Context context, int layoutResource, ArrayList<_7Data> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);
        }

        /*SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time = dateFormat.format(new Date());*/

        _7Data data = getItem(position);
        if (data != null) {
            name = (TextView) view.findViewById(R.id.user_name);
            phone = (TextView) view.findViewById(R.id.user_phone);
            date_time = (TextView) view.findViewById(R.id.user_date_time);
            msg = (TextView) view.findViewById(R.id.user_msg);
            extra = (TextView) view.findViewById(R.id.user_extra);

            if (name != null & phone != null & msg != null & date_time != null) {
                name.setText(data.get_name());
                phone.setText(data.get_phone());
                date_time.setText(data.get_timestamp());
                msg.setText(data.get_from() + "--> " + data.get_to() + ", " + data.get_date() + ", " + data.get_time());
                extra.setText("הערות:" + " " + data.get_extras());

            }
        }
        return view;
    }
}
