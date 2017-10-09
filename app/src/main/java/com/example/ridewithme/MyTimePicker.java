package com.example.ridewithme;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;


/**
 * Created by Naor on 25/03/2017.
 */

public class MyTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText editText;
    private Calendar myCalendar;
    private Context _context;

    public MyTimePicker(Context context, EditText editText ){

        Activity activity = (Activity)context;
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.myCalendar = Calendar.getInstance();
        this._context = context;

    }


    @Override
    public void onClick(View v) {
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        new TimePickerDialog(_context,this, hour, minute, true).show();
    }
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub

        if (hourOfDay < 10 && minute < 10){
            this.editText.setText("0" + hourOfDay + ":" + "0" + minute);
        }

        else if(hourOfDay < 10 && minute > 9){
            this.editText.setText("0" + hourOfDay + ":" + minute);
        }
        else if (hourOfDay > 9 && minute < 10){
            this.editText.setText(hourOfDay + ":" + "0" + minute);
        }
        else if (hourOfDay > 9 && minute > 9){
            this.editText.setText(hourOfDay + ":" + minute);
        }




    }

}

