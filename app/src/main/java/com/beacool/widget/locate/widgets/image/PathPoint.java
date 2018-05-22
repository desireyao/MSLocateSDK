package com.beacool.widget.locate.widgets.image;

/**
 * Package com.beacool.widget.install.widgets.
 * Created by JoshuaYin on 16/2/14.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class PathPoint {
    private static final String TAG = "PathPointView";

    private String name = "";
    private long time = 0;

    private double coordinateX = 0.0, coordinateY = 0.0;
    double posX = 0.0, posY = 0.0;

    public PathPoint(String name, double coordinateX, double coordinateY) {
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }
}
