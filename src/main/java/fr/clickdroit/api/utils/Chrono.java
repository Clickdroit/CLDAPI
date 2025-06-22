package fr.clickdroit.api.utils;

public class Chrono {
    public static int[] timeToHMS(long tempsS) {
        int h = (int)(tempsS / 3600L);
        int m = (int)(tempsS % 3600L / 60L);
        int s = (int)(tempsS % 60L);
        int[] i = { h, m, s };
        return i;
    }

    public static String timeToString(long tempsS) {
        int[] i = timeToHMS(tempsS);
        int h = i[0];
        int m = i[1];
        int s = i[2];
        StringBuilder r = new StringBuilder();
        if (h > 0)
            r.append(h + "h ");
        if (m > 0)
            r.append(m + "min ");
        if (s > 0)
            r.append(s + " s");
        if (h <= 0 && m <= 0 && s <= 0) {
            r = new StringBuilder();
            r.append("0 s");
        }
        return r.toString();
    }

    public static String timeToDigitalString(long tempsS) {
        int[] i = timeToHMS(tempsS);
        int h = i[0];
        int m = i[1];
        int s = i[2];
        StringBuilder r = new StringBuilder();
        if (h > 0)
            r.append(h + ":");
        if (tempsS < 0L)
            return "00h00";
        r.append(((m > 9) ? (String)Integer.valueOf(m) : ("0" + m)) + ":");
        r.append((s > 9) ? Integer.valueOf(s) : ("0" + s));
        return r.toString();
    }

    public static int getCycleDurationTime(long value) {
        return (int)Math.floor(value / 60.0D);
    }
}
