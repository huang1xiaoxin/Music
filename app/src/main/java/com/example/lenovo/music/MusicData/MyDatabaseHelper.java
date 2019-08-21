package com.example.lenovo.music.MusicData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper{
    //创建一个用于储存我我喜欢的音乐
    public static  final String CREATE_IIKEMUSICBOOK="create table likeMusicBook(" +
            "id integer primary key autoincrement,"+"songId integer not null,"+
            "title text not null,"+"singer text not null,"+"duration integer not null,"+"uri text not null)";
    public static  final String CREATE_RECENTSICBOOK="create table recent(" +
            "id integer primary key autoincrement,"+"songId integer not null,"+
            "title text not null,"+"singer text not null,"+"duration integer not null,"+"uri text not null)";
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }
    private Context mContext;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_IIKEMUSICBOOK);
        db.execSQL(CREATE_RECENTSICBOOK);
        Log.e("数据库","已创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists likeMusicBook");
        db.execSQL("drop table if exists recent");
        onCreate(db);
    }
}
