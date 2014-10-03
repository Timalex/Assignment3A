package com.example.alexander.assignment3a;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Alexander on 2014-10-01.
 */
public class MusicCursorAdapter extends CursorAdapter
{

    ViewHolder viewHolder;
    LayoutInflater inflater;
    MediaMetadataRetriever metadataRetriever;

    public MusicCursorAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
        metadataRetriever = new MediaMetadataRetriever();
    }


    // Anropas när en ny vy behöver skapas
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        // Håller kontaktvyn så att den kan återanvändas
        viewHolder = new ViewHolder();

        View view = inflater.inflate(R.layout.row_song, parent, false);
        viewHolder.name = (TextView) view.findViewById(R.id.viewArtist);
        viewHolder.image = (ImageView) view.findViewById(R.id.viewCover);

        // Låt kontaktlayouten hålla reda på sin viewHolder
        view.setTag(viewHolder);

        return view;
    }

    // Anropas när data behöver knytas till en vy
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        // Hämta viewholdern som kontaktvyn höll reda på
        viewHolder = (ViewHolder) view.getTag();

        // Hämta namn och bildadress för vyn
        String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
        String trackData = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));


        metadataRetriever.setDataSource(trackData);
        byte[] coverData = metadataRetriever.getEmbeddedPicture();
        if (coverData != null)
        {
            Bitmap coverImage = BitmapFactory.decodeByteArray(coverData, 0, coverData.length);
            viewHolder.image.setImageBitmap(coverImage);
        }
        else
        {
            viewHolder.image.setImageResource(R.drawable.ic_action_picture);
        }



        viewHolder.name.setText(trackTitle);

    }

    public static class ViewHolder{
        public TextView name;
        public ImageView image;
    }
}
