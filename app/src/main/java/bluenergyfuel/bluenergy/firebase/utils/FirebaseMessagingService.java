package bluenergyfuel.bluenergy.firebase.utils;

/**
 * Created by jockinjc0 on 4/28/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergy;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    Bitmap bitmapPic;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0 ){
            String messageData = remoteMessage.getData().get("message");
            String titleData = remoteMessage.getData().get("title");
            String imgUrlData = remoteMessage.getData().get("image");
            showNotification(messageData, titleData, imgUrlData);
        }
    }

    private void showNotification(String message, String title, String imageUrl) {
        Intent i = new Intent(this,BluEnergy.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, i,
                PendingIntent.FLAG_ONE_SHOT);
        try {
             bitmapPic = Picasso.with(getApplicationContext()).load(imageUrl).resize(1000,1000).get();

        }catch (IOException e){
            e.printStackTrace();
        }
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, "Open", pendingIntent)
                .addAction(0, "Remind", pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapPic)
                        .setBigContentTitle(title)
                        .setSummaryText(message)
                );
        notification.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notification.setLights(Color.RED, 3000, 3000);
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final int notifID = 1337;
        manager.notify(notifID, notification.build());

    }
}