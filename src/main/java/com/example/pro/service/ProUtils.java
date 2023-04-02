package com.example.pro.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class ProUtils {
    private static SimpleDateFormat SDF_U = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    private static SimpleDateFormat SDF_US = new SimpleDateFormat("dd MMM yy|HH:mm ", Locale.US);

    public static String parseDate(String orginDate) throws ParseException {
        return SDF_US.format(SDF_U.parse(orginDate));
    }

    public static Integer getHours(String date1, String date2) throws ParseException {
        ZonedDateTime parse = ZonedDateTime.parse(date1);
        ZonedDateTime parse2 = ZonedDateTime.parse(date2);
        Date d1 = Date.from(parse.toInstant());
        Date d2 = Date.from(parse2.toInstant());
        long dayM = 1000 * 24 * 60 * 60;
        long hourM = 1000 * 60 * 60;
        long differ = d2.getTime() - d1.getTime();
        long hour = differ % dayM / hourM;
        return Integer.parseInt(String.valueOf(hour));
    }
}
