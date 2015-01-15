package org.ado.biblio.desktop.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

    private static final DateFormat SQLITE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat SQLITE_DATE_TIME_FORMAT_GERMAN = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final DateFormat SQLITE_DATE_FORMAT_GERMAN = new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat SQLITE_DATE_TIME_FORMAT_SPAIN = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final DateFormat SQLITE_DATE_FORMAT_SPAIN = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat SQLITE_DATE_TIME_FORMAT_UK = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final DateFormat SQLITE_DATE_FORMAT_UK = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private static final Map<String, DateFormatGroup> DATE_FORMAT_LOCALE = initDateFormat();

    private static Map<String, DateFormatGroup> initDateFormat() {
        final HashMap<String, DateFormatGroup> map = new HashMap<>();
        map.put(Locale.GERMAN.getLanguage(), new DateFormatGroup(SQLITE_DATE_TIME_FORMAT_GERMAN, SQLITE_DATE_FORMAT_GERMAN));
        map.put("es", new DateFormatGroup(SQLITE_DATE_TIME_FORMAT_SPAIN, SQLITE_DATE_FORMAT_SPAIN));
        map.put(Locale.ENGLISH.getLanguage(), new DateFormatGroup(SQLITE_DATE_TIME_FORMAT_UK, SQLITE_DATE_FORMAT_UK));
        return map;
    }

    public static String getDateFormat() {
        return getDateFormat(DateTypeEnum.DATE_TIME);
    }

    public static String getDateFormat(DateTypeEnum dateType) {
        final SimpleDateFormat dateFormat = (SimpleDateFormat) DATE_FORMAT_LOCALE.get(Locale.getDefault().getLanguage()).getDateFormat(dateType);
        return dateFormat.toPattern();
    }

    public static Date parse(String stringDate) {
        return parse(stringDate, DateTypeEnum.DATE_TIME);
    }

    public static Date parse(String stringDate, DateTypeEnum dateType) {
        final DateFormat dateFormat = DATE_FORMAT_LOCALE.get(Locale.getDefault().getLanguage()).getDateFormat(dateType);
        try {
            return dateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new IllegalStateException(String.format("Cannot parse \"%s\" to date", stringDate), e);
        }
    }

    public static Date parseSqlite(String stringDate) {
        try {
            return SQLITE_DATE_FORMAT.parse(stringDate);
        } catch (ParseException e) {
            throw new IllegalStateException(String.format("Cannot parse \"%s\" to date", stringDate), e);
        }
    }

    public static String formatSqlite(Date date) {
        return SQLITE_DATE_FORMAT.format(date);
    }

    public static String format(Date date) {
        return format(date, DateTypeEnum.DATE_TIME);
    }

    public static String format(Date date, DateTypeEnum dateType) {
        final DateFormat dateFormat = DATE_FORMAT_LOCALE.get(Locale.getDefault().getLanguage()).getDateFormat(dateType);
        if (dateFormat == null) {
            return SQLITE_DATE_FORMAT.format(date);
        }
        return dateFormat.format(date);
    }

    public enum DateTypeEnum {
        DATE_TIME, DATE
    }

    static class DateFormatGroup {
        private DateFormat dateTime;
        private DateFormat date;

        public DateFormatGroup(DateFormat dateTime, DateFormat date) {
            this.dateTime = dateTime;
            this.date = date;
        }

        public DateFormat getDateFormat(DateTypeEnum dateType) {
            switch (dateType) {
                case DATE_TIME:
                    return dateTime;
                case DATE:
                    return date;
                default:
                    throw new IllegalStateException(String.format("No date format found for \"%s\".", dateType));
            }
        }
    }
}