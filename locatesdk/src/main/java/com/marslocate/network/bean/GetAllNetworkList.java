package com.marslocate.network.bean;

import java.util.List;

/**
 * Created by yaoh on 2018/5/18.
 */

public class GetAllNetworkList {


    /**
     * data : {"networkList":[{"authority":1,"createTime":"2018-01-24 16:42:36","networkId":"10002E","networkName":"简易网络1","networkType":2},{"authority":1,"createTime":"2018-01-12 11:09:31","networkId":"10002A","networkName":"jiang5","networkType":1},{"authority":1,"createTime":"2017-12-20 16:44:10","networkId":"10000F","networkName":"jiang1","networkType":1},{"authority":1,"createTime":"2017-12-05 18:20:54","networkId":"100006","networkName":"jiang","networkType":1}]}
     * message : 查询用户全部网络成功
     * result : 0
     */

    private DataBean data;
    private String message;
    private int result;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static class DataBean {
        private List<NetworkListBean> networkList;

        public List<NetworkListBean> getNetworkList() {
            return networkList;
        }

        public void setNetworkList(List<NetworkListBean> networkList) {
            this.networkList = networkList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "networkList=" + networkList +
                    '}';
        }

        public static class NetworkListBean {
            /**
             * authority : 1
             * createTime : 2018-01-24 16:42:36
             * networkId : 10002E
             * networkName : 简易网络1
             * networkType : 2
             */

            private int authority;
            private String createTime;
            private String networkId;
            private String networkName;
            private int networkType;

            public int getAuthority() {
                return authority;
            }

            public void setAuthority(int authority) {
                this.authority = authority;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getNetworkId() {
                return networkId;
            }

            public void setNetworkId(String networkId) {
                this.networkId = networkId;
            }

            public String getNetworkName() {
                return networkName;
            }

            public void setNetworkName(String networkName) {
                this.networkName = networkName;
            }

            public int getNetworkType() {
                return networkType;
            }

            public void setNetworkType(int networkType) {
                this.networkType = networkType;
            }

            @Override
            public String toString() {
                return "NetworkListBean{" +
                        "authority=" + authority +
                        ", createTime='" + createTime + '\'' +
                        ", networkId='" + networkId + '\'' +
                        ", networkName='" + networkName + '\'' +
                        ", networkType=" + networkType +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "GetAllNetworkList{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
