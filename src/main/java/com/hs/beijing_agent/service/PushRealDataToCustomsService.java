package com.hs.beijing_agent.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.entity.commom.Constant;
import com.hs.beijing_agent.util.HttpClientTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PushRealDataToCustomsService {

    private static HttpClientTemplate client = HttpClientTemplate.Factory.getClient();


    /**
     * @description:1-请求加签数据并进行加签---------待定
     * @param:[]
     * @return:void
     * @date:2019/5/16
     * @author:tangyj
     * @remark:       
     * */
    public static void signrealData() throws IOException {
        //1-从雪糕服务器获取数据

        String url = Constant.SERVER_IP + "/api/beijing/customer/custom/lunxun";
        String result = client.execute(url);
        System.out.println("execute:" + result);
        //2-解析待加签的数据
        if(result.equals("{\"error\":20000}")){
            //{"error":20000}
            return;
        }
        Map<String,String> maps = (Map<String,String>) JSON.parse(result);
        //3-遍历map数据，进行加密，并调用雪糕平台接口将数据传给海关
        Set<String> keys = maps.keySet();
        for(String orderNo : keys){
            String signData = maps.get(orderNo);
            System.out.println("signData：" + signData);
            //加密

        }

    }

    public static void main(String[] args) throws IOException {
        signrealData();
    }
}
