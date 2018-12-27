package com.ssau.construction.util;

public class ExponentialIntervalGetter implements IntervalGetter {
    private double i;
    private double interval;

    public ExponentialIntervalGetter(double i) {
        this.i = i;
        generateNext();
    }

    @Override
    public double getInterval() {
        return interval;
    }

    @Override
    public void generateNext(){
        interval = Distribution.getExponentialDistribution(i);
        System.out.println(interval);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getI() {
        return i;
    }
}
