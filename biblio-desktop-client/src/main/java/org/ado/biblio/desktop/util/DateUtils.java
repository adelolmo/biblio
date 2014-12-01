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
    private static final DateFormat SQLITE_DATE_FORMAT_GERMAN = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final DateFormat SQLITE_DATE_FORMAT_SPAIN = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final DateFormat SQLITE_DATE_FORMAT_UK = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private static final Map<String, DateFormat> DATE_FORMAT_LOCALE = initDateFormat();

    private static Map<String, DateFormat> initDateFormat() {
        final HashMap<String, DateFormat> map = new HashMap<>();
        map.put(Locale.GERMAN.getLanguage(), SQLITE_DATE_FORMAT_GERMAN);
        map.put("es", SQLITE_DATE_FORMAT_SPAIN);
        map.put(Locale.ENGLISH.getLanguage(), SQLITE_DATE_FORMAT_UK);
        return map;
    }

    public static Date parseSqlite(String stringDate) {
        try {
            return SQLITE_DATE_FORMAT.parse(stringDate);
        } catch (ParseException e) {
            throw new IllegalStateException(String.format("Cannot parse \"%s\" to date", stringDate), e);
        }
    }

    public static String format(Date date) {
        final DateFormat dateFormat = DATE_FORMAT_LOCALE.get(Locale.getDefault().getLanguage());
        if (dateFormat == null) {
            return SQLITE_DATE_FORMAT.format(date);
        }
        return dateFormat.format(date);
    }
}