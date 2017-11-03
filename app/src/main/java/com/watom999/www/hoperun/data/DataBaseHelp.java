package com.watom999.www.hoperun.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.watom999.www.hoperun.utils.Logout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun.data
 *@Function: 1、
 *@Description: 1、       
 *@CreatedDate: 2017/11/1 19:17
 *@UpDate: 1、
 ***********************************************/

public class DataBaseHelp extends DBOpenHelper{
    private DBOpenHelper mDBOpenHelper;
    private SQLiteDatabase writableDatabase;
    private Context context;
    private int mDBVersion;
    private String mDBname;
    private String[] creatTableSQL;
    private String[] updateTableSQL;



    public void execSQL(String sql){
        this.writableDatabase.execSQL(sql);
    }

    public void execSQL(String sql,Object[] params){
        this.writableDatabase.execSQL(sql,params);
    }

    @Override
    protected int getDBVersion(Context context) {
        return 0;
    }

    @Override
    protected String getDBName(Context context) {
        return null;
    }

    @Override
    protected String[] getCreatTableSQL(Context context) {
        return new String[0];
    }

    @Override
    protected String[] getUpdateTableSQL(Context context) {
        return new String[0];
    }

    public DataBaseHelp(Context context) {
        super(context);
        this.context = context;
        this.mDBVersion = getDBVersion(context);
        this.mDBname = getDBName(context);
        this.creatTableSQL=getCreatTableSQL(context);
        this.updateTableSQL=getUpdateTableSQL(context);
        writableDatabase = mDBOpenHelper.getWritableDatabase();
    }

    protected void openDB(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                writableDatabase = mDBOpenHelper.getWritableDatabase();
            }
        }).start();
    }

    public void closeDB(){
        this.writableDatabase.close();
        this.mDBOpenHelper.close();
    }



    /**
     * 用ContentValues存放数据，不用关注放入类型
     */
    private void ContentValuesPut(ContentValues contentValues, String key, Object value){
        if (value==null){
            contentValues.put(key,"");
        }else{
            String className = value.getClass().getName();
            if (className.equals("java.lang.String")){
                contentValues.put(key,value.toString());
            } else if (className.equals("java.lang.Integer")){
                contentValues.put(key,Integer.valueOf(value.toString()));
            } else if (className.equals("java.lang.Float")){
                contentValues.put(key,Float.valueOf(value.toString()));
            } else if (className.equals("java.lang.Double")){
                contentValues.put(key,Double.valueOf(value.toString()));
            } else if (className.equals("java.lang.Boolean")){
                contentValues.put(key,Boolean.valueOf(value.toString()));
            } else if (className.equals("java.lang.Long")){
                contentValues.put(key,Long.valueOf(value.toString()));
            } else if (className.equals("java.lang.Short")){
                contentValues.put(key,Short.valueOf(value.toString()));
            }
        }
    }

    /**
     * 根据数组的列和值进行insert
     * @param tableName 需要插入数据的数据表名称
     * @param columns 列标题：要插入表格中的数组，如【姓名|性别|年龄。。。】
     * @param values  列数据：要插入表格中的数组，如【张三|男|23.。。】
     * @return 返回插入结果
     */
    public boolean insert(String tableName,String[] columns,Object[] values){
        ContentValues contentValues = new ContentValues();
        for (int rows = 0; rows < columns.length;++rows){
            ContentValuesPut(contentValues,columns[rows],values[rows]);
        }
        long rowId = this.writableDatabase.insert(tableName,null,contentValues);
        Logout.d("数据库插入结果："+rowId+"   -1失败");
        return rowId!=-1;
    }

    /**
     * 根据map来进行insert
     */
    public boolean insert(String tableName,Map<String,Object> columnValues){
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            this.ContentValuesPut(contentValues,key,columnValues.get(key));
        }
        long rowId = this.writableDatabase.insert(tableName,null,contentValues);
        Logout.d("数据库插入结果："+rowId+"   -1失败");
        return rowId!=-1;
    }


    /**
     * 统一对数组where条件进行拼接
     * @param whereColumns
     * @return
     */
    private String initWhereSqlFromArray(String[] whereColumns){
        StringBuffer whereStr = new StringBuffer();
        for (int i=0;i<whereColumns.length;++i){
            whereStr.append(whereColumns[i]).append(" = ? ");
            if (i<whereColumns.length-1){
                whereStr.append(" and ");
            }
        }
        return whereStr.toString();
    }

    /**
     * 统一对map的where条件和值进行处理
     * @param whereParams
     * @return
     */
    private Map<String,Object> initWhereSqlFromMap(Map<String,String> whereParams){
        Set set = whereParams.keySet();
        String[] temp = new String[whereParams.size()];
        int i = 0;
        Iterator iterator = set.iterator();
        StringBuffer whereStr = new StringBuffer();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            whereStr.append(key).append(" = ? ");
            temp[i] = whereParams.get(key);
            if (i<set.size()-1){
                whereStr.append(" and ");
            }
            i++;
        }
        HashMap result = new HashMap();
        result.put("whereSql",whereStr);
        result.put("whereSqlParam",temp);
        return result;
    }

    /**
     * 根据数组条件来update
     * @param tableName
     * @param columns
     * @param values
     * @param whereColumns
     * @param whereArgs
     * @return
     */
    public boolean update(String tableName,String[] columns,Object[] values,String[] whereColumns,String[] whereArgs){
        ContentValues contentValues = new ContentValues();
        for (int i=0;i<columns.length;++i){
            this.ContentValuesPut(contentValues,columns[i],values[i]);
        }
        String whereClause = this.initWhereSqlFromArray(whereColumns);
        int rowNumber = this.writableDatabase.update(tableName,contentValues,whereClause,whereArgs);
        return rowNumber > 0 ;
    }

    /**
     * 根据map值来进行update
     * @param tableName
     * @param columnValues
     * @param whereParam
     * @return
     */
    public boolean update(String tableName,Map<String,Object> columnValues,Map<String,String> whereParam){
        ContentValues contentValues = new ContentValues();
        Iterator iterator = columnValues.keySet().iterator();

        String columns;
        while (iterator.hasNext()){
            columns = (String) iterator.next();
            ContentValuesPut(contentValues,columns,columnValues.get(columns));
        }

        Map map = this.initWhereSqlFromMap(whereParam);
        int rowNumber = this.writableDatabase.update(tableName,contentValues,(String)map.get("whereSql"),(String[]) map.get("whereSqlParam"));
        return rowNumber > 0;
    }

    /**
     * 根据数组条件进行delete
     * @param tableName
     * @param whereColumns
     * @param whereParam
     * @return
     */
    public boolean delete(String tableName,String[] whereColumns,String[] whereParam){
        String whereStr = this.initWhereSqlFromArray(whereColumns);
        int rowNumber = this.writableDatabase.delete(tableName,whereStr,whereParam);
        return rowNumber > 0;
    }


    /**
     * 根据map来进行delete
     * @param tableName
     * @param whereParams
     * @return
     */
    public boolean delete(String tableName,Map<String,String> whereParams){
        Map map = this.initWhereSqlFromMap(whereParams);
        int rowNumber = this.writableDatabase.delete(tableName,map.get("whereSql").toString(),(String[]) map.get("whereSqlParam"));
        return rowNumber > 0;
    }


    /**
     * 封装了rawQuery()方法
     * rawQuery()方法的第一个参数为select语句；第二个参数为select语句中占位符参数的值，如果select语句没有使用占位符，该参数可以设置为null。
     * Cursor cursor = db.rawQuery("select * from  where name like ? and age=?", new String[]{"xxx", "4"});
     * @param rawQuerySQL rawQuery()的第一个参数为select语句
     * @param params  rawQuery()的select语句中占位符参数的值
     * @return  查询返回List<Map>集合
     */
    public List<Map> queryListMap(String rawQuerySQL, String[] params){
        ArrayList list = new ArrayList();
        Cursor cursor = this.writableDatabase.rawQuery(rawQuerySQL,params);
        int columnCount = cursor.getColumnCount();
        while (cursor.moveToNext()){
            HashMap item = new HashMap();
            for (int i=0;i<columnCount;++i){
                int type = cursor.getType(i);
                switch (type){
                    case 0:
                        item.put(cursor.getColumnName(i),null);
                        break;
                    case 1:
                        item.put(cursor.getColumnName(i), cursor.getInt(i));
                        break;
                    case 2:
                        item.put(cursor.getColumnName(i),cursor.getFloat(i));
                        break;
                    case 3:
                        item.put(cursor.getColumnName(i),cursor.getString(i));
                        break;
                }
            }
            list.add(item);
        }
        cursor.close();
        return list;
    }

    /**
     * 查询单条数据返回map
     * @param sql
     * @param params
     * @return
     */
    public Map queryItemMap(String sql,String[] params){
        Cursor cursor = this.writableDatabase.rawQuery(sql,params);
        HashMap map = new HashMap();
        if (cursor.moveToNext()){
            for (int i = 0;i < cursor.getColumnCount();++i){
                int type = cursor.getType(i);
                switch (type){
                    case 0:
                        map.put(cursor.getColumnName(i),null);
                        break;
                    case 1:
                        map.put(cursor.getColumnName(i),cursor.getInt(i));
                        break;
                    case 2:
                        map.put(cursor.getColumnName(i),cursor.getFloat(i));
                        break;
                    case 3:
                        map.put(cursor.getColumnName(i),cursor.getString(i));
                        break;
                }
            }
        }
        cursor.close();
        return map;
    }


}
