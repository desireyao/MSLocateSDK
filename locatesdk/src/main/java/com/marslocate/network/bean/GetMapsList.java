package com.marslocate.network.bean;

import java.util.List;

/**
 * Created by yaoh on 2018/5/16.
 */

public class GetMapsList {


    /**
     * result : 0
     * message : 查询地图列表成功
     * data : {"mapLists":[{"scale":80,"mapId":157,"deviceCount":3,"height":4098,"originCoordinate_x":0,"width":5216,"networkId":"1002DB","originCoordinate_y":0,"serNum":1,"mapName":"办公室","mapUrl":"http://m4.beacool.com/iot_file/image/8fa068a0a5bf455082e12ce3c6e6397bOX99GMWO7GQQV4LCN64BJ(N.png"}]}
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

    @Override
    public String toString() {
        return "GetMapsList{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
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
             * scale : 80.0
             * mapId : 157
             * deviceCount : 3
             * height : 4098.0
             * originCoordinate_x : 0.0
             * width : 5216.0
             * networkId : 1002DB
             * originCoordinate_y : 0.0
             * serNum : 1
             * mapName : 办公室
             * mapUrl : http://m4.beacool.com/iot_file/image/8fa068a0a5bf455082e12ce3c6e6397bOX99GMWO7GQQV4LCN64BJ(N.png
             */

            private double scale;
            private int mapId;
            private int deviceCount;
            private double height;
            private double originCoordinate_x;
            private double width;
            private String networkId;
            private double originCoordinate_y;
            private int serNum;
            private String mapName;
            private String mapUrl;

            public double getScale() {
                return scale;
            }

            public void setScale(double scale) {
                this.scale = scale;
            }

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

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getOriginCoordinate_x() {
                return originCoordinate_x;
            }

            public void setOriginCoordinate_x(double originCoordinate_x) {
                this.originCoordinate_x = originCoordinate_x;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }

            public String getNetworkId() {
                return networkId;
            }

            public void setNetworkId(String networkId) {
                this.networkId = networkId;
            }

            public double getOriginCoordinate_y() {
                return originCoordinate_y;
            }

            public void setOriginCoordinate_y(double originCoordinate_y) {
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

            @Override
            public String toString() {
                return "MapListsBean{" +
                        "scale=" + scale +
                        ", mapId=" + mapId +
                        ", deviceCount=" + deviceCount +
                        ", height=" + height +
                        ", originCoordinate_x=" + originCoordinate_x +
                        ", width=" + width +
                        ", networkId='" + networkId + '\'' +
                        ", originCoordinate_y=" + originCoordinate_y +
                        ", serNum=" + serNum +
                        ", mapName='" + mapName + '\'' +
                        ", mapUrl='" + mapUrl + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "mapLists=" + mapLists +
                    '}';
        }
    }
}
