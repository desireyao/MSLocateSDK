package com.marslocate.model;

import com.marslocate.network.bean.GetAllNetworkList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoh on 2018/5/18.
 */

public class MSNetworkList {

    private List<Network> mNetworkList = new ArrayList<>();

    public List<Network> getNetworkList() {
        return mNetworkList;
    }

    public void setNetworkList(List<Network> mNetworkList) {
        this.mNetworkList = mNetworkList;
    }

    public MSNetworkList(GetAllNetworkList data) {
        if (data.getResult() != 0 && data.getData() == null) {
            return;
        }

        List<GetAllNetworkList.DataBean.NetworkListBean> dataList = data.getData().getNetworkList();
        for (GetAllNetworkList.DataBean.NetworkListBean bean : dataList) {
            Network network = new Network();
            network.setNetworkId(bean.getNetworkId());
            network.setNetworkName(bean.getNetworkName());
            mNetworkList.add(network);
        }
    }

    public static class Network {

        private String networkId;
        private String networkName;

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

        @Override
        public String toString() {
            return "\n{" +
                    "networkId='" + networkId + '\'' +
                    ", networkName='" + networkName + '\'' +
                    '}';
        }
    }

}
