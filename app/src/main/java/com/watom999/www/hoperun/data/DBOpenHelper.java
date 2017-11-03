package com.watom999.www.hoperun.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun.data
 *@Function: 1、用数据库保存数据
 *@Description: 1、该类主要用来创建数据库表格
 *@CreatedDate: 2017/10/31 15:56
 *@UpDate: 1、
 ***********************************************/

public abstract class DBOpenHelper extends SQLiteOpenHelper {

    private static String name = "mydb.db"; //表示数据库的名称
    private static int version = 1; //更新数据库的版本号，此时会执行 onUpgrade()方法

    protected abstract int getDBVersion(Context context);

    protected abstract String getDBName(Context context);

    protected abstract String[] getCreatTableSQL(Context context);

    protected abstract String[] getUpdateTableSQL(Context context);

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table person(id integer primary key,name varchar(64),account varchar(64))";
        db.execSQL(sql);
//        db.execSQL("CREATE TABLE IF NOT EXISTS person" +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, website STRING,weibo STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "alter table person add sex varchar(8)";
        db.execSQL(sql);
    }
}
