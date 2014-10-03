package com.example.alexander.assignment3a;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerService extends Service {

    // Set to true when the player has been prepared
    private static boolean playerUsedBefore = false;
    // Keeps "track" of the track last used
    private int currentTrackId = 0;
    public MediaPlayer musicPlayer;
    // Assigns my playerBinder defined further down in the code
    private final IBinder binder = new PlayerBinder();

    // The service also has a reference to the tracklist prevent dependency on the activity
    private ArrayList<Track> playlist;

    // Warning, this must have a value of 1
    private static final int NOTIFICATION_ID = 1;
    // Name says it
    private static final int INTENT_FROM_NOTIFICATION = 22;

    public PlayerService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        musicPlayer = new MediaPlayer();

        // Heightens the process priority while being visible with a notification. This notification is required because of previous abuse of android
        startForeground(NOTIFICATION_ID, createNotification(getString(R.string.notification_placeholder_text)));
    }

    // Receives a reference to the same playlist as the activity
    public void setPlaylist(ArrayList<Track> playlist)
    {
        this.playlist = playlist;
    }

    // Creates my custom Binder that returns this service instance
    public class PlayerBinder extends Binder{
        PlayerService getService()
        {
            return PlayerService.this;
        }
    }

    // Returns the binder to clients of the service, like our activity
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    // This exists to make the service stick around even if the service is unbound
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    // Convenience method for building a Notification with a pending intent
    private Notification createNotification(String contentTitle)
    {
        Intent showPlayer = new Intent(this, SongSelectorActivity.class);
        // This intent is pending until the user clicks the notification
        // The flag makes sure changes to it only makes modifications instead of new copies
        PendingIntent pendingIntent = PendingIntent.getActivity(this, INTENT_FROM_NOTIFICATION, showPlayer, PendingIntent.FLAG_UPDATE_CURRENT);
        // This is the new way to build notifications
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(contentTitle)
                .setContentText(getString(R.string.notification_content_text))
                .setContentIntent(pendingIntent)
                // Cannot be dismissed by the user
                .setOngoing(true)
                .build();
    }

    // play a specific track
    public void playTrack(final Track track)
    {
        String title = track.getTitle();
        String dataPath = track.getDataPath();

        try
        {
            musicPlayer.reset();
            // Loads music from the filepath provided
            musicPlayer.setDataSource(this, Uri.parse(dataPath));
            // Decides on which stream the music will be playing, instead of say a ringtone stream
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musicPlayer.prepare();
            playerUsedBefore = true;
            musicPlayer.start();
            currentTrackId = playlist.indexOf(track);
            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                // Called when a track is finished playing
                @Override
                public void onCompletion(MediaPlayer mp)
                {

                    if (currentTrackId < playlist.size() - 1)
                    {
                        playTrack(playlist.get(++currentTrackId));
                    }
                    else
                    {
                        playFirstTrack();
                    }
                }
            });
            // Modify notification with new title
            startForeground(NOTIFICATION_ID, createNotification(title));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    // Just play a song
    public int play()
    {
        if (playerUsedBefore)
        {
            musicPlayer.start();
            return currentTrackId;
        }
        else
        {
            return playFirstTrack();
        }
    }

    public void pause()
    {
        musicPlayer.pause();
    }

    public int nextTrack()
    {
        int nextTrackId = currentTrackId + 1;
        if (nextTrackId < playlist.size())
        {
            playTrack(playlist.get(nextTrackId));
            return nextTrackId;
        }
        else
        {
            return playFirstTrack();
        }
    }

    public int previousTrack()
    {
        int previousTrackId = currentTrackId - 1;
        if (previousTrackId >= 0)
        {
            playTrack(playlist.get(previousTrackId));
            return previousTrackId;
        }
        else
        {
            return playFirstTrack();
        }
    }




    private int playFirstTrack()
    {
        playTrack(playlist.get(0));
        return 0;
    }

    public boolean isMusicPlaying()
    {
        return musicPlayer.isPlaying();
    }

    public void jumpInsideTrack(int pos)
    {
        musicPlayer.seekTo(pos);
    }
}
