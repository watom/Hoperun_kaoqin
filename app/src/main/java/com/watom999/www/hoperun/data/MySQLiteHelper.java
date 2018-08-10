package com.watom999.www.hoperun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.watom999.www.hoperun.entity.UserInfoEntity;
import com.watom999.www.hoperun.utils.Logout;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class MySQLiteHelper {
    public static final String DB_NAME = "uers_info";//数据库名称
    public static final String DB_TABLE_NAME = "login_info";//数据表名称
    public static SQLiteDatabase dbInstance;
    private StringBuffer loginTableCreate;
    public static final int VERSION = 2;//修改版本号，可调用onUpgrade方法来更新数据库。
    private MyDBHelper myDBHelper;//帮助类,创建表字段
    private Context context;

    public MySQLiteHelper(Context context)
    {
        this.context = context;
        openDatabase();
    }
    /**
     * 创建并打开或者直接打开数据库。
     */
    public void openDatabase() {
        if (dbInstance == null) {
            myDBHelper = new MyDBHelper(context, DB_NAME, VERSION);
            //如果DB_NAME数据库不存在，就先创建数据库，再获取可读可写的数据库对象；如果数据库存在，就直接打开数据库对象。
            dbInstance = myDBHelper.getWritableDatabase();
        }
    }

    /**
     * 往数据库里面的product表插入一条数据，若失败返回-1
     * @return 失败返回-1
     */
    public long insert(UserInfoEntity o) {
        ContentValues values = new ContentValues();
        values.put("login_account", o.getLogin_account());
        values.put("login_password", o.getLogin_password());
        values.put("page_id", o.getPage_id());
        return dbInstance.insert(DB_TABLE_NAME, null, values);
    }

    /**
     * 修改数据库中的一条数据，若失败返回-1
     * @return 失败返回-1
     */
    public void alter(String whichpage,String account,String password) {
//        dbInstance.execSQL("update "+DB_TABLE_NAME+" set "+value+"=? where page_id=?",new Object[]{key});
        ContentValues values = new ContentValues();
        values.put("login_account", account);
        values.put("login_password", password);
        dbInstance.update(DB_TABLE_NAME, values, "page_id=?", new String[]{whichpage});
    }
//    // 根据用户名，修改密码
//    public void dbUpdatePassword(String username, String newPassword) {
//        String sql = "update t_user set password=? where username=? and isDel=0";
//        Object bindArgs[] = new Object[] { newPassword, username };
//    }
    /**
     * 获得数据库中所有的用户，将每一个用户放到一个map中去，然后再将map放到list里面去返回
     * @return list
     */
    public HashMap<String, Object> getUserLoginInfo(String local_page_id) {
        Cursor cursor = null;
       HashMap<String, Object> map = new HashMap<>();
        String sql = "select * from "+ DB_TABLE_NAME+" where page_id=? ";
        String[] selectionArgs = new String[] { local_page_id };
        cursor = dbInstance.rawQuery(sql, selectionArgs);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                String remote_page_id = cursor.getString(cursor.getColumnIndex("page_id"));
                if (remote_page_id.equals(local_page_id)) {
                    map.put("login_account", cursor.getString(cursor.getColumnIndex("login_account")));
                    map.put("login_password", cursor.getString(cursor.getColumnIndex("login_password")));
                }
            }
        }
        return map;
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
            loginTableCreate = new StringBuffer();
            loginTableCreate.append("create table ").append(DB_TABLE_NAME)
                    .append(" (")
                    .append("_id integer primary key autoincrement,")
                    .append("page_id varchar(64),").append("login_time varchar(64),").append("login_account varchar(64),").append("login_password varchar(64)")
                    .append(")");
            db.execSQL(loginTableCreate.toString()); //执行SQL语句创建数据表
        }

        /**
         * 当数据库DB_NAME的版本号变大时，会调用此方法。版本号变小时不会调用此方法。
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
