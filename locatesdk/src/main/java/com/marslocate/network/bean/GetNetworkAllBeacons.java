package com.marslocate.network.bean;

import java.util.List;

/**
 * Created by yaoh on 2018/5/21.
 */

public class GetNetworkAllBeacons {

    /**
     * result : 0
     * message : 查询指定网络下所有地图上所有设备信息及对应的iBeacon信息成功
     * data : {"deviceList":[{"mapId":166,"deviceType":7009,"coordinateX":674.47,"coordinateY":146.53,"ibeaconInfo":{"ibeaconId":172,"minor":4541,"uuid":"FDA50693-A4E2-4FB-1AFC-FC6EB07647825","major":213},"deviceMacId":"fffffffff1"},{"mapId":166,"deviceType":7002,"coordinateX":674.47,"coordinateY":146.53,"ibeaconInfo":{"ibeaconId":173,"minor":4541,"uuid":"FDA50693-A4E2-4FB1A-FCFC-6EB07647825","major":213},"deviceMacId":"fffffffff2"}]}
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
        private List<DeviceListBean> deviceList;

        public List<DeviceListBean> getDeviceList() {
            return deviceList;
        }

        public void setDeviceList(List<DeviceListBean> deviceList) {
            this.deviceList = deviceList;
        }

        public static class DeviceListBean {
            /**
             * mapId : 166
             * deviceType : 7009
             * coordinateX : 674.47
             * coordinateY : 146.53
             * ibeaconInfo : {"ibeaconId":172,"minor":4541,"uuid":"FDA50693-A4E2-4FB-1AFC-FC6EB07647825","major":213}
             * deviceMacId : fffffffff1
             */

            private int mapId;
            private int deviceType;
            private double coordinateX;
            private double coordinateY;
            private IbeaconInfoBean ibeaconInfo;
            private String deviceMacId;

            public int getMapId() {
                return mapId;
            }

            public void setMapId(int mapId) {
                this.mapId = mapId;
            }

            public int getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(int deviceType) {
                this.deviceType = deviceType;
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

            public IbeaconInfoBean getIbeaconInfo() {
                return ibeaconInfo;
            }

            public void setIbeaconInfo(IbeaconInfoBean ibeaconInfo) {
                this.ibeaconInfo = ibeaconInfo;
            }

            public String getDeviceMacId() {
                return deviceMacId;
            }

            public void setDeviceMacId(String deviceMacId) {
                this.deviceMacId = deviceMacId;
            }

            public static class IbeaconInfoBean {
                /**
                 * ibeaconId : 172
                 * minor : 4541
                 * uuid : FDA50693-A4E2-4FB-1AFC-FC6EB07647825
                 * major : 213
                 */

                private int ibeaconId;
                private int minor;
                private String uuid;
                private int major;

                public int getIbeaconId() {
                    return ibeaconId;
                }

                public void setIbeaconId(int ibeaconId) {
                    this.ibeaconId = ibeaconId;
                }

                public int getMinor() {
                    return minor;
                }

                public void setMinor(int minor) {
                    this.minor = minor;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public int getMajor() {
                    return major;
                }

                public void setMajor(int major) {
                    this.major = major;
                }

                @Override
                public String toString() {
                    return "IbeaconInfoBean{" +
                            "ibeaconId=" + ibeaconId +
                            ", minor=" + minor +
                            ", uuid='" + uuid + '\'' +
                            ", major=" + major +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "DeviceListBean{" +
                        "mapId=" + mapId +
                        ", deviceType=" + deviceType +
                        ", coordinateX=" + coordinateX +
                        ", coordinateY=" + coordinateY +
                        ", ibeaconInfo=" + ibeaconInfo +
                        ", deviceMacId='" + deviceMacId + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "GetNetworkAllBeacons{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
