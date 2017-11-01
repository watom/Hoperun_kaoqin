package com.watom999.www.hoperun.data;

import android.content.Context;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun.data
 *@Function: 1、
 *@Description: 1、       
 *@CreatedDate: 2017/11/1 19:17
 *@UpDate: 1、
 ***********************************************/

public class DataBaseHelp{
    private DBOpenHelper mDbOpenHelper;
    private Context context;
    private int mDbVersion;
    private String mTablesName;
/**
 *   http://www.cnblogs.com/yangqiangyu/p/5530440.html
 *http://blog.csdn.net/liuhe688/article/details/6715983
 *
 *http://blog.csdn.net/qq_27280457/article/details/51790055
 *http://yzxqml.iteye.com/blog/1717135
 */

    public DataBaseHelp(Context context, String mTablesName, int mDbVersion) {
        this.mDbOpenHelper = new DBOpenHelper(context,mTablesName,null,mDbVersion);
        this.context = context;
        this.mDbVersion = mDbVersion;
        this.mTablesName = mTablesName;
    }
}
