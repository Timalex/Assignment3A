package com.example.alexander.assignment3a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alexander on 2014-10-03.
 */
public class TrackAdapter extends ArrayAdapter<Track>
{
    private final ArrayList<Track> playlist;
    private final LayoutInflater inflater;
    private final MediaMetadataRetriever metadataRetriever;
    private ViewHolder viewHolder;

    public TrackAdapter(Context context, ArrayList<Track> playlist)
    {
        super(context, R.layout.row_song, playlist);

        this.playlist = playlist;

        inflater = LayoutInflater.from(context);
        metadataRetriever = new MediaMetadataRetriever();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.row_song, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.viewCover);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.viewArtist);
            viewHolder.title = (TextView) convertView.findViewById(R.id.viewTitle);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Track track = playlist.get(position);

        viewHolder.artist.setText(track.getArtist());
        viewHolder.title.setText(track.getTitle());
        setCover(viewHolder.image,track.getDataPath());

        return convertView;
    }

    // This retrives the cover image from inside the mp3 file decodes it and uses it in the imageview
    private void setCover(ImageView imageHolder, String dataPath)
    {
        metadataRetriever.setDataSource(dataPath);
        byte[] coverData = metadataRetriever.getEmbeddedPicture();
        if (coverData != null)
        {
            Bitmap coverImage = BitmapFactory.decodeByteArray(coverData, 0, coverData.length);
            imageHolder.setImageBitmap(coverImage);
        }
        else
        {
            imageHolder.setImageResource(R.drawable.ic_action_picture);
        }
    }

    private static class ViewHolder
    {
        public ImageView image;
        public TextView artist;
        public TextView title;
    }
}
