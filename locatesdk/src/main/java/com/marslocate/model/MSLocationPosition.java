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

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }
}
