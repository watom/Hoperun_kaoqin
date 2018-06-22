package com.watom999.www.hoperun.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**********************************************************************************
 *@Copyright (C), 2011-2013, 北京国电通网络技术有限公司. 
 *@FileName:     com.sgcc.hcs.MyToast.java
 *@Author:　   　　　    ning.zhou
 *@Version :     V1.0
 *@Date:         2014-3-27 上午10:57:19
 *@Description:  作用　 弹出Toast
 **********************************************************************************
 */
public class MyToast extends Toast {

	static Toast toast;
	public MyToast(Context context) {
		super(context);
	}
	/**
	 * 显示操作失败的提示
	 * @param mContext 上下文
	 * @param result 结果
	 */
	public static void showFailToast(Context mContext, int result){
		toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		if (result == -3) {
			toast.setText("网络连接失败，请重试");
		} else if (result == -2) {
			toast.setText("访问超时，请重试");
		} else {
		//	toast.setText(R.string.common_operatefail);
		}
		toast.show();
	}
	/**
	 * 显示提示信息——通过id
	 * @param mContext
	 * @param id 字符串id
	 */
	public static void showToast(Context mContext, int id) {
		toast = Toast.makeText(mContext, id, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setText(id);
		toast.show();
	}
	/**
	 * 显示提示信息——通过字符串
	 * @param mContext
	 * @param content 字符串
	 */
	public static void showToast(Context mContext, String content) {
//		if (toast==null) {
//			
//
		/**-----------------解决返回为空----------**/
		if (null!=content&&!"".equals(content)) {
			toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setText(content);
			toast.show();
		}else {

		}
		/**-----------------2014/07/22----------**/
	}
	

}
