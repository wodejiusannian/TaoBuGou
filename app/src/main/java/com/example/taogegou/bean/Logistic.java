package com.example.taogegou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/12.
 */

public class Logistic {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * order_number :
         * phone_number : 2222222222
         * address : 独山
         * good_ID : 1
         * name : 姜金浩
         * award_type : 2
         * has_buy : 0
         * transport_number :
         */

        private String order_number;
        private String phone_number;
        private String address;
        private String good_ID;
        private String name;
        private int award_type;
        private int has_buy;
        private String transport_number;

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGood_ID() {
            return good_ID;
        }

        public void setGood_ID(String good_ID) {
            this.good_ID = good_ID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAward_type() {
            return award_type;
        }

        public void setAward_type(int award_type) {
            this.award_type = award_type;
        }

        public int getHas_buy() {
            return has_buy;
        }

        public void setHas_buy(int has_buy) {
            this.has_buy = has_buy;
        }

        public String getTransport_number() {
            return transport_number;
        }

        public void setTransport_number(String transport_number) {
            this.transport_number = transport_number;
        }
    }
}
