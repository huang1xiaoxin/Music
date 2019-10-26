package com.example.lenovo.music;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.music.LikeMusicFragment.LikeMusicFragment;
import com.example.lenovo.music.LocalMusic.LocalMusicList;
import com.example.lenovo.music.MusicContent.MusicContentFragment;
import com.example.lenovo.music.RecentPlayFragment.RecentMusicPlay;
import com.example.lenovo.music.Service.MyService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class  MainActivity extends AppCompatActivity implements LocalMusicList.LocalMusicFragmentLister , View.OnClickListener {

    private MyService.MusicBinder musicBinder;
    private Bundle bundle=new Bundle();
    private ImageButton startButton;
    private ImageView bottomImage;
    private TextView bottomSinger;
    private  TextView bottomTilde;
    private ImageButton playModeButton;
    private ImageButton nextButton;
    private ImageButton lastButtom;
    private SeekBar seekBar;
    private TextView maxTime;
    private TextView currentTime;
    private int count=0;
    public static final int UPDATE = 1;
    private Timer timer = new Timer();
    private int i=1;
    private Songs sendToMusicContent;
    private DataDo dataDo;
    //更新进度条的操作
    private TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
           if(musicBinder.isPlaying()){
               handler.sendEmptyMessage(UPDATE);
           }

        }
    };

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE:
                    seekBar.setProgress(musicBinder.getCurrentPosition());
                    break;
                    default:break;
            }
        }
    };


    //服务器的连接
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("服务器：","已连接");
            musicBinder=(MyService.MusicBinder) service;
            musicBinder.setLister(MainActivity.this);
            bundle.putSerializable("binder",musicBinder);//传一个对象

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDo=DataDo.getInstance(this);
        startButton=findViewById(R.id.Stop_Button);
        startButton.setImageResource(R.drawable.stopsong);
        bottomImage =findViewById(R.id.Song_image);
        bottomSinger =findViewById(R.id.singer_text);
        bottomTilde =findViewById(R.id.SongItem_text);
        playModeButton=findViewById(R.id.PlayStyle_Button);
        findViewById(R.id.likeMusicButton2).setOnClickListener(this);
        findViewById(R.id.recentMusicButton2).setOnClickListener(this);
        ImageButton localButton=findViewById(R.id.localMusicButton);
        localButton.setOnClickListener(this);
        ImageButton likeButton=findViewById(R.id.likeMusicButton);
        likeButton.setOnClickListener(this);
        ImageButton recentButtom=findViewById(R.id.recentMusicButton);
        recentButtom.setOnClickListener(this);
        maxTime=findViewById(R.id.MaxTime);
        currentTime=findViewById(R.id.CurrentTime);
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicBinder.isPlaying()){
                    musicBinder.pauseMusic();
                    startButton.setImageResource(R.drawable.startsong);
                }else {
                    musicBinder.startMusic();
                    startButton.setImageResource(R.drawable.stopsong);
                }
            }
        });
        playModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
               switch (count){
                   case 1:
                       playModeButton.setImageResource(R.drawable.shuiji);
                       musicBinder.setRandomPlay(true);
                       musicBinder.setCyclePlay(false);
                       musicBinder.setOrderPlay(false);
                       break;
                   case 2:
                       playModeButton.setImageResource(R.drawable.danqu);
                       musicBinder.setRandomPlay(false);
                       musicBinder.setCyclePlay(true);
                       musicBinder.setOrderPlay(false);
                       break;
                   case 3:
                       playModeButton.setImageResource(R.drawable.sunxu);
                       musicBinder.setRandomPlay(false);
                       musicBinder.setCyclePlay(false);
                       musicBinder.setOrderPlay(true);
                       count=0;
                       break;
                       default:break;
                }

            }
        });
        nextButton=findViewById(R.id.NextSong_Button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicBinder.nextSong();
                changeButtomInfo(musicBinder.getSongsToMain());
            }
        });
        lastButtom=findViewById(R.id.Before_Button);
        lastButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicBinder.lastSong();
                changeButtomInfo(musicBinder.getSongsToMain());
            }
        });
        seekBar=findViewById(R.id.SeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //fromUser是用来判断用户是否改变的滑块的值
            //第一个是滑块滑动时调用
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               currentTime.setText(formatTime(progress));
            }
            //是开始滑动时调用
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            //滑动结束时调用
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicBinder.seekToPosition(seekBar.getProgress());

            }
        });
        initPermission();
    }
    @Override
    public void onClick(View v){
        if(!musicBinder.getIsMusicContentPage()){
            switch(v.getId()){
                case R.id.localMusicButton:
                    LocalMusicList localMusicList=new LocalMusicList();
                    localMusicList.setArguments(bundle);
                    ReplaceFragment(localMusicList);
                    break;
                case R.id.likeMusicButton:
                    LikeMusicFragment likeMusicFragment=new LikeMusicFragment();
                    likeMusicFragment.setArguments(bundle);
                    ReplaceFragment(likeMusicFragment);
                    Log.e("已导航到：","我喜欢的页面");
                    break;
                case R.id.recentMusicButton:
                    RecentMusicPlay recentMusicPlay=new RecentMusicPlay();
                    recentMusicPlay.setArguments(bundle);
                    ReplaceFragment(recentMusicPlay);
                    Log.e("已导航到：","最近播放页面");
                    break;
                case R.id.likeMusicButton2:
                    LikeMusicFragment likeMusicFragment1=new LikeMusicFragment();
                    likeMusicFragment1.setArguments(bundle);
                    ReplaceFragment(likeMusicFragment1);
                    break;
                case R.id.recentMusicButton2:
                    RecentMusicPlay recentMusicPlay1=new RecentMusicPlay();
                    recentMusicPlay1.setArguments(bundle);
                    ReplaceFragment(recentMusicPlay1);
                   break;
                default:
                    break;
            }
        }

    }
    //底部的播放栏点击跳转到另一页面,注意要是public类型，不可以是private类型，否则调用不了
    public void Layout_Click(View v){
        if(!musicBinder.getIsMusicContentPage()){
            switch (v.getId()){
                case R.id.bottom_layout:
                    if(!musicBinder.getIsMusicContentPage()){
                        MusicContentFragment musicContentFragment =new MusicContentFragment();
                        musicContentFragment.setArguments(bundle);
                        ReplaceFragment(musicContentFragment);
                    }else {
                        return;
                    }
                    break;
                    default: break;

            }
        }
    }


    //转换时间
    private String formatTime(int length){
        Date date=new Date(length);
        SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
        return sdf.format(date);
    }
    private  void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.MusicList_Fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    //用以更改信息
    public void changeButtomInfo(Songs songs){
        if(songs!=null){
            dataDo.addMusic("recent",songs);
            sendToMusicContent=songs;
      //      int finalTime = (int) songs.getDuration();
            seekBar.setMax((int) songs.getDuration());
            maxTime.setText(formatTime((int) songs.getDuration()));
            startButton.setImageResource(R.drawable.stopsong);
            if(loadingCover(songs.getUrl())==null){
                bottomImage.setImageResource(R.drawable.musicimage);
            }else {
                bottomImage.setImageBitmap(loadingCover(songs.getUrl()));
            }
            if(songs.getTitle()==null){
                bottomTilde.setText("未知");
            }else {
                bottomTilde.setText(songs.getTitle());
            }
            if(songs.getArtist()==null){
                bottomSinger.setText("未知");
            }else {
                bottomSinger.setText(songs.getArtist());
            }
            if(i==1){
                timer.schedule(timerTask,0,500);
                i++;
            }


        }else {
            return;
        }

    }

    public Bitmap loadingCover(String mediaUri){
        Bitmap bitmap=null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mediaUri);
            byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
            bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);
        }catch (Exception e){}
        return bitmap;
    }
    public Songs getSendToMusicContent(){
        return sendToMusicContent;
    }



    public interface MainActivityLister{
        void changeImage(Songs songs);
    }
    //动态申请存储读取的权限
    private void initPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE )!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }
}
