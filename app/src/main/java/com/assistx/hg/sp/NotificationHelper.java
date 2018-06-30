//package com.assistx.hg.sp;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//
//
//@RequiresApi(api = Build.VERSION_CODES.N)
//public class NotificationHelper extends ContextWrapper {
//
//    private static final String channelID = "channel1ID";
//    private static final String channelName = "channel1Name";
//    private NotificationManager manager;
//
//
//    public NotificationHelper(Context base) {
//        super(base);
//    }
//
//
//    private void createChannels() {
//        NotificationChannel mChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
//        mChannel.enableLights(true);
//        mChannel.setLightColor(Color.RED);
//        getManager().createNotificationChannel(mChannel);
//    }
//
//    public NotificationManager getManager() {
//        if (manager == null)
//            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        return manager;
//    }
//
//    public Notification.Builder getChannelNotification(String title, String body) {
//        return new Notification.Builder(getApplicationContext(), channelID)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.redl)
//                .setAutoCancel(true)
//                .setContentTitle(title);
//    }
//
//}
//
//
