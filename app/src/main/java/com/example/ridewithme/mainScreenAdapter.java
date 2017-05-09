package com.example.ridewithme;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;


/**
 * Created by Mesfin & Naor on 04/12/2016.
 */

public class mainScreenAdapter extends ArrayAdapter<TrempData>  {

    private TextView  name ,timestamp ,uid, from,to,date,time,myextra ;
    private ImageButton phoneBtn;
    private ImageView timeline,car;
    private int layoutResource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    public mainScreenAdapter(Context context, int layoutResource, ArrayList<TrempData> list) {
        super(context, layoutResource, list);
        this.layoutResource = layoutResource;
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            final LayoutInflater Inflater = LayoutInflater.from(getContext());
            view = Inflater.inflate(layoutResource, null);
        }

        final TrempData data = getItem(position);
        //data.setPos(position);
        //int[] androidColors = getContext().getResources().getIntArray(R.array.androidcolors);
        //int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];


        if (data != null) {
            name = (TextView) view.findViewById(R.id.myname);
            phoneBtn = (ImageButton) view.findViewById(R.id.myphone);
            //myphonestr = (TextView)view.findViewById(R.id.myphonestr);
            timestamp = (TextView) view.findViewById(R.id.mytimestamp);
            uid = (TextView) view.findViewById(R.id.myuid);
            timeline = (ImageView)view.findViewById(R.id.timeline);
            car = (ImageView)view.findViewById(R.id.mycar);
            date = (TextView) view.findViewById(R.id.mydate);
            time = (TextView) view.findViewById(R.id.mytime);
            from = (TextView) view.findViewById(R.id.myfrom);
            to = (TextView) view.findViewById(R.id.myto);
            myextra = (TextView) view.findViewById(R.id.myextra);

            name.setText(data.get_name());
            //name.setTextColor(randomAndroidColor);
            //myphonestr.setText(data.get_phone());
            timestamp.setText(data.get_timestamp());
            uid.setText(data.get_uid());
            date.setText(data.get_date());
            time.setText(data.get_time());
            to.setText(data.get_to());
            from.setText(data.get_from());
            uid.setText(data.get_uid());
            myextra.setText(data.get_extras());



            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get_phone()));
                    getContext().startActivity(i);
                }
            });



            if(firebaseAuth.getCurrentUser().getUid().equals(uid.getText())){

                phoneBtn.setVisibility(View.GONE);
                car.setColorFilter(Color.rgb(255,164,30));

            }
            else{

                phoneBtn.setVisibility(View.VISIBLE);
                car.setColorFilter(Color.rgb(176,176,176));

            }

        }


        return view;
    }


}





