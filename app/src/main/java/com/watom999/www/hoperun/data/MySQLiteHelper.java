package com.watom999.www.hoperun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.watom999.www.hoperun.entity.UserInfoEntity;
import com.watom999.www.hoperun.utils.Logout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class MySQLiteHelper {
    public static final String DB_NAME = "uers_info";//数据库名称
    public static final String DB_TABLE_NAME = "login_info";//数据表名称
    public static final int VERSION = 1;//版本 更新的时候有用,一般无用
    public static SQLiteDatabase dbInstance;
    private MyDBHelper myDBHelper;//帮助类,创建表字段
    private StringBuffer tableCreate;
    private Context context;

    public MySQLiteHelper(Context context)
    {
        this.context = context;
    }

    public void openDatabase() {
        //创建数据库
        if (dbInstance == null) {
            myDBHelper = new MyDBHelper(context, DB_NAME, VERSION);
            //如果DB_NAME数据库不存在，就先创建数据库，再获取可读可写的数据库对象；如果数据库存在，就直接打开数据库对象。
            dbInstance = myDBHelper.getWritableDatabase();
            //如果存储空间（内部存储）满了，那么只能返回只读数据库对象。
            myDBHelper.getReadableDatabase();
        }
    }

    /**
     * 往数据库里面的product表插入一条数据，若失败返回-1
     * @return 失败返回-1
     */
    public long insert(UserInfoEntity o) {
        ContentValues values = new ContentValues();
        values.put("Login_account", o.getLogin_account());
        values.put("Login_password", o.getLogin_password());
        values.put("page_id", o.getPage_id());
        return dbInstance.insert(DB_TABLE_NAME, null, values);
    }

    /**
     * 获得数据库中所有的用户，将每一个用户放到一个map中去，然后再将map放到list里面去返回
     * @return list
     */
    public ArrayList<HashMap<String, Object>> getUserInfo() {
        Cursor cursor = null;
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        cursor = dbInstance.query(DB_TABLE_NAME, new String[] { "_id",
                        "login_account", "login_password","page_id"}, null,
                null, null,null, null);
        while (cursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
            item.put("login_account", cursor.getString(cursor.getColumnIndex("login_account")));
            item.put("login_password",cursor.getString(cursor.getColumnIndex("login_password")));
            item.put("page_id",cursor.getString(cursor.getColumnIndex("page_id")));
            list.add(item);
        }
        return list;
    }

    /**
     * 当数据库已被创建且版本未变化时，MyDBHelper类中的方法onCreate和onUpgrade，数据库只被调用一次。
     */
    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        /**
         * 1、创建数据库DB_NAME。如果数据库已经被创建了或数据库存在，就不会在调用此方法来创建数据库。
         * 2、在数据库中创建的数据表DB_TABLE_NAME。
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Logout.d("已经创建了数据库DB_NAME");
            //创建的数据表DB_TABLE_NAME
            tableCreate = new StringBuffer();
            tableCreate.append("create table ").append(DB_TABLE_NAME)
                    .append(" (")
                    .append("_id integer primary key autoincrement,")
                    .append("user_ID text,").append("user_name text").append("login_account text").append("login_password text").append("page_id text")
                    .append(")");
            System.out.println(tableCreate.toString());
            db.execSQL(tableCreate.toString()); //执行SQL语句创建数据表
        }

        /**
         * 当数据库DB_NAME的版本号变大时，会调用此方法。版本号表小时不会调用此方法，因为数据库不能降级。
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "drop table if exists " + DB_TABLE_NAME;
            db.execSQL(sql);
            myDBHelper.onCreate(db);
        }
    }
}
