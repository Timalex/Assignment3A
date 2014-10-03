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

    private static boolean playerUsedBefore = false;
    private int currentTrackId = 0;
    public MediaPlayer musicPlayer;
    private final IBinder binder = new PlayerBinder();

    private ArrayList<Track> playlist;

    private static final int NOTIFICATION_ID = 1;
    private static final int LAUNCHED_FROM_NOTIFICATION = 22;

    public PlayerService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        musicPlayer = new MediaPlayer();

        startForeground(NOTIFICATION_ID, createNotification("Här visas sångens namn"));
    }

    public void setPlaylist(ArrayList<Track> playlist)
    {
        this.playlist = playlist;
    }

    public class PlayerBinder extends Binder{
        PlayerService getService()
        {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    private Notification createNotification(String contentTitle)
    {
        Intent showPlayer = new Intent(this, SongSelectorActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, LAUNCHED_FROM_NOTIFICATION, showPlayer, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(contentTitle)
                .setContentText(getString(R.string.notification_content_text))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    public void playTrack(final Track track)
    {
        String title = track.getTitle();
        String dataPath = track.getDataPath();

        try
        {
            musicPlayer.reset();
            musicPlayer.setDataSource(this, Uri.parse(dataPath));
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musicPlayer.prepare();
            musicPlayer.start();
            playerUsedBefore = true;
            currentTrackId = playlist.indexOf(track);
            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
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
            startForeground(NOTIFICATION_ID, createNotification(title));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

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
