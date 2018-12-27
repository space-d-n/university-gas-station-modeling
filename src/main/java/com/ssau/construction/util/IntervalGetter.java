package com.ssau.construction.util;

public interface IntervalGetter extends Cloneable {
    double getInterval();
    default void generateNext(){};
    Object clone() throws CloneNotSupportedException;
}
