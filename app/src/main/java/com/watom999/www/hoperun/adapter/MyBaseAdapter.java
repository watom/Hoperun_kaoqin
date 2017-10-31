package com.watom999.www.hoperun.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/***********************************************
 *@Copyright: 2017(C), 国电通__期
 *@Author&Email: wanghaitao 1164973719@qq.com
 *@FileName: com.watom999.www.hoperun.Adapter
 *@Function: 1、
 *@Description: 1、       
 *@CreatedDate: 2017/10/30 11:04
 *@UpDate: 1、
 ***********************************************/

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private List<T> dataList = new ArrayList<T>();
    public LayoutInflater mLayoutInflater;

    public MyBaseAdapter(Activity context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    protected Activity context;

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int i) {
        return !(getCount()<=0||getCount()>=i)?dataList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getViewCustom(i,  view,  viewGroup);
    }

    public abstract View getViewCustom(int i, View view, ViewGroup viewGroup);
}
