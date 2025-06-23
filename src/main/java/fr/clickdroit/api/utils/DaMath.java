package fr.clickdroit.api.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Random;

public class DaMath {
    public static Random random = new Random();

    public static Location getLocationWithYaw(Location loc, int multiply, double y) {
        Location finalloc = new Location(loc.getWorld(), loc.getX(), y, loc.getZ());
        loc.setYaw(loc.getYaw() + 90.0F);
        finalloc.add(multiply * Math.sin(loc.getYaw() * Math.PI / 360.0D), 0.0D, multiply * Math.cos(loc.getYaw() * Math.PI / 360.0D));
        return finalloc;
    }

    public static String getArrow(Location from, Location to) {
        if (from == null || to == null)
            return "?";
        if (!from.getWorld().getName().equals(to.getWorld().getName()))
            return "?";
        from.setY(0.0D);
        to.setY(0.0D);
        String[] arrows = { "⬆", "⬈", "⮕", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
                Vector d = from.getDirection();
        Vector v = to.subtract(from).toVector().normalize();
        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
        a = ((int)(a + 22.5D) % 360);
        if (a < 0.0D)
            a += 360.0D;
        return String.valueOf(arrows[(int)a / 45]);
  }

        public static String getYArrow(Location from, Location to) {
            if (from == null || to == null)
                return "?";
            if (!from.getWorld().getName().equals(to.getWorld().getName()))
                return "?";
            int yFrom = (int)from.getY(), yTo = (int)to.getY();
            if (yFrom < yTo)
                return "§c§l⬆";
            if (yFrom > yTo)
                return "§c§l⬇";
            if (yFrom == yTo)
                return "§c§l=";
            return "?";
        }

        public static double round(double value, int places) {
            if (places < 0)
                throw new IllegalArgumentException();
            long factor = (long)Math.pow(10.0D, places);
            value *= factor;
            long tmp = Math.round(value);
            double dou = (tmp / factor);
            return dou;
        }

        public static int r(int i) {
            return random.nextInt(i);
        }

        public static int randInt(int min, int max) {
            Random rand = new Random();
            int randomNum = rand.nextInt(max - min + 1) + min;
            return randomNum;
        }

        public static double sup(double... value) {
            if (value[0] < value[1])
                return value[1];
            return value[0];
        }

        public static double inf(double... value) {
            if (value[0] > value[1])
                return value[1];
            return value[0];
        }

        public static double offset2d(Entity a, Entity b) {
            return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
        }

        public static double offset2d(Location a, Location b) {
            return offset2d(a.toVector(), b.toVector());
        }

        public static double offset2d(Vector a, Vector b) {
            a.setY(0);
            b.setY(0);
            return a.subtract(b).length();
        }

        public static double offset(Entity a, Entity b) {
            return offset(a.getLocation().toVector(), b.getLocation().toVector());
        }

        public static double offset(Location a, Location b) {
            return offset(a.toVector(), b.toVector());
        }

        public static double offset(Vector a, Vector b) {
            return a.subtract(b).length();
        }

        public static float getArrow2(Location from, Location to) {
            if (!from.getWorld().getName().equals(to.getWorld().getName()))
                return -1000.0F;
            double dx = to.getX() - from.getX();
            double dz = to.getZ() - from.getZ();
            double baseAngleWithZAxis = Math.toDegrees(Math.atan(dz / dx)) + ((dx >= 0.0D) ? -90 : 90);
            float finalAngle = (float)(baseAngleWithZAxis - from.getYaw());
            if (finalAngle >= 180.0F) {
                finalAngle = -180.0F + finalAngle - 180.0F;
            } else if (finalAngle < -180.0F) {
                finalAngle += 360.0F;
            }
            return finalAngle;
        }

        public static Vector getFrontVector(Location loc) {
            float newZ = (float)(loc.getZ() + 4.0D * Math.sin(Math.toRadians((loc.getYaw() - 90.0F))));
            float newX = (float)(loc.getX() + 4.0D * Math.cos(Math.toRadians((loc.getYaw() - 90.0F))));
            return new Vector(newX - loc.getX(), 0.0D, newZ - loc.getZ());
        }

        public static Vector rotateAroundAxisY(Vector v, double angle) {
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double x = v.getX() * cos + v.getZ() * sin;
            double z = v.getX() * -sin + v.getZ() * cos;
            return v.setX(x).setZ(z);
        }
    }
