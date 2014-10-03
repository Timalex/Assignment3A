package com.example.alexander.assignment3a;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by Alexander on 2014-10-01.
 */
public class PlaylistHelper
{
    Context context;


    public PlaylistHelper(Context context)
    {
        this.context = context;
    }

    private boolean isStorageAvailable()
    {
        String storageState = Environment.getExternalStorageState();

        // Return true if external media is mounted
        return Environment.MEDIA_MOUNTED.equals(storageState) ||
               Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState);
    }

    public ArrayList<Track> getTrackPlaylist()
    {
        ArrayList<Track> playlist = new ArrayList<Track>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]
                        {
                                MediaStore.Audio.AudioColumns.ARTIST,
                                MediaStore.Audio.AudioColumns.TITLE,
                                MediaStore.Audio.AudioColumns.DATA
                        },
                MediaStore.Audio.AudioColumns.IS_MUSIC + " > 0",
                null,
                null
        );

        while(cursor.moveToNext())
        {
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
            String dataPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));

            playlist.add(new Track(artist,title,dataPath));
        }

        return playlist;
    }

    public Cursor getPlaylistCursor()
    {
        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]
                {
                    MediaStore.Audio.AudioColumns._ID,
                    MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                    MediaStore.Audio.AudioColumns.ALBUM,
                    MediaStore.Audio.AudioColumns.ARTIST,
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.DATA
                },
                MediaStore.Audio.AudioColumns.IS_MUSIC + " > 0",
                null,
                null
        );
    }

    public ArrayList<Long> getPlaylistIds()
    {
        Cursor playlistCursor = getPlaylistCursor();

        ArrayList<Long> playlist = new ArrayList<Long>();

        while(playlistCursor.moveToNext())
        {
            playlist.add(
                    playlistCursor.getLong(
                    playlistCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)));
        }
        return playlist;
    }

    public Uri getTrackPath(long trackId)
    {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.AudioColumns.DATA },
                MediaStore.Audio.AudioColumns._ID + " = " + trackId,
                null,
                null
        );

        if (cursor.moveToFirst())
        {
            String uriString = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            return Uri.parse(uriString);
        }

        return null;
    }

    public String getTrackTitle(long trackId)
    {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.AudioColumns.TITLE },
                MediaStore.Audio.AudioColumns._ID + " = " + trackId,
                null,
                null
        );

        if (cursor.moveToFirst())
        {
             return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
        }

        return null;
    }
}
