package com.commercial.tuds.earnandpay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static androidx.core.content.ContextCompat.getSystemService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String MyUid = FirebaseAuth.getInstance().getUid();
        if (MyUid != null)
            FirebaseDatabase.getInstance().getReference().child("meta_data").child("device_tokens").child(FirebaseAuth.getInstance().getUid()).setValue(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notificationType = remoteMessage.getData().get("data_type");
        Log.d("msg","####"+remoteMessage.toString());
        Log.d("msg","####"+ remoteMessage.getData().get("data_type"));

        if (notificationType.matches("discountCardNotification") || notificationType.matches("discountCardUpdateNotification")) {
            Intent intent = new Intent(this, ViewCardsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            showNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), intent);
        }else if (notificationType.matches("adminNotification")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            showNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), intent);
        }else if (notificationType.matches("transactionNotification")) {
            Intent intent = new Intent(this, TransactionHistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            showNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), intent);
        }else {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            showNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), intent);
        }
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}