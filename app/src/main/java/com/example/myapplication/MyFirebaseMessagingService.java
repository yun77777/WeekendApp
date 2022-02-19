package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived");

        if(remoteMessage.getNotification() != null) { //foreground
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

        } else if(remoteMessage.getData().size() > 0 ) {//background
            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
        }

        super.onMessageReceived(remoteMessage);
    }

    private void sendNotification(String messageBody, String messageTitle) {
        final String CHANNEL_ID = "my_channel_02";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Name",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.hello) // working well for foreground, data in body(not notification)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notification.build());
    }
}