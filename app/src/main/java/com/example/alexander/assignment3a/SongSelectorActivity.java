package com.example.alexander.assignment3a;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;


public class SongSelectorActivity extends ListActivity implements MediaController.MediaPlayerControl
{

    Intent serviceIntent;
    PlayerService playerService;
    boolean isBoundToService;
    boolean isPauseButtonVisible;

    private static final String EXTRA_PAUSE_VISIBILITY = "pause button visibility";

    private PlaylistHelper playlistHelper;

    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) service;
            playerService = playerBinder.getService();
            isBoundToService = true;

            playerService.setPlaylist(playlist);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isBoundToService = false;
        }
    };

    private MenuItem playButton;
    private MenuItem pauseButton;
    private TrackAdapter trackAdapter;
    private ArrayList<Track> playlist;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        serviceIntent = new Intent(this,PlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);

        playlistHelper = new PlaylistHelper(this);
        playlist = playlistHelper.getTrackPlaylist();
        trackAdapter = new TrackAdapter(this, playlist);
        setListAdapter(trackAdapter);

//        mediaController = new MediaController(this,false);
//        mediaController.setMediaPlayer();
//        mediaController.setEnabled(true);
//        mediaController.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controls, menu);
        playButton = menu.findItem(R.id.action_play);
        pauseButton = menu.findItem(R.id.action_pause);
        togglePauseButton();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_play:
                playerService.play();
                togglePauseButton();
                return true;
            case R.id.action_pause:
                playerService.pause();
                togglePauseButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.d("Paused", "Activity paused");
//        playerIntentService.setAction(PlayerIntentService.ACTION_TO_FOREGROUND);
//        startService(playerIntentService);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_PAUSE_VISIBILITY,isPauseButtonVisible);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(isBoundToService)
        {
            unbindService(serviceConnection);
            isBoundToService = false;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        playerIntentService.setAction(PlayerIntentService.ACTION_TO_BACKGROUND);
//        startService(playerIntentService);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        playerService.playTrack(playlist.get(position));
        togglePauseButton();
    }

    private void togglePauseButton()
    {
        if (playerService.isMusicPlaying())
        {
            playButton.setVisible(false);
            pauseButton.setVisible(true);
            isPauseButtonVisible = true;
        }
        else
        {
            playButton.setVisible(true);
            pauseButton.setVisible(false);
            isPauseButtonVisible = false;
        }
    }
}
