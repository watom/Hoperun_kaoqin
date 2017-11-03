package com.watom999.www.hoperun.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @Description:工具类
 *
 *
 * @author <a href="mailto:snowpenglei@gmail.com">@author LP</a>
 *
 * @data 2013-9-21
 *
 *       version 1.0
 *
 *       Copyright sgcc
 */

public class Tools {

	/**
	 * @param view
	 * @param type
	 *            ==1,poi ;type==2,route
	 * @return
	 */
	public static Bitmap getViewBitmap(View view, int type) {
		Bitmap bitmap = null;
		view.setDrawingCacheEnabled(true);
		if (view != null && type == 1) {
			view.measure(MeasureSpec.makeMeasureSpec(350, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY));
		} else {
			view.measure(MeasureSpec.makeMeasureSpec(450, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(50, MeasureSpec.EXACTLY));
		}
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		bitmap = view.getDrawingCache();
		return bitmap;
	}

	/**
	 * 重启程序包
	 *
	 * @param context
	 */
	public static void toRestart(Context context) {
		Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}

	/**
	 * String电话号码省略处理
	 *
	 *
	 * @return
	 */
	public static String phoneNumOmit(String phoneNum) {
		if (phoneNum != null && phoneNum.length() == 11) {
			String omitNum = phoneNum.subSequence(0, 3) + "*****" + phoneNum.subSequence(8, 11);
			return omitNum;
		}
		return "";
	}

    //扫描指定文件
     public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
        }

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public static void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = null;
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
            mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        }
        State wifi = null;
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
            wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        }
        if (mobile != null && (mobile == State.CONNECTED || mobile == State.CONNECTING)) {
            isConnected = true;
        } else if (wifi != null && (wifi == State.CONNECTED || wifi == State.CONNECTING)) {
            isConnected = true;
        }
        return isConnected;
    }



	public static boolean isWiFi(Context context){
		boolean isWifi = false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = null;
		if(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!=null){
			wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		}
		if (wifi!=null&&(wifi == State.CONNECTED || wifi == State.CONNECTING)) {
			isWifi = true;
		}
		return isWifi;
	}

	/**
	 * @param error
	 * @return
	 */
	public static boolean WriteErroLog(String error, Context context) {
		boolean result = true;
		StringBuilder stringBuilder = new StringBuilder();
		if (context != null) {
			try {
				SharedPreferences preferences = context.getSharedPreferences("errolog", Context.MODE_PRIVATE + Context.MODE_APPEND);
				Editor editor = preferences.edit();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				String date = format.format(System.currentTimeMillis());
				stringBuilder.append(date).append(",").append(error);
				editor.putString("error", stringBuilder.toString());
				editor.putBoolean("isSended", false);
				editor.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}

		}
		return result;

	}

	/**
	 * 获取交易流水号
	 *
	 * @param context
	 * @return 交易流水号
	 *
	 */
	public static String getSerialNo(Context context) {
		String currentTimeStr = System.currentTimeMillis() + "";
		StringBuffer seriaNo = new StringBuffer(currentTimeStr.substring(currentTimeStr.length() - 7, currentTimeStr.length()));
		for (int i = 0; i < 8; i++) {
			int c = (int) (Math.random() * 10);
			seriaNo.append(c);
		}
		return seriaNo.toString();

	}

	public static String getImei(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = manager.getDeviceId();
		return imei;

	}

	/**
	 * 普通数字转化成代表钱的数字
	 *
	 * @param string
	 * @return
	 */

	public static String getMoneyType(String string) {

		String numString="0.00";
			try {
				if (!Tools.isEmpty(string) && string != null) {
				// 把string类型的货币转换为double类型。
				Double numDouble = Double.parseDouble(string);
				// 想要转换成指定国家的货币格式
				NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
				// 把转换后的货币String类型返回
				 numString = format.format(numDouble);
				numString = numString.replace("￥", "");
				if (numString.contains("(") && numString.contains(")")) {
					numString = numString.replace("(", "-");
					numString = numString.replace(")", "");
				}
				return numString;
				}else {
					return numString;
				}
			} catch (NumberFormatException e) {
				return "0.00";
//				return numString;
			}
	}

	/**
	 * 判断邮箱
	 */
	public static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}
	/**
	 * @return 当前时间
	 */
	public static String currentTimeMillis() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}
	public static String currentTimeMillis2() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 * 获取交易时间
	 *
	 * @return
	 */
	public static String getTransTime() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());// 时间改为24小时制
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * 减少一个月的时间
	 * @param time
	 * @return
	 * @throws Exception
     */
	public static String getMonthAgoOne(String time) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		// 设置日期为输入日期
		c.setTime(dateFormat.parse(time));
		// 前一天的日期
		c.add(Calendar.MONTH, -1);
		String lastTime = dateFormat.format(c.getTime());
		return lastTime;
	}

	/**
	 * 获取上一个月的时间
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static String getAddMonthAgoOne(String time) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 设置日期为输入日期
		c.setTime(dateFormat.parse(time));
		// 前一月
		c.add(Calendar.MONTH,1);
		String lastTime = dateFormat.format(c.getTime());
		return lastTime;
	}
	/**
	 * 获取某一年某一月有多少天
	 * @param year
	 * @param month
     * @return
     */
	public static int getMothEndDay(int year, int month){
		int day=30;
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
		    day=31;
		}
		if (month==2) {//二月
			if (year%400==0||(year%100!=0&&year%4==0)) {//闰年
					day =29;
			}else {//不是闰年
					day=28;
			}
		}
		if (month==4||month==6||month==9||month==11) {
				day=30;
		}

		return day;
	}

	public static String currentDataMoth() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * "yyyy-MM-dd" 格式化 "yyyy年MM月dd日"
	 * @param data
	 * @return
	 */
	public static String formatDataMoth(String data) {
		String formatTime = "";
		SimpleDateFormat raw = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Date parse=null;
        try {
            parse = raw.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String desiredDate = desiredFormat.format(parse);
        return desiredDate;
	}
	/**
	 * "yyyy-MM-dd" 格式化 "yyyy年MM月dd日"
	 * @param data
	 * @return
	 */
	public static String formatDataString(String data) {
		SimpleDateFormat raw = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Date parse=null;
        try {
            parse = raw.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String desiredDate = desiredFormat.format(parse);
        return desiredDate;
	}
	/**
	 * @return 当前日期
	 */
	public static String currentData() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}
	/**
	 * @return 当前日期
	 */
	public static String currentDataFormat() {
		long time = System.currentTimeMillis();
		String formatTime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * 获得某一天的前30天日期
	 *
	 *
	 *            ，例如：20101022
	 * @return String类型，
	 */
	public static String getlastMonthDay(String time){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 设置日期为输入日期
		try {
			c.setTime(dateFormat.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 前一天的日期
		c.add(Calendar.DAY_OF_MONTH, -30);
		String lastTime = dateFormat.format(c.getTime());
		return lastTime;
	}

	/**
	 * 获得某一年的前3年日期
	 *
	 *
	 *            ，例如：20101022
	 * @return String类型，
	 */
	public static String getThreeYearAgo(String time) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 设置日期为输入日期
		c.setTime(dateFormat.parse(time));

		// 前一天的日期
		c.add(Calendar.YEAR, -3);
		String lastTime = dateFormat.format(c.getTime());
		return lastTime;
	}

	/**
	 * 获取view的宽高
	 *
	 * @param view
	 * @return float[] index 0为宽 index 1为高
	 */
	public static int[] getViewWH(final View view) {
		final int[] hw = new int[2];
		view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				hw[0] = view.getWidth();
				hw[1] = view.getHeight();
				return false;
			}
		});
		return hw;
	}


	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {

		return str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0 || str.equals("");
	}

	/**
	 * 判断String类型是否为空
	 * @param str
	 * @return
     */
	public static String stringEmpty(String str){
		String s;
		if(isEmpty(str)) {
		    s="";
		}else {
			s=str;
		}
		return s;
	}

	public static int intEmpty(String in){
		int i;
		if(in==null) {
		    i=0;
		}else {
			i= Integer.parseInt(in);
		}
		return i;
	}
	public static double douEmpty(String in){
		double i;
		if(in==null) {
			i=0;
		}else {
			i= Double.parseDouble(in);
		}
		return i;
	}

	/**

	 * 限制输入特殊字符

	 */

//	public static String stringFilter(String str)throws PatternSyntaxException {
//		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		return m.replaceAll("");
//	}
//
    public static boolean stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`’~~!！@@##$%^&*()+=|{}'；:;·',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

	/**

	 * 判定输入汉字

	 */

	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

	}


	/**
	 * DecimalFormat转换最简便
	 */
	public static String formatDouble(double d) {
		DecimalFormat df   = new DecimalFormat("######0.00");
		return df.format(d);
	}

	/**

	 * 检测String是否全是中文

	 */

	public static String checkNameChese(String name) {
		String m=name;

//		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (!isChinese(cTemp[i])) {

				m=m.substring(0,i);

//				res = false;
				break;
			}else if(cTemp[i]=='！'||cTemp[i]=='，'||cTemp[i]=='?'||cTemp[i]=='。'||cTemp[i]=='!'||cTemp[i]==','||cTemp[i]=='？'||cTemp[i]=='.') {
				m=m.substring(0,i);
				break;
			}

		}

		return m;

	}


    /**
     * 检测String是否全是中文
     */

    public static boolean isAllChenese(String name) {
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
         if (cTemp[i] == '！' || cTemp[i] == '，' || cTemp[i] == '?' || cTemp[i] == '。' ||
                 cTemp[i] == '!' || cTemp[i] == ',' || cTemp[i] == '？' || cTemp[i] == '.'
            ||cTemp[i] == '~') {
            return false;
        }else if(stringFilter(name)){
             return false;
         }else if (!isChinese(cTemp[i])) {
                return false;
            }
        }
        return true;

    }


    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        String sysVersion = Build.VERSION.RELEASE;
        return sysVersion;

	}

	/**
	 * 手机型号
	 */
	public static String getPhoneType() {
		String phoneType = Build.BRAND + Build.MODEL.toString();
		return phoneType;
	}

	/**
	 * 格式化数据
	 *
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static String getFormatNum(String src, String pattern) {
		String result = "";
		DecimalFormat format = new DecimalFormat(pattern);
		if (!"".equals(src) && null != src) {
			result = format.format(Double.parseDouble(src));
		}
		return result;
	}

	/**
	 * 首页年电量电费保留到万元
	 */
	public static String getFormatNumNew(String src, String pattern) {
		String result = "";
		DecimalFormat format = new DecimalFormat(pattern);
		if (!"".equals(src) && null != src) {
			result = format.format(Double.parseDouble(src) / 10000);
		}
		return result;
	}

	/**
	 * 取两位小数
	 *
	 * @param str
	 * @return
	 */
	public static String MatchNum(String str) {
		Pattern pattern = Pattern.compile("^(-)?(([1-9]{1}[0-9]*)|([0]{1}))(.([0-9]){1,2})?$");
		Matcher matcher = pattern.matcher(str);
		if (str.length() > 0 && !matcher.matches() && str.contains(".")) {
			if (str.substring(str.indexOf("."), str.length()).length() > 3) {
				str = str.substring(0, str.indexOf(".")) + str.substring(str.indexOf("."), str.indexOf(".") + 3);
			}

		} else {

		}
		return str;
	}


	/**
	 * 首页金额显示格式为两位小数
	 *
	 * @param amount
	 * @return
	 */
	public static String amountFormat(String amount) {
		try {
			double money = Double.parseDouble(amount);
			DecimalFormat df = new DecimalFormat("#0.00");
			return df.format(money).toString();
		} catch (Exception e) {
			return amount;
		}
	}


	/**
	 *
	 *
	 *            -MM-dd HH:mm:ss
	 * @return yyyyMMdd
	 */
	public static String dateToSring(String strdate) {

		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat to = new SimpleDateFormat("yyyyMMdd");
		Date temp = null;
		try {
			temp = from.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return to.format(temp);

	}

	/**
	 *
	 *
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String sringToDate(String strdate) {

		try {
			SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");

			Date temp = null;

			temp = from.parse(strdate);

			return to.format(temp);
		} catch (Exception e) {
			// TODO: handle exception
			return "";// 如果异常返回空串

		}
	}
	public static String sringToDate2(String strdate) {

		try {
			SimpleDateFormat from = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat to = new SimpleDateFormat("yyyy-MM");

			Date temp = null;

			temp = from.parse(strdate);

			return to.format(temp);
		} catch (Exception e) {
			// TODO: handle exception
			return "";// 如果异常返回空串

		}
	}

	// 获取1-6随机数，并与入参不重复。
	public static int getfrom1to6random(int k) {
		int i = 0;
		if (k >= 1 && k <= 5) {
			i = (int) (Math.random() * 5) + 1;
			while (i != 0 && i == k) {
				i = (int) (Math.random() * 5) + 1;
			}

		}

		return i;
	}
	// 获取1-6随机数，并与入参不重复。
	public static int getfrom1to2random(int k) {
		int i = 0;
		if (k >= 1 && k <= 5) {
			i = (int) (Math.random() * 2) + 1;
			while (i != 0 ) {
				i = (int) (Math.random() * 2) + 1;
			}
		}
		return i;
	}

	// 金额元转换成分
	public static String YantoFen(String yuan) {
		String fen = "";
		try {
			double fen2 = Double.parseDouble(yuan) * 100;
			fen = String.format("%.2f", fen2);
		} catch (Exception e) {
			fen = yuan;
		}
		return fen;
	}

	public static String deciaml2(double data) {
		DecimalFormat df = new DecimalFormat("#.00");

		return df.format(data);
	}

	public static int deciaml(double data) {
		DecimalFormat df = new DecimalFormat("0");

		return Integer.parseInt(df.format(data));
	}


	/**
	 *
	 * @return yyyyMMddHHmmss
	 *
	 *            -MM-dd HH:mm:ss
	 */
	public static String formatStringToDeafault(String strdate) {

		SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date temp = null;
		try {
			temp = from.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return to.format(temp);

	}

	/**
	 * 解析商户ID（支付宝签名中的合作者ID）
	 */
	public static String[] paraserAlipayMerchantId(String sign) {
		String merchantId = "";
		String[] dataState = new String[3];
		// partner="2088311941409541"&seller_id="jxdl_nc@sina.com"&out_trade_no="901501061038450000011125"&subject="df"&body="dfjn"&total_fee="0.01"&notify_url="http://218.65.110.166:8101/Socket_Server/alipaynotifyorder"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="60m"&sign="FGPkDirP%2BZB4SPZdWNGUcOIrFktWY3y22b4JuuwQqprczIVqCDQRBEKZTfhzJFwSwr2%2B9F1bBtd53vX8YtTep6rp3imozDFreVLJSUJRwsT7uxZHjZTaF21kl8VuNkrred7%2Fn6a2HLCV4yN78jrr5Dk%2BCRLaCKUyD7B1SIAy7Yc%3D"&sign_type="RSA"
		try {
			String[] arrayStr = sign.split("&");
			// String indexStr = "";
			// String indexStr1 = "";
			// for(int i = 0;i<arrayStr.length;i++){
			// if(arrayStr[i].startsWith("partner")){
			// indexStr = arrayStr[i];
			// }
			// if(arrayStr[i].startsWith("partner")){
			// indexStr = arrayStr[i];
			// }
			// }
			String[] transNoArray = arrayStr[0].split("=");
			String[] transNoArray1 = arrayStr[2].split("=");
			String[] transNoArray2 = arrayStr[5].split("=");
			dataState[0] = transNoArray[1].replace("\"", "").trim();
			dataState[1] = transNoArray1[1].replace("\"", "").trim();
			dataState[2] = transNoArray2[1].replace("\"", "").trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataState;
	}


	/**
	 * CID验证，正常情况下CID由数组字母组成
	 * @param cid
	 * @return
	 */
	public static boolean CidVerification(String cid) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(cid);
		return m.find();
	}
	/**
	 * 客户名改*号
	 *
	 * @param tring
	 */
	public static String getXingName(String tring) {
		int num = 0;
		String xing = "",a="",b="";
		if(null ==tring){
			return "";
		}
		tring = tring.trim();
		if (tring.length() > 5) {
			a = tring.substring(0, 2);
			b = tring.substring(6);
			tring = a + "****" + b;
		} else {
			if (tring.length() % 3 == 0) {
				num = tring.length() / 3;
			} else {
				num = tring.length() / 3 + 1;
			}
			for (int i = 0; i < num; i++) {
				xing = xing + "*";
			}
			tring = tring.substring(num, tring.length());
			tring = xing + tring;

		}
		return tring;

	}

	/**
	 * 客户地址中的数字改*号
	 *
	 * @param
	 */
	public static String getXingAddress(String userMarketAddres) {
		String c = "",a="",b="";
		if(userMarketAddres==null){
		return userMarketAddres;
		}
		int num;
		if (userMarketAddres.length() > 12) {
			num= userMarketAddres.length();
			a = userMarketAddres.substring(0, num-12);
			b = userMarketAddres.substring(num-6);
			userMarketAddres = a + "******" + b;
		} else {
			if (userMarketAddres.length() % 3 == 0) {
				num = userMarketAddres.length() / 3;
			} else {
				num = userMarketAddres.length() / 3 + 1;
			}
			for (int i = 0; i < num; i++) {
				c = c + "*";
			}
			userMarketAddres = userMarketAddres.substring(num,
					userMarketAddres.length());
			userMarketAddres = c + userMarketAddres;

		}
		return userMarketAddres;

	}


	/**
	 * 电话号码中的数字改*号
	 *
	 * @param phone
	 */
	public static String getXingPhone(String phone) {
		String c = "", a, b;
		a = phone.substring(0, 3);
		b = phone.substring(7);
		phone = a + "*****" + b;

		return phone;
	}
	/**
	 * CID验证，正常情况下CID由数组字母组成
	 * @param type
	 * @return
	 */
	public static boolean CidVerificationdata(String type) {
		String regEx = "[0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(type);
		return m.find();
	}


	/**
	 * 两个时间字符相差的天数
	 * @param
	 * @return
	 */
	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}


	/**
	 *
	 */

	public static String cityCodeToCityName(int dpCode) {
		String name = "";
		switch (dpCode) {
			case 110100:// 北京
				name = "BEIJING";
				break;
			case 120100:// 天津
				name = "TIANJING";
				break;
			case 310100://上海
			    name = "SHANGHAI";
			    break;
			case 140000://山西
				name = "SHANXI";
				break;
			case 150000://内蒙古
			    name = "MENGDONG";
			    break;
			case 220000://吉林
			    name = "JILIN";
			    break;
			case 230000://黑龙江
			    name = "HEILONGJIANG";
			    break;
			case 340000://安徽
			    name = "ANHUI";
			    break;
			case 350000://福建
			    name = "FUJIAN";
			    break;
			case 410000://河南
				name = "HENAN";
			    break;
			case 420000://湖北
			    name = "HUBEI";
			    break;
			case 540000://西藏
			    name = "XIZANG";

			    break;
			case 620000://甘肃
			    name = "GANSU";
			    break;
			case 630000://青海
			    name = "QINGHAI";
			    break;
			case 640000://宁夏
			    name = "NINGXIA";
			    break;
			case 650000://新疆
			    name = "XINJIANG";
			    break;
			case 210000:// 辽宁
				name = "LIAONING";
				break;
			case 320000:// 江苏
				name = "JIANGSU";
				break;
			case 330000:// 浙江
			name = "ZHEJIANG";
				break;
			case 610000:// 陕西
			name = "SHANXI";
				break;
			case 430000:// 湖南
			name = "HUNAN";
				break;
			case 370000:// 山东
				name = "SHANDONG";
				break;
			case 360000:// 江西
				name = "JIANGXI";
				break;
			case 500100:// 重庆
				name = "CHONGQING";
				break;
			case 510000:// 四川
				name = "SICHUAN";
				break;
			default:// 默认
				name = "ZANWU";
				break;
		}
		return name;
	}

	/**
	 * 获取图片大小
	 *
	 * @param size
	 * @return
	 */
	public static String getReadableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, 2));
	}

	/**
	 * 存储缩放的图片
	 * <p>
	 * 图片数据
	 */
	public static String saveScalePhoto(String filePath, Bitmap bitmap, int maxSize) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			//微调一下，保证尽可能的接近要求的尺寸
			if (maxSize == 150) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 94, fos);
			} else if (maxSize == 100) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 96, fos);
			}

			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError err) {
			return null ;
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 检测只能输入中文
	 * @param string
	 * @return
	 */
	/*
	*
	* //        String REGEX = "^[\\u4E00-\\u9FFF]+$";
        String REGEX = "/[^\\u4E00-\\u9FA5]/g\n";
        boolean matches = Pattern.matches(REGEX, s);
        Logout.e("ycw","matches ===="+matches);
//        StringBuffer unicode = new StringBuffer();
//
//        for (int i = 0; i < s.length(); i++) {
//            // 取出每一个字符
//            char c = s.charAt(i);
//            // 转换为unicode
//            unicode.append("\\u" + Integer.toHexString(c));
//        }
//
//        String s1 = unicode.toString();
//        Logout.e("ycw","unicode==="+s1);

	* */
	public static boolean isInputCh(String string) {
		String REGEX = "^[\\u4E00-\\u9FFF]+$";
		return Pattern.matches(REGEX, string);
	}

	/*

	获取指定时间
	*/

	public static String getAppointTime(String appointTime , int yearTime, int monthTime, int dayTime){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			Date parse = format.parse(appointTime);
			Calendar c = Calendar.getInstance();
			c.setTime(parse);
			c.add(Calendar.YEAR,yearTime);
			c.add(Calendar.MONTH,monthTime);
			c.add(Calendar.DATE,dayTime);
			appointTime = format.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return appointTime;
	}

	/**
	 * 计算两个时间段的多少天
	 */


	public static long get2DateDifference(String startTime, String endTime){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			Date startParse = format.parse(startTime);
			Date endParse = format.parse(endTime);

			long diff = endParse.getTime() - startParse.getTime();
			return diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static void setStatus(String status, TextView tv_statue) {
		if ("01".equals(status)) {
			tv_statue.setText("运行");
		} else if ("02".equals(status)) {
			tv_statue.setText("停用");
		} else if ("03".equals(status)) {
			tv_statue.setText("拆除");
		} else if ("04".equals(status)) {
			tv_statue.setText("未投运");
		}else {
			tv_statue.setText("运行");
		}
	}
}