package com.marslocate.network.bean;

import java.util.List;

/**
 * Created by yaoh on 2018/5/21.
 */

public class GetMapInfo {


    /**
     * result : 0
     * message : 查询地图列表成功
     * data : {"mapLists":[{"mapId":147,"deviceCount":9,"height":713,"originCoordinate_x":0,"width":609,"networkId":"1002C0","originCoordinate_y":0,"serNum":1,"mapName":"测试","mapUrl":"http://m4.beacool.com/iot_file/image/963ea0e076184da4a7aa148bfdbe8b46F2.jpg"}]}
     */

    private int result;
    private String message;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<MapListsBean> mapLists;

        public List<MapListsBean> getMapLists() {
            return mapLists;
        }

        public void setMapLists(List<MapListsBean> mapLists) {
            this.mapLists = mapLists;
        }

        public static class MapListsBean {
            /**
             * mapId : 147
             * deviceCount : 9
             * height : 713
             * originCoordinate_x : 0
             * width : 609
             * networkId : 1002C0
             * originCoordinate_y : 0
             * serNum : 1
             * mapName : 测试
             * mapUrl : http://m4.beacool.com/iot_file/image/963ea0e076184da4a7aa148bfdbe8b46F2.jpg
             */

            private int mapId;
            private int deviceCount;
            private int height;
            private int originCoordinate_x;
            private int width;
            private String networkId;
            private int originCoordinate_y;
            private int serNum;
            private String mapName;
            private String mapUrl;

            public int getMapId() {
                return mapId;
            }

            public void setMapId(int mapId) {
                this.mapId = mapId;
            }

            public int getDeviceCount() {
                return deviceCount;
            }

            public void setDeviceCount(int deviceCount) {
                this.deviceCount = deviceCount;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getOriginCoordinate_x() {
                return originCoordinate_x;
            }

            public void setOriginCoordinate_x(int originCoordinate_x) {
                this.originCoordinate_x = originCoordinate_x;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getNetworkId() {
                return networkId;
            }

            public void setNetworkId(String networkId) {
                this.networkId = networkId;
            }

            public int getOriginCoordinate_y() {
                return originCoordinate_y;
            }

            public void setOriginCoordinate_y(int originCoordinate_y) {
                this.originCoordinate_y = originCoordinate_y;
            }

            public int getSerNum() {
                return serNum;
            }

            public void setSerNum(int serNum) {
                this.serNum = serNum;
            }

            public String getMapName() {
                return mapName;
            }

            public void setMapName(String mapName) {
                this.mapName = mapName;
            }

            public String getMapUrl() {
                return mapUrl;
            }

            public void setMapUrl(String mapUrl) {
                this.mapUrl = mapUrl;
            }
        }
    }
}
