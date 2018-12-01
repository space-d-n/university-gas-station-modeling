package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public abstract class FunctionalBlock implements Serializable, Cloneable {
    protected transient GraphicsContext graphicsContext;
    public abstract void render(double x, double y, int size);

    public FunctionalBlock(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext){
        this.graphicsContext = graphicsContext;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
