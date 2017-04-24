package paztechnologies.com.meribus.FirebaseMessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.text.Html;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import paztechnologies.com.meribus.R;

/**
 * Created by Admin on 4/12/2017.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    private Context ctx;

    public MyNotificationManager(Context mctx) {
        this.ctx = mctx;
    }


    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification

    public void showBigNotification(String title, String msg, String url, Intent intent) {
        PendingIntent resulPendingIntent = PendingIntent.getActivity(ctx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(msg).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        Notification notification;
        notification = builder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0).setAutoCancel(true)
                .setContentIntent(resulPendingIntent).setStyle(bigPictureStyle).setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo)).setContentText(msg).build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }


    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification

    public void showSmallNotification(String title, String msg, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, ID_SMALL_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        Notification notification = builder.setSmallIcon(R.drawable.logo).setWhen(0).setAutoCancel(true).setContentTitle(title)
                .setContentIntent(pendingIntent).setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.logo))
                .setContentText(msg).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    private Bitmap getBitmapFromURL(String url) {
        try {

            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
