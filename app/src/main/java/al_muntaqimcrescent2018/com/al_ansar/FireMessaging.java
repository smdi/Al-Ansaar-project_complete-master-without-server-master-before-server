package al_muntaqimcrescent2018.com.al_ansar;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Imran on 23-02-2018.
 */

public class FireMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            String title = remoteMessage.getNotification().getTitle();
            String content = remoteMessage.getNotification().getBody();

            NotificationManageri.getInstance(getApplicationContext()).displayNotification(title, content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}