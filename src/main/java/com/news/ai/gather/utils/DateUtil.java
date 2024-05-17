package com.news.ai.gather.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Created by zhiwei on 2022/3/13.
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String ALL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //Mon May 13 19:59:32 +0000 2024
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy");

    public static long conversionDate2Long(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return System.currentTimeMillis();
        }
        ZonedDateTime dateTime = ZonedDateTime.parse(dateString, formatter);
        return dateTime.toInstant().toEpochMilli();
    }

    public static LocalDateTime conversionDate2LocalDateTime(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return LocalDateTime.now();
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }


}
