package com.cgstate.boxmobile.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/2/8.
 */

public class TimeFormatUtil {
    //opttime=2016/11/25 10:14:52


    public static String format(String oldStr) {
        oldStr = oldStr.substring(0, oldStr.indexOf(" "));
        return oldStr;
    }


    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
    static Date date = new Date();

    private static String today = getTodday();
    private static String yesterdy = getYesterday();

    /**
     * 0今天,1昨天,-1较早
     *
     * @param opttime
     * @return
     */

    public static int showWhichDay(String opttime) {
        opttime = opttime.substring(0, opttime.indexOf(" "));

        if (opttime.equals(today)) {
            return 0;
        } else if (opttime.equals(yesterdy)) {
            return 1;
        } else {
            return 2;
        }
    }


    private static String getTodday() {
        return formatter.format(date);
    }

    private static String getYesterday() {
        Calendar yesterday = new GregorianCalendar();
        yesterday.setTime(date);
        yesterday.add(Calendar.DATE, -1);//昨天
        return formatter.format(yesterday.getTime());
    }

}
