package com.example.alexander.assignment3a;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PlayerIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_TO_FOREGROUND = "foreground";
    public static final String ACTION_TO_BACKGROUND = "background";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.alexander.assignment3a.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.alexander.assignment3a.extra.PARAM2";

    private static final int PLAYER_NOTIFICATION_ID = 1234;

    private static final int REQUEST_CODE_NOTIFY = 1;

    public PlayerIntentService() {
        super("PlayerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TO_FOREGROUND.equals(action)) {
                notifyForeground();
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            } else if (ACTION_TO_BACKGROUND.equals(action)) {
                stopForeground(true);
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    private void notifyForeground()
    {
        Intent showPlayer = new Intent(this, SongSelectorActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NOTIFY, showPlayer, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification playerNotification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("contentTitle")
                .setContentText("contentText")
                .setContentIntent(pendingIntent)
                .build();
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1,playerNotification);
        startForeground(PLAYER_NOTIFICATION_ID,playerNotification);
        Log.d("inForeground", "started");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
