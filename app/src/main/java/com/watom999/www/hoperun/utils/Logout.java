package com.watom999.www.hoperun.utils;


import android.util.Log;

import com.watom999.www.hoperun.controlSwitch.Config;
public class Logout {

	/**
	 * log  info
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (Config.isDebug)
			Log.i(tag, "msg: " + msg);
	}
	/**
	 * log  debug
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (Config.isDebug)
			// Log.d(tag, msg);
			Log.d(tag, "msg: " + msg);
	}

	/**
	 * log  error
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (Config.isDebug)
			Log.e(tag, "msg: " + msg);
	}

	/**
	 * log  v
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, Object msg) {
		if (Config.isDebug)
			Log.v(tag, "msg: " + msg);
	}



}
