package com.example.ridewithme;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mesfin & Naor on 14/12/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class SearchTremp extends DialogFragment {
    public EditText date, time;
    public Button search_send_btn;
    public String str_from, str_to, str_date, str_time;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private MyDatePicker myDatePicker;
    private MyTimePicker myTimePicker;
    private ProgressDialog progressDialog;
    private String[] userName = new String[1];

    final int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    AlertDialog alertDialog;

    AutoCompleteTextView from,to;
    public String[] cities ={"אבן יהודה","אופקים","אור הנר","אור יהודה","אזור","אילת","אמירים","אפרת","אריאל","אשדוד","אשקלון","יעקב","באר שבע",
            "בית דגן","בית שאן","בית שמש","בית שערים","בני ברק","בנימינה","בת ים","גבעת סביון","גבעת שמואל","גבעתיים","גדרה",
            "גלעד","גן יבנה","גני תקוה","הוד השרון","הרצליה","זכרון יעקב","חדרה","חולון","חיפה","חצור הגלילית","טבעון","טבריה","טירת הכרמל",
            "יבנה","יהוד","ים המלח","יפו","יקנעם עילית","ירוחם","ירושלים","כוכב יאיר","כפר אזר","כפר ורדים","כפר מלל","כפר סבא","כפר שמריהו","כפר תבור"
            ,"כרכור","כרמיאל","לוד","לפיד","מגדל","מודיעין","מטולה","מכבים","מכמורת","מפלסים","נהריה","נען","נצרת","נתניה","סביון","עכו",
            "מבשרת ציון","מגדל העמק","מצפה רמון","מקווה ישראל","נווה אור","נווה איתן","ניר ח''ן","נס ציונה","נצרת עילית","עפולה","ערד","עתלית",
            "פרדס חנה","פתח-תקוה","צור הדסה","צורעה","צפת","קדימה","קיבוץ אורטל","נחל עוז","עלומים","קיסריה","קצרין","קרית אונו","קרית אתא","קרית ביאליק",
            "קרית גת","קרית מוצקין","קרית שמונה","ראש העין","ראש פינה","ראשון לציון","רחובות","רמלה","רעננה","שדרות","שוהם","שילה","רמת-גן","רמת רזיאל","רמת ישי",
            "רמת אפעל","רמת השרון","תל-אביב"};



    public SearchTremp() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_tremp_dialog, null);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_drawable);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("לידיעתך,");
        alertDialog.setMessage("במידה ונמצא טרמפ מתאים עבורך, הודעה תישלח למציע הטרמפ.");
        alertDialog.setCanceledOnTouchOutside(false);

        //Creating the instance of ArrayAdapter containing list of cities
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),R.layout.auto_complete_drop_down_list,cities);

        //INITIALIZE DATA_BASE
        progressDialog = new ProgressDialog(getActivity());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getUserName();
        //INITIALIZE VIEWS
        from = (AutoCompleteTextView)v.findViewById(R.id.from);

        to = (AutoCompleteTextView)v.findViewById(R.id.to);
        from.setThreshold(1);//will start working from first character
        to.setThreshold(1);
        from.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        to.setAdapter(adapter);
        date = (EditText) v.findViewById(R.id.date);
        myDatePicker = new MyDatePicker(getActivity(), date);
        time = (EditText) v.findViewById(R.id.time);
        myTimePicker = new MyTimePicker(getActivity(), time);
        search_send_btn = (Button) v.findViewById(R.id.search_send);
        search_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_from = from.getText().toString();
                str_to = to.getText().toString();
                str_date = date.getText().toString();
                str_time = time.getText().toString();
                alertDialog.show();
                searchForTremp();
                dismiss();

            }

        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "המשך", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return v;
    }

    public void searchForTremp() {

        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double timeplus10 = Double.parseDouble(str_time.substring(3,5)) + 10;
                double timeminus10 = Double.parseDouble(str_time.substring(3,5)) - 10;

                // searchInDB(dataSnapshot);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TrempData trempData = ds.getValue(TrempData.class);
                    double trempdataTime = Double.parseDouble(trempData.get_time().substring(3,5));

                    if(trempData.get_from().equals(str_from)
                            && trempData.get_to().equals(str_to)
                            && trempData.get_date().equals(str_date)
                            && (trempdataTime <= timeplus10 && trempdataTime >= timeminus10) ){

                        sendSMS(trempData.get_phone(),"מערכת Ride With Me מצאה אותך מתאים  עבור הגדרות החיפוש שלי. אם זה עדיין רלוונטי אשמח שתיצור איתי קשר במספר זה. תודה רבה, " + userName[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendSMS(String phoneNumber, String msg){
        if(ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{android.Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SEND_SMS);
        }
        else{
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNumber,null,parts,null,null);

        }

    }

    public void getUserName(){

        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);

                    if(mAuth.getCurrentUser().getUid().toString().equals(user.getId().toString())){
                        userName[0]= user.getName().toString();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}


