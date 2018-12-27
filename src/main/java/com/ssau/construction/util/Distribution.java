package com.ssau.construction.util;

import java.util.Random;

public class Distribution {
    private static Random random = new Random();

    public static double getUniformDistribution(double a, double b){
        return random.nextDouble()*(b-a)+a;
    }

    public static double getExponentialDistribution(double y){
        return -(Math.log(random.nextDouble()))/y;
    }

    public static double getNormalDistribution(double mean, double deviation){
        return random.nextGaussian()*deviation+mean;
    }
}
