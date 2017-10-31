package com.watom999.www.hoperun.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    private static List<SimpleDateFormat> formatList;
    private static SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat bejingFormat = new SimpleDateFormat("yyyyMMdd");

    static {
        formatList = new ArrayList<SimpleDateFormat>();
        formatList.add(defaultFormat);
        formatList.add(bejingFormat);
    }

    public static Date getDate(String string) {
        ParsePosition position = new ParsePosition(0);
        Date date = null;
        for (SimpleDateFormat format : formatList) {
            date = format.parse(string, position);
            if (date != null) {
                break;
            }
        }
        return date;
    }

    public static String format(Date date) {
        return defaultFormat.format(date);
    }

    public static String getWeekByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK);

        switch (weekIndex) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;
    }

}
