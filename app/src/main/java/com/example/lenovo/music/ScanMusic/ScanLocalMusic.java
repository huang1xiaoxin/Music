package com.example.lenovo.music.ScanMusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.lenovo.music.Songs;

import java.util.ArrayList;

public class ScanLocalMusic {
    private ArrayList<Songs> songsList;
    public ArrayList<Songs> getMap3Infos(ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<Songs> mp3Infos = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Songs song = new Songs();
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));    //文件路径

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration / (1000 * 60) >= 1) {        //只把1分钟以上的音乐添加到集合当中
                song.setId(id);
                song.setTitle(title);
                song.setArtist(artist);
                song.setDuration(duration);
                song.setUrl(url);
                mp3Infos.add(song);
            }

        }
        songsList=mp3Infos;
        return mp3Infos;
    }

}
