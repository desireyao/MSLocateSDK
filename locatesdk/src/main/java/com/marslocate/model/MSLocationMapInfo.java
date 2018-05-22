package com.marslocate.model;

import com.marslocate.network.bean.GetMapInfo;

/**
 * Created by yaoh on 2018/5/21.
 */

public class MSLocationMapInfo {

    private int mapId;
    private String mapName;
    private String mapUrl;
    private double width;
    private double height;
    private double originCoordinate_x;
    private double originCoordinate_y;

    public int getMapId() {
        return mapId;
    }

    private void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getMapName() {
        return mapName;
    }

    private void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    private void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public double getWidth() {
        return width;
    }

    private void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    private void setHeight(double height) {
        this.height = height;
    }

    public double getOriginCoordinate_x() {
        return originCoordinate_x;
    }

    public void setOriginCoordinate_x(double originCoordinate_x) {
        this.originCoordinate_x = originCoordinate_x;
    }

    public double getOriginCoordinate_y() {
        return originCoordinate_y;
    }

    public void setOriginCoordinate_y(double originCoordinate_y) {
        this.originCoordinate_y = originCoordinate_y;
    }

    @Override
    public String toString() {
        return "{" +
                "mapId=" + mapId +
                ", mapName='" + mapName + '\'' +
                ", mapUrl='" + mapUrl + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", originCoordinate_x=" + originCoordinate_x +
                ", originCoordinate_y=" + originCoordinate_y +
                '}';
    }

    public static MSLocationMapInfo parseData(GetMapInfo data) {
        if (data.getResult() != 0 || data.getData().getMapLists() == null) {
            return null;
        }

        GetMapInfo.DataBean.MapListsBean mapListsBean = data.getData().getMapLists().get(0);
        MSLocationMapInfo mapInfo = new MSLocationMapInfo();
        mapInfo.setMapId(mapListsBean.getMapId());
        mapInfo.setMapName(mapListsBean.getMapName());
        mapInfo.setWidth(mapListsBean.getWidth());
        mapInfo.setHeight(mapListsBean.getHeight());
        mapInfo.setMapUrl(mapListsBean.getMapUrl());
        mapInfo.setOriginCoordinate_x(mapListsBean.getOriginCoordinate_x());
        mapInfo.setOriginCoordinate_y(mapListsBean.getOriginCoordinate_y());

        return mapInfo;
    }
}
