package com.example.ridewithme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Naor on 28/08/2017.
 */

public class Notifications {

    private Context ctx;
    private Class classAct;

    public Notifications(Context ctx,Class classAct){
        this.ctx = ctx;
        this.classAct = classAct;

    }

    public void Notifications(String bannerTxt, String titulo, String txt, int icon){

        NotificationManager notificationManager = (NotificationManager) this.ctx.getSystemService(this.ctx.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity (
                ctx.getApplicationContext(), 0,new Intent(ctx.getApplicationContext(),this.classAct),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.ctx.getApplicationContext());

        builder.setSmallIcon(icon);
        builder.setTicker(bannerTxt);
        builder.setContentTitle(titulo);
        builder.setContentIntent(pendingIntent);
        builder.setContentText(txt);


        Notification notification = builder.build();

        notificationManager.notify(icon, notification);

    }

}
