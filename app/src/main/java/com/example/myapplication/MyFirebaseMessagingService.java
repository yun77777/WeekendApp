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
        //token을 서버로 전송
    }

    private static final String TAG = "FirebaseMsgService";

    private String msg, title;


    //

    String id = "my_channel_02";
    CharSequence name = "fcm_nt";
    String description = "push";
    int importance = NotificationManager.IMPORTANCE_LOW;
    MediaPlayer mediaPlayer;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived");

        if(remoteMessage.getNotification() != null) { //foreground
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

        } else if(remoteMessage.getData().size() > 0 ) {//background
            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
        }
//        title = remoteMessage.getNotification().getTitle();
//        msg = remoteMessage.getNotification().getBody();
//
//        Log.e(TAG, "title: " + title);
//        Log.e(TAG, "msg: " + msg);
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent contentintent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "test").setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setVibrate(new long[]{1, 1000});
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, mBuilder.build());
//
//        mBuilder.setContentIntent(contentintent);

    }

    private void sendNotification(String messageBody, String messageTitle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 2;
        String CHANNEL_ID = "my_channel_02";

        try {
            Notification notification = new Notification.Builder(MyFirebaseMessagingService.this)
                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
                    .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
                    .setSmallIcon(R.drawable.hello)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
//            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
//            mediaPlayer.start();

            mNotificationManager.notify(notifyID, notification);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}