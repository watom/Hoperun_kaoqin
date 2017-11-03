package com.watom999.www.hoperun.utils;


import android.util.Log;

import com.watom999.www.hoperun.controlSwitch.Config;
public class Logout {
	private static String tag="wanghaitao";

	/**
	 * log  info
	 */
	public static void i( Object msg) {
		if (Config.isDebug) {
			Log.i(tag, "msg:  |---" + msg+" ---|" );
		}
	}
	/**
	 * log  debug
	 */
	public static void d( Object msg) {
		if (Config.isDebug)
			Log.d(tag, "msg:  |---" + msg+" ---|" );
	}

	/**
	 * log  error
	 */
	public static void e( Object msg) {
		if (Config.isDebug)
			Log.e(tag, "msg:  |---" + msg+" ---|" );
	}

	/**
	 * log  v
	 */
	public static void v( Object msg) {
		if (Config.isDebug)
			Log.v(tag, "msg:  |---" + msg+" ---|" );
	}
}
