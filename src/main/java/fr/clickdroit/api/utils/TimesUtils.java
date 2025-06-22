package fr.clickdroit.api.utils;

public class TimesUtils {
    public static String timeToStringAll(long seconde) {
        long d = seconde / 86400L;
        long h = seconde % 86400L / 3600L;
        long m = seconde % 3600L / 60L;
        long s = seconde % 60L;
        if (h < 1L && m < 1L && d < 1L)
            return s + "s";
        if (h < 1L && d < 1L)
            return m + "m " + s + "s";
        if (d < 1L)
            return h + "h " + m + "m " + s + "s";
        return d + "j " + h + "h " + m + "m " + s + "s";
    }
}
