package com.ssau.construction.car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Incasator extends Car {

    private static final int width = 32;
    private static final int height = 17;
    private static double rate = 200;
    private static ArrayList<String> cars = new ArrayList<>();
    private static Random random = new Random();

    static {
        cars.addAll(Arrays.asList("pictures/incasator.png"));
    }

    public Incasator(double x, double y) {
        super(x, y, cars.get(random.nextInt(cars.size())), width, height);
    }

    public static void setRate(double rate) {
        Incasator.rate = rate;
    }

    @Override
    public double getRate() {
        return rate;
    }

    @Override
    public String getType() {
        return "Инкасатор";
    }
}
