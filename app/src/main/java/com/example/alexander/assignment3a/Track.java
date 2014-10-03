package com.example.alexander.assignment3a;

import android.net.Uri;

/**
 * Created by Alexander on 2014-10-03.
 */
public class Track
{
    private String artist;
    private String title;
    private String dataPath;

    public Track(String artist, String title, String dataUri)
    {
        this.artist = artist;
        this.title = title;
        this.dataPath = dataUri;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getDataPath()
    {
        return dataPath;
    }

    public String getTitle()
    {
        return title;
    }
}
