package dk.easv.ATForum;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {
    public static final String channelId1 = "channel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotifications();
    }

    private void createNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(channelId1, "Notification1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel for topic notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
