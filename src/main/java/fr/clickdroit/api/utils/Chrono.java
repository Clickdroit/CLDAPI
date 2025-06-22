package fr.clickdroit.api.utils;

public class Chrono {
    public static int[] timeToHMS(long tempsS) {
        int h = (int)(tempsS / 3600L);
        int m = (int)(tempsS % 3600L / 60L);
        int s = (int)(tempsS % 60L);
        return new int[] { h, m, s };
    }

    public static String timeToString(long tempsS) {
        int[] i = timeToHMS(tempsS);
        int h = i[0];
        int m = i[1];
        int s = i[2];

        StringBuilder r = new StringBuilder();
        if (h > 0)
            r.append(h).append("h ");
        if (m > 0)
            r.append(m).append("min ");
        if (s > 0)
            r.append(s).append("s");

        if (h <= 0 && m <= 0 && s <= 0) {
            r = new StringBuilder();
            r.append("0s");
        }
        return r.toString();
    }

    public static String timeToDigitalString(long tempsS) {
        // Vérification pour temps négatif
        if (tempsS < 0L)
            return "00:00";

        int[] i = timeToHMS(tempsS);
        int h = i[0];
        int m = i[1];
        int s = i[2];

        StringBuilder r = new StringBuilder();

        // Si il y a des heures, les afficher
        if (h > 0) {
            r.append(String.format("%02d", h)).append(":");
        }

        // Minutes et secondes (toujours affichées)
        r.append(String.format("%02d", m)).append(":");
        r.append(String.format("%02d", s));

        return r.toString();
    }

    public static int getCycleDurationTime(long value) {
        return (int) Math.floor(value / 60.0D);
    }
    public static long minutesToSeconds(int minutes) {
        return minutes * 60L;
    }
    public static long hoursToSeconds(int hours) {
        return hours * 3600L;
    }
    public static String timeToShortString(long tempsS) {
        if (tempsS < 0L) return "00:00";

        int m = (int)(tempsS / 60L);
        int s = (int)(tempsS % 60L);

        return String.format("%02d:%02d", m, s);
    }
}
