package com.example.taogegou.config;

public interface NetConfig {
    String SHOP_PATH = "http://api.dataoke.com/index.php?r=Port/index&type=total&appkey=zjab0boefq&v=2&page=%d";
    /*登录，注册，修改密码的接口*/

    String BASE_PATH = "http://120.77.46.28:8080";
    //登录
    String LOGIN_PATH_PHONE = BASE_PATH + "/test/servlet/login?loginType=1&username=%s&password=%s";
    //三方登陆的接口
    String LOGIN_PATH_THIRD = BASE_PATH + "/test/servlet/login?loginType=0&openID=%s";
    //注册接口
    String REGISTER_PATH = BASE_PATH + "/test/servlet/register?userName=%s&password=%s&shareFromWho=%s";
    //修改密码
    String CHANGE_PAHT = BASE_PATH + "/test/servlet/changePassword?userName=wubo&oldPassword=1&newPassword=2";
    //搜索的接口
    String SEARCH_PATH = BASE_PATH + "/test/servlet/searchGoods?keywords=%s&orderBy=%d&pageIndex=%d";
    //转链接口
    /*mm_103424431_12490887_47302518   wubo pid*/
    /*mm_111980370_13448108_54518145   wushuang pid*/
    /*mm_114562371_20984473_70926435   longwen pid*/
    String TRANSLATE_PATH = "https://uland.taobao.com/coupon/edetail?activityId=%s&itemId=%s&pid=mm_103424431_12490887_47302518&src=mdk_mdktk&dx=1&nowake=1";
    //是否更新的接口
    String IS_FRESH_PATH = "http://hmyc365.net:8080/HuiMei/appVersion!getAndVersion.action";
    //一等奖接口
    String MALL_FIRST = "http://120.77.46.28:8080/test/servlet/getAwardGoods?pageIndex=%d&awardType=1";
    //二等奖接口
    String MALL_SECOND = "http://120.77.46.28:8080/test/servlet/getAwardGoods?pageIndex=%d&awardType=2";
    //三等奖接口
    String MALL_THIRD = "http://120.77.46.28:8080/test/servlet/getAwardGoods?pageIndex=%d&awardType=3";
    //判断此用户有几个奖品
    String GET_PRIZE = "http://120.77.46.28:8080/test/servlet/getUnusedAwards?userID=%s";

}
