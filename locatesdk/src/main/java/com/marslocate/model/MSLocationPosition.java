package com.marslocate.model;

/**
 * Created by yaoh on 2018/5/21.
 */

public class MSLocationPosition {

    private float positionX;
    private float positionY;

    public MSLocationPosition(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public float getPositionX() {
        return positionX;
    }

    private void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    private void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        return " [" +
                "  positionX = " + positionX +
                ", positionY = " + positionY +
                ']';
    }
}
