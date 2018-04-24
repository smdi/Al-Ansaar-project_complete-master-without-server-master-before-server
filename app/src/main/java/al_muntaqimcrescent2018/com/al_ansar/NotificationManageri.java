package al_muntaqimcrescent2018.com.al_ansar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Imran on 23-02-2018.
 */

public class NotificationManageri {

    private Context context;
    private MediaPlayer mp;
    private static NotificationManageri notificationManager;

    private NotificationManageri(Context context)
    {
        this.context = context;
    }

    public static synchronized NotificationManageri getInstance(Context context)
    {
        if(context!=null)
        {

            notificationManager = new NotificationManageri(context);
        }

        return notificationManager;
    }

    public void displayNotification(String title ,String content)
    {

        Vibrator v =(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(1000);

        try {

            mp = MediaPlayer.create(context, R.raw.twitter_ios);
            mp.start();


        }
        catch (Exception e){

            e.printStackTrace();
        }

        Intent i1 = new Intent(context, MainActivity.class);
        PendingIntent pi1 = PendingIntent.getActivity(context, 1, i1, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.alansar);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.alansar)
                        .setColor(Color.WHITE)
                        .setLights(1,1,1)
                        .setContentTitle("Al Ansaar")
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setTicker("Al Ansaar")
                        .setLargeIcon(bitmap)
                        .setContentIntent(pi1);


        android.app.NotificationManager nf = (android.app.NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nf.notify(0, mBuilder.build());

    }

}