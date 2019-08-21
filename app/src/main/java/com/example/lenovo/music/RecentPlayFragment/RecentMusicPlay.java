package com.example.lenovo.music.RecentPlayFragment;

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
import android.widget.Toast;

import com.example.lenovo.music.Adpater.LocalMusicAdapter;
import com.example.lenovo.music.DataDo;
import com.example.lenovo.music.MainActivity;
import com.example.lenovo.music.R;
import com.example.lenovo.music.Service.MyService;
import com.example.lenovo.music.Songs;

import java.util.ArrayList;

public class RecentMusicPlay extends Fragment {

    private MyService.MusicBinder musicBinder;
    private MainActivity mA;
    private ArrayList<Songs> arrayList;
    private DataDo dataDo;
    private ListView listView;
    private LocalMusicAdapter localMusicAdapter;
    private ImageButton cleanButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.recent_music,container,false);
        listView=view.findViewById(R.id.List_recentMusic);
        cleanButton=view.findViewById(R.id.clean_recent);
        mA= (MainActivity) getActivity();
        musicBinder= (MyService.MusicBinder) getArguments().getSerializable("binder");
        dataDo=new DataDo(getActivity());
        arrayList=dataDo.getDataSongs("recent");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    musicBinder.setIndex(position);
                    musicBinder.setMusicList(arrayList);
                    musicBinder.startPlay(arrayList.get(position).getUrl());
                    musicBinder.setIsRecentMusicList(true);
                    Log.e("LocalMusic","playing");
                    mA.changeButtomInfo(arrayList.get(position));
                    Log.e("MainActivity","已更改信息");
                }catch (Exception e){}
            }
        });
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList!=null){
                    dataDo.deleteAll("recent");
                    arrayList.clear();
                    localMusicAdapter=new LocalMusicAdapter((getActivity().getApplicationContext()),R.layout.list_date, arrayList);
                    listView.setAdapter(localMusicAdapter);
                    if(musicBinder.getIsRecentMusicList()){
                        musicBinder.setMusicList(arrayList=null);
                    }

                }
                Toast.makeText(getActivity(),"历史播放记录已清除！！",Toast.LENGTH_SHORT).show();

            }
        });
        if(arrayList!=null){
            try {
                localMusicAdapter=new LocalMusicAdapter((getActivity().getApplicationContext()),R.layout.list_date, arrayList);
                listView.setAdapter(localMusicAdapter);
            }catch (Exception e){}
            return view;
        }else {
            return view;
        }
    }
}
