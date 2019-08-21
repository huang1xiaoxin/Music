package com.example.lenovo.music.LocalMusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.music.Adpater.LocalMusicAdapter;
import com.example.lenovo.music.MainActivity;
import com.example.lenovo.music.R;
import com.example.lenovo.music.ScanMusic.ScanLocalMusic;
import com.example.lenovo.music.Service.MyService;
import com.example.lenovo.music.Songs;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicList extends Fragment {
    public ScanLocalMusic scanLocalMusic;
    public Activity myActivity;
    public ArrayList<Songs> list;
    public LocalMusicAdapter localMusicAdapter;
    private ListView listView;
    private MyService.MusicBinder musicBinder;
    private MainActivity mA;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.music_list,container,false);
        scanLocalMusic=new ScanLocalMusic();
        mA =(MainActivity) getActivity();
        try{
            myActivity=getActivity();
            list=scanLocalMusic.getMap3Infos(myActivity.getContentResolver());
            localMusicAdapter=new LocalMusicAdapter(myActivity.getApplicationContext(), R.layout.list_date, list);
            listView=view.findViewById(R.id.List_music);
            listView.setAdapter(localMusicAdapter);
        }catch (Exception e){}
        musicBinder = (MyService.MusicBinder) getArguments().getSerializable("binder");
        ListView listView=view.findViewById(R.id.List_music);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                musicBinder.setIndex(position);
                musicBinder.setMusicList(list);
                musicBinder.startPlay(list.get(position).getUrl());
                Log.e("LocalMusic","playing");
                mA.changeButtomInfo(list.get(position));
                Log.e("MainActivity","已更改信息");
            }catch (Exception e){}

            }
        });
        return view;
    }
    public interface LocalMusicFragmentLister{
        void changeButtomInfo(Songs songs);

    }


}
