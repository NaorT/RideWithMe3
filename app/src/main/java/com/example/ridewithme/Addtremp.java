package com.example.ridewithme;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

public class Addtremp extends DialogFragment {
    public EditText name, phone,date, time, extra;
    public Button send_btn,update;
    private DatabaseReference mDatabase, mDatabase2;
    public String str_image, str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp, send_time2;
    public TrempData trempData;
    private FirebaseAuth mAuth;
    private MyDatePicker myDatePicker;
    private MyTimePicker myTimePicker;
    String userUid;
    FirebaseMessaging fm;
    AlertDialog alertDialog;
    AutoCompleteTextView from,to;
    public String[] cities ={"אבן יהודה","אופקים","אור הנר","אור יהודה","אזור","אילת","אמירים","אפרת","אריאל","אשדוד","אשקלון","באר","יעקב","באר שבע",
            "בית דגן","בית שאן","בית שמש","בית שערים","בני ברק","בנימינה","בת ים","גבעת סביון","גבעת שמואל","גבעתיים","גדרה",
            "גלעד","גן יבנה","גני תקוה","הוד השרון","הרצליה","זכרון יעקב","חדרה","חולון","חיפה","חצור הגלילית","טבעון","טבריה","טירת הכרמל",
            "יבנה","יהוד","ים המלח","יפו","יקנעם עילית","ירוחם","ירושלים","כוכב יאיר","כפר אזר","כפר ורדים","כפר מלל","כפר סבא","כפר שמריהו","כפר תבור"
            ,"כרכור","כרמיאל","לוד","לפיד","מגדל","מודיעין","מטולה","מכבים","מכמורת","מפלסים","נהריה","נען","נצרת","נתניה","סביון","עכו",
            "מבשרת ציון","מגדל העמק","מצפה רמון","מקווה ישראל","נווה אור","נווה איתן","ניר ח''ן","נס ציונה","נצרת עילית","עפולה","ערד","עתלית",
            "פרדס חנה","פתח-תקוה","צור הדסה","צורעה","צפת","קדימה","קיבוץ אורטל","נחל עוז","עלומים","קיסריה","קצרין","קרית אונו","קרית אתא","קרית ביאליק",
            "קרית גת","קרית מוצקין","קרית שמונה","ראש העין","ראש פינה","ראשון לציון","רחובות","רמלה","רעננה","שדרות","שוהם","שילה","רמת-גן","רמת רזיאל","רמת ישי",
            "רמת אפעל","רמת השרון","תל-אביב"};


    public Addtremp() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_tremp_dialog, null);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_drawable);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("לידיעתך,");
        alertDialog.setMessage("במידה וימצא מישהו העונה להגדרות הטרמפ שהצעת, תישלח אליך הודעת SMS");
        alertDialog.setCanceledOnTouchOutside(false);

        //Creating the instance of ArrayAdapter containing list of cities
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getActivity(),R.layout.auto_complete_drop_down_list,cities);

        Bundle mArgs = getArguments();

        //INITIALIZE DATA_BASE AND VIEWS
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        fm = FirebaseMessaging.getInstance();


        //timeStamp
        final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        send_time2 = dateFormat1.format(new Date());
        //Initialize views
        name = (EditText) v.findViewById(R.id.name);
        phone = (EditText) v.findViewById(R.id.phone);
        from = (AutoCompleteTextView)v.findViewById(R.id.from);

        to = (AutoCompleteTextView)v.findViewById(R.id.to);
        from.setThreshold(1);//will start working from first character
        to.setThreshold(1);
        from.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        to.setAdapter(adapter);
        date = (EditText) v.findViewById(R.id.date);
        myDatePicker = new MyDatePicker(getActivity(),date);
        time = (EditText) v.findViewById(R.id.time);
        myTimePicker = new MyTimePicker(getActivity(),time);
        extra = (EditText) v.findViewById(R.id.extra);
        send_btn = (Button) v.findViewById(R.id.add3) ;

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(mAuth.getCurrentUser().getUid().toString().equals(user.getId().toString())){
                        name.setText(user.getName().toString());
                        phone.setText(user.getPhone().toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                Date myDate = new Date();
                SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
                String mdy = mdyFormat.format(myDate);
                str_name = name.getText().toString();
                str_phone = phone.getText().toString();
                str_from = from.getText().toString();
                str_to = to.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();
                str_extra = extra.getText().toString();
                str_timestamp = send_time2 + ", " + mdy;
                str_uid = mAuth.getCurrentUser().getUid();
                str_image = "default";


                trempData = new TrempData(str_image, str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                if (name.length() == 0) {
                    name.requestFocus();
                    name.setHintTextColor(Color.RED);
                    name.setHint("מלא את שמך");
                    return;
                } else if (phone.length() != 10) {
                    phone.requestFocus();
                    phone.setText("");
                    phone.setHintTextColor(Color.RED);
                    phone.setTextSize(15);
                    phone.setHint("מספר פלאפון לא תקין. נסה שוב");
                    return;
                } else if (from.length() == 0) {
                    from.requestFocus();
                    from.setHintTextColor(Color.RED);
                    from.setHint("מלא מוצא");
                    return;
                } else if (to.length() == 0) {
                    to.requestFocus();
                    to.setHintTextColor(Color.RED);
                    to.setHint("מלא יעד");
                    return;
                } else if (date.length() == 0) {
                    date.requestFocus();
                    date.setHintTextColor(Color.RED);
                    date.setHint("מלא תאריך");
                    return;
                } else if (time.length() == 0) {
                    time.requestFocus();
                    time.setHintTextColor(Color.RED);
                    time.setHint("מלא שעת יציאה");
                    return;
                }

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "המשך", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference newPost = mDatabase.push();
                        str_key = newPost.getKey();
                        trempData = new TrempData(str_image,str_key, str_uid, str_name, str_phone, str_from, str_to, str_date, str_time, str_extra, str_timestamp);
                        newPost.setValue(trempData);
                        //showToast();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                //Toast.makeText(getActivity(), "הטרמפ נוסף בהצלחה", Toast.LENGTH_SHORT).show();
                dismiss();
            }

        });
        return v;
    }
   /* //this method set the dialog fragment to full screen
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.setLayout(attributes.MATCH_PARENT, attributes.MATCH_PARENT);

    }*/

    public void showToast(){
        Toast.makeText(getActivity(), "הטרמפ נוסף בהצלחה", Toast.LENGTH_SHORT).show();
    }

    public void changeOption(AutoCompleteTextView text) {
        if (text.getHeight() == 100) {
            text.setHeight(30);
        } else {
            text.setHeight(100);

        }
    }



}






