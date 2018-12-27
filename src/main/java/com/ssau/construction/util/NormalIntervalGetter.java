package com.ssau.construction.util;

public class NormalIntervalGetter implements IntervalGetter {
    private double m;
    private double d;
    private double interval;

    public NormalIntervalGetter(double m, double d) {
        this.m = m;
        this.d = d;
        generateNext();
    }

    @Override
    public double getInterval() {
        return interval;
    }

    @Override
    public void generateNext(){
        interval = Distribution.getNormalDistribution(m, d);
        System.out.println(interval);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public double getM() {
        return m;
    }

    public double getD() {
        return d;
    }
}
