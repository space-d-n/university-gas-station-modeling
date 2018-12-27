package com.ssau.construction.car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Passenger extends Car {

    private static final int width = 32;
    private static final int height = 17;
    private double petrolAmount;
    private static double rate = 200;
    private static ArrayList<String> cars = new ArrayList<>();
    private static Random random = new Random();

    static {
        cars.addAll(Arrays.asList("pictures/passenger1.png", "pictures/passenger2.png", "pictures/passenger3.png"));
    }

    public Passenger(double x, double y) {
        super(x, y, cars.get(random.nextInt(cars.size())), width, height);
        this.petrolAmount = random.nextInt(50);
    }

    public double getPetrolAmount() {
        return petrolAmount;
    }

    public void setPetrolAmount(double petrolAmount) {
        this.petrolAmount = petrolAmount;
    }

    public static void setRate(double rate) {
        Passenger.rate = rate;
    }

    @Override
    public double getRate() {
        return rate;
    }

    @Override
    public String getType() {
        return "Легковой";
    }
}
