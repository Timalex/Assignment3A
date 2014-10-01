package com.example.alexander.assignment3a;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class SongSelectorActivity extends Activity {

    Intent playerIntentService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerIntentService = new Intent(this,PlayerIntentService.class);
        startService(playerIntentService);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.d("Paused", "Activity paused");
        playerIntentService.setAction(PlayerIntentService.ACTION_TO_FOREGROUND);
        startService(playerIntentService);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        playerIntentService.setAction(PlayerIntentService.ACTION_TO_BACKGROUND);
//        startService(playerIntentService);

    }
}
