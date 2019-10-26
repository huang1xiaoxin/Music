package com.example.lenovo.music.LikeMusicFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.music.Adpater.LocalMusicAdapter;
import com.example.lenovo.music.DataDo;
import com.example.lenovo.music.MainActivity;
import com.example.lenovo.music.MusicData.MyDatabaseHelper;
import com.example.lenovo.music.R;
import com.example.lenovo.music.Service.MyService;
import com.example.lenovo.music.Songs;

import java.util.ArrayList;

public class LikeMusicFragment extends Fragment  {
    private ArrayList<Songs> songsList;
    private ListView listView;
    private MainActivity mA;
    private MyService.MusicBinder musicBinder;
    private LocalMusicAdapter localMusicAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.music_list,container,false);
        listView=view.findViewById(R.id.List_music);
        musicBinder = (MyService.MusicBinder) getArguments().getSerializable("binder");
        mA= (MainActivity) getActivity();
        DataDo dataDo=DataDo.getInstance(getContext());
        if(songsList!=null){
            songsList.clear();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    musicBinder.setIsLikeMusicList(true);
                    musicBinder.setIndex(position);
                    musicBinder.setMusicList(songsList);
                    musicBinder.startPlay(songsList.get(position).getUrl());
                    Log.e("LikeMusic","IsPlaying");
                    mA.changeButtomInfo(songsList.get(position));
                    Log.e("MainActivity","已更改信息");
                }catch (Exception e){}
            }
        });
        songsList=dataDo.getDataSongs("likeMusicBook");
        if(songsList!=null){
            try {
                localMusicAdapter=new LocalMusicAdapter((getActivity().getApplicationContext()),R.layout.list_date, songsList);
                listView.setAdapter(localMusicAdapter);
            }catch (Exception e){}
            return view;
        }else {
            return view;
        }


    }

}
