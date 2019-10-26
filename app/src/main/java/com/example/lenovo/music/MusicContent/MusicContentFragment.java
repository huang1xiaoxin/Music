package com.example.lenovo.music.MusicContent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.music.DataDo;
import com.example.lenovo.music.MainActivity;
import com.example.lenovo.music.R;
import com.example.lenovo.music.Service.MyService;
import com.example.lenovo.music.Songs;

public class MusicContentFragment extends Fragment implements MainActivity.MainActivityLister {

    private ImageButton addLikeButton;
    private ImageView  musicView;
    private MainActivity mA;
    private Songs getSongs;
    private boolean isAddLike =false;
    private DataDo dataDo;
    private TextView textView;
    private MyService.MusicBinder musicBinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.musiccontent,container,false);
        mA=(MainActivity)getActivity();
        textView=view.findViewById(R.id.ContentMusic_Text);
        //获取musicBinder的对象
        musicBinder= (MyService.MusicBinder) getArguments().getSerializable("binder");
        assert musicBinder != null;
        musicBinder.setIsMusicContentPage(true);
        musicBinder.setMainActivityLister(MusicContentFragment.this);
        assert mA != null;
        getSongs=mA.getSendToMusicContent();
        if(getSongs!=null){
            dataDo=DataDo.getInstance(getContext());
            addLikeButton=view.findViewById(R.id.add_likeMusic);
            addLikeButton.setImageResource(R.drawable.dislike);
            textView.setText(getSongs.getTitle());
            if(!dataDo.isExist("likeMusicBook", (int) getSongs.getId())){
                addLikeButton.setImageResource(R.drawable.addlike);
                isAddLike =true;
                Log.e("这首歌是：","我喜欢的歌曲");

            }
            musicView=view.findViewById(R.id.content_imageView);
            Log.e("getMusic:",getSongs.getUrl());

            if(mA.loadingCover(getSongs.getUrl())==null){
                musicView.setImageResource(R.drawable.musicimage);
            }else {
                musicView.setImageBitmap(mA.loadingCover(getSongs.getUrl()));
            }
            addLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAddLike ==false){
                        addLikeButton.setImageResource(R.drawable.addlike);
                        dataDo.addMusic("likeMusicBook",getSongs);
                        Log.e("添加的喜欢歌曲是：",getSongs.getTitle());
                        isAddLike =true;
                    }
                    else{
                        addLikeButton.setImageResource(R.drawable.dislike);
                        dataDo.deleteSong("likeMusicBook",getSongs);
                        Log.e("删除的喜欢歌曲是：",getSongs.getTitle());
                        isAddLike =false;
                        if(musicBinder.getIsLikeMusicList()){
                            musicBinder.setMusicList(dataDo.getDataSongs("likeMusicBook"));
                        }
                    }
                }
            });
            return view;
        }else {
            return view;
        }

    }

    @Override
    public void changeImage(Songs songs) {
        if(songs!=null){
            getSongs=songs;
            textView.setText(getSongs.getArtist());
            if(!dataDo.isExist("likeMusicBook", (int) getSongs.getId())){
                addLikeButton.setImageResource(R.drawable.addlike);
                musicView.setImageBitmap(loadingImage(getSongs.getUrl()));
                isAddLike =true;
                Log.e("这首歌是：","我喜欢的歌曲");
            }else {
                addLikeButton.setImageResource(R.drawable.dislike);
                musicView.setImageBitmap(loadingImage(getSongs.getUrl()));
                isAddLike =false;
            }
        }else {
            return;
        }
    }

    @Override
    public void onDestroyView() {
        musicBinder.setIsMusicContentPage(false);
        super.onDestroyView();
    }
    //获取本地歌曲的图片，通过传入歌曲的本地地址
    public Bitmap loadingImage(String mediaUri){
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
