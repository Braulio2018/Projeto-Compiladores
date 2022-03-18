package com.gutoveronezi.compiler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    
    private static final String DEFAULT_PATTERN = "YYYY-MM-dd HH:mm:ss";

    public static String getDateFormated(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern == null ? DEFAULT_PATTERN : pattern);
        return sdf.format(date == null ? new Date() : date);
    }

    public static String getDateFormated() {
        return getDateFormated(null, null);
    }
}
