package com.example.lenovo.music.Adpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.music.R;
import com.example.lenovo.music.Songs;

import java.util.List;


public class LocalMusicAdapter extends ArrayAdapter<Songs> {
    private int resourceId;
    public LocalMusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Songs> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Songs songs=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.singer=view.findViewById(R.id.List_TextSinger);
            viewHolder.title=view.findViewById(R.id.List_TextTitle);
            viewHolder.image=view.findViewById(R.id.SongImage);
            view.setTag(viewHolder);

        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.title.setText(songs.getTitle());
        viewHolder.singer.setText(songs.getArtist());
        if(loadingCover(songs.getUrl())==null){
            viewHolder.image.setImageResource(R.drawable.musicimage);
        }else {
            viewHolder.image.setImageBitmap(loadingCover(songs.getUrl()));
        }
        return  view;
    }
    class ViewHolder{
        TextView singer;
        TextView title;
        ImageView image;
    }
    /*
    获取专辑的封面
     */
    private Bitmap loadingCover(String mediaUri){
        Bitmap bitmap=null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mediaUri);
            byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
             bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);

        }catch (Exception e){}
        return bitmap;

    }


}
