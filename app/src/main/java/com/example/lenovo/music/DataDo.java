package com.example.lenovo.music;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lenovo.music.MusicData.MyDatabaseHelper;

import java.util.ArrayList;

public class DataDo {
    private Context context;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    public  static  final  String Name="musicData.db";
    public  DataDo(Context context){
        this.context=context;
        myDatabaseHelper=new MyDatabaseHelper(context,Name,null,2);
        database=myDatabaseHelper.getWritableDatabase();
    }
    public void addMusic(String dateName,Songs songs){
        String singer=songs.getArtist();
        String path=songs.getUrl();
        String title=songs.getTitle();
        int durations= (int) songs.getDuration();
        int songId= (int) songs.getId();
        if(isExist(dateName, songId)){
            ContentValues values=new ContentValues();
            values.put("songId",songId);
            values.put("title",title);
            values.put("singer",singer);
            values.put("uri",path);
            values.put("duration",durations);
            database.insert(dateName,null,values);
            values.clear();
            Log.e("添加歌曲到数据库",title);
        }else {
            return;
        }

    }
    public void deleteSong(String dateName,Songs songs){
       database.delete(dateName, "songId=?",new String[]{String.valueOf(songs.getId())});
       Log.e("已从数据库中移除：",songs.getTitle());
    }
    //遍历数据库
    public ArrayList<Songs> getDataSongs(String dataName){
        ArrayList<Songs> list=new ArrayList<>();
        Cursor cursor=database.query(dataName,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Songs song=new Songs();
                String uri=cursor.getString(cursor.getColumnIndex("uri"));
                String singer=cursor.getString(cursor.getColumnIndex("singer"));
                String title=cursor.getString(cursor.getColumnIndex("title"));
                int duration =cursor.getInt(cursor.getColumnIndex("duration"));
                int id=cursor.getInt(cursor.getColumnIndex("songId"));
                song.setId(id);
                song.setTitle(title);
                song.setArtist(singer);
                song.setDuration(duration);
                song.setUrl(uri);
                list.add(song);
            }while (cursor.moveToNext());
        }else {
            list=null;
        }
        cursor.close();
        return list;
    }
    //判断这首歌曲时候已经存在数据库中
    public boolean isExist(String name,int id){
        int songsId=0;
        ArrayList<Songs>  arrayList=getDataSongs(name);
        if(arrayList!=null){
            for (int i=0;i<arrayList.size();i++){

                if(id==(int) arrayList.get(i).getId()){
                    songsId=id;
                    break;
                }
            }
        }
        return id != songsId;
    }
    //清除所有的数据
    public void deleteAll(String tableName) {
        database.execSQL("delete from "+tableName);
        database.execSQL("update sqlite_sequence SET seq = 0 where name ='"+tableName+"';");
        Log.e("已清除：","所有的数据");

    }

}
