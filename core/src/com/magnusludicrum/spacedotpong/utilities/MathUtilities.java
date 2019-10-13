package com.magnusludicrum.spacedotpong.utilities;

public class MathUtilities {

    public static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

}
