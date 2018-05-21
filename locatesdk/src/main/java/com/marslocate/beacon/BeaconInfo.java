package com.marslocate.beacon;

/**
 * Created by yaoh on 2018/5/20.
 */

public class BeaconInfo {

    private int mapId;            //  beancon所在的 mapId
    private double coordinateX;    //  在地图的坐标
    private double coordinateY;

    private String uuid;
    private int majorID, minorID;

    private double distance;
    private int rssi;

    private int averageRSSI; //  平均的 rssi
    private String macAddress;

    public void processAverageRSSI(int rssi) {
        if (averageRSSI != 0) {
            averageRSSI = (averageRSSI + rssi) / 2;
        } else {
            averageRSSI = rssi;
        }
    }

    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    public int getMinorID() {
        return minorID;
    }

    public void setMinorID(int minorID) {
        this.minorID = minorID;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getAverageRSSI() {
        return averageRSSI;
    }

    public void setAverageRSSI(int averageRSSI) {
        this.averageRSSI = averageRSSI;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "\n {" +
                " uuid = " + uuid +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", majorID=" + majorID +
                ", minorID=" + minorID +
                ", distance=" + distance +
                ", rssi=" + rssi +
                ", averageRSSI=" + averageRSSI +
                ", mapId = " + mapId +
                ", macAddress = " + macAddress +
                '}' + "\n";
    }
}
