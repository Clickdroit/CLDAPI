package fr.clickdroit.api.utils;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class Math2 {
    public static final int min(int a, int b, int c) {
        return (a <= b) ? ((a <= c) ? a : c) : ((b <= c) ? b : c);
    }

    public static final int min(int... nums) {
        if (nums != null && nums.length != 0) {
            int min = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < min)
                    min = nums[i];
            }
            return min;
        }
        assert false;
        return 0;
    }

    public static final int max(int a, int b, int c) {
        return (a >= b) ? ((a >= c) ? a : c) : ((b >= c) ? b : c);
    }

    public static final int max(int... nums) {
        if (nums != null && nums.length != 0) {
            int max = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] > max)
                    max = nums[i];
            }
            return max;
        }
        assert false;
        return 0;
    }

    public static final double min(double a, double b, double c) {
        return (a <= b) ? ((a <= c) ? a : c) : ((b <= c) ? b : c);
    }

    public static final double min(double... nums) {
        if (nums != null && nums.length != 0) {
            double min = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < min)
                    min = nums[i];
            }
            return min;
        }
        assert false;
        return Double.NaN;
    }

    public static final double max(double a, double b, double c) {
        return (a >= b) ? ((a >= c) ? a : c) : ((b >= c) ? b : c);
    }

    public static final double max(double... nums) {
        if (nums != null && nums.length != 0) {
            double max = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] > max)
                    max = nums[i];
            }
            return max;
        }
        assert false;
        return Double.NaN;
    }

    public static final int minPositive(int... nums) {
        int max = -1;
        if (nums != null)
            for (int num : nums) {
                if (num >= 0 && (num < max || max == -1))
                    max = num;
            }
        return max;
    }

    public static final int fit(int min, int x, int max) {
        assert min <= max : String.valueOf(min) + "," + x + "," + max;
        return (x <= min) ? min : ((x >= max) ? max : x);
    }

    public static final short fit(short min, short x, short max) {
        assert min <= max : String.valueOf(min) + "," + x + "," + max;
        return (x <= min) ? min : ((x >= max) ? max : x);
    }

    public static final long fit(long min, long x, long max) {
        assert min <= max : String.valueOf(min) + "," + x + "," + max;
        return (x <= min) ? min : ((x >= max) ? max : x);
    }

    public static final float fit(float min, float x, float max) {
        assert min <= max : String.valueOf(min) + "," + x + "," + max;
        return (x <= min) ? min : ((x >= max) ? max : x);
    }

    public static final double fit(double min, double x, double max) {
        assert min <= max : String.valueOf(min) + "," + x + "," + max;
        return (x <= min) ? min : ((x >= max) ? max : x);
    }

    public static final double mod(double d, double m) {
        double r = d % m;
        return (r < 0.0D) ? (r + m) : r;
    }

    public static final float mod(float d, float m) {
        float r = d % m;
        return (r < 0.0F) ? (r + m) : r;
    }

    public static final int mod(int d, int m) {
        int r = d % m;
        return (r < 0) ? (r + m) : (r % m);
    }

    public static final long mod(long d, long m) {
        long r = d % m;
        return (r < 0L) ? (r + m) : (r % m);
    }

    public static final long floor(double d) {
        long l = (long)d;
        if (d >= 0.0D)
            return l;
        if (l == Long.MIN_VALUE)
            return Long.MIN_VALUE;
        return (d == l) ? l : (l - 1L);
    }

    public static final long ceil(double d) {
        long l = (long)d;
        if (d <= 0.0D)
            return l;
        if (l == Long.MAX_VALUE)
            return Long.MAX_VALUE;
        return (d == l) ? l : (l + 1L);
    }

    public static final long round(double d) {
        if (d == 0.49999999999999994D)
            return 0L;
        if (Math.getExponent(d) >= 52)
            return (long)d;
        return floor(d + 0.5D);
    }

    public static final int floorI(double d) {
        int i = (int)d;
        if (d >= 0.0D)
            return i;
        if (i == Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        return (d == i) ? i : (i - 1);
    }

    public static final int ceilI(double d) {
        int i = (int)d;
        if (d <= 0.0D)
            return i;
        if (i == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (d == i) ? i : (i + 1);
    }

    public static final int roundI(double d) {
        if (d == 0.49999999999999994D)
            return 0;
        if (Math.getExponent(d) >= 52)
            return (int)d;
        return floorI(d + 0.5D);
    }

    public static final long floor(float f) {
        long l = (long)f;
        if (f >= 0.0F)
            return l;
        if (l == Long.MIN_VALUE)
            return Long.MIN_VALUE;
        return (f == (float)l) ? l : (l - 1L);
    }

    public static final long ceil(float f) {
        long l = (long)f;
        if (f <= 0.0F)
            return l;
        if (l == Long.MAX_VALUE)
            return Long.MAX_VALUE;
        return (f == (float)l) ? l : (l + 1L);
    }

    public static final long round(float f) {
        if (f == 0.49999997F)
            return 0L;
        if (Math.getExponent(f) >= 23)
            return (long)f;
        return floor(f + 0.5F);
    }

    public static final int floorI(float f) {
        int i = (int)f;
        if (f >= 0.0F)
            return i;
        if (i == Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        return (f == i) ? i : (i - 1);
    }

    public static final int ceilI(float f) {
        int i = (int)f;
        if (f <= 0.0F)
            return i;
        if (i == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        return (f == i) ? i : (i + 1);
    }

    public static final int roundI(float f) {
        if (f == 0.49999997F)
            return 0;
        if (Math.getExponent(f) >= 23)
            return (int)f;
        return floorI(f + 0.5F);
    }

    public static final int nextPowerOfTwo(int n) {
        if (n < 0) {
            int i = n ^ 0xFFFFFFFF;
            i |= i >> 1;
            i |= i >> 2;
            i |= i >> 4;
            i |= i >> 8;
            i |= i >> 16;
            i ^= 0xFFFFFFFF;
            return (n == i) ? n : (i >> 1);
        }
        int h = Integer.highestOneBit(n);
        return (n == h) ? n : (h << 1);
    }

    public static final long nextPowerOfTwo(long n) {
        if (n < 0L) {
            long l = n ^ 0xFFFFFFFFFFFFFFFFL;
            l |= l >> 1L;
            l |= l >> 2L;
            l |= l >> 4L;
            l |= l >> 8L;
            l |= l >> 16L;
            l |= l >> 32L;
            l ^= 0xFFFFFFFFFFFFFFFFL;
            return (n == l) ? n : (l >> 1L);
        }
        long h = Long.highestOneBit(n);
        return (n == h) ? n : (h << 1L);
    }

    public static final double frac(double d) {
        double r = mod(d, 1.0D);
        return (r == 1.0D) ? 0.0D : r;
    }

    public static final float frac(float f) {
        float r = mod(f, 1.0F);
        return (r == 1.0F) ? 0.0F : r;
    }

    public static final int sign(byte i) {
        return i >> 7 | -i >>> 7;
    }

    public static final int sign(short i) {
        return i >> 15 | -i >>> 15;
    }

    public static final int sign(int i) {
        return i >> 31 | -i >>> 31;
    }

    public static final int sign(long i) {
        return (int)(i >> 63L) | (int)(-i >>> 63L);
    }

    public static final int sign(float f) {
        return (f > 0.0F) ? 1 : ((f < 0.0F) ? -1 : 0);
    }

    public static final int sign(double d) {
        return (d > 0.0D) ? 1 : ((d < 0.0D) ? -1 : 0);
    }

    public static final double smoothStep(double x, double x1, double x2) {
        double d = fit(0.0D, (x - x1) / (x2 - x1), 1.0D);
        return d * d * (3.0D - 2.0D * d);
    }
}
