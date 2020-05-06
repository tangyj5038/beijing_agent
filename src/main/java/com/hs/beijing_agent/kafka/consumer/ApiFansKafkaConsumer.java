package com.hs.beijing_agent.kafka.consumer;

/* *
 *@Description:
 *@Author:TYJ
 *@Date: create in  2019/10/16 16:00
 */


import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.entity.commom.Constant;
import com.hs.beijing_agent.util.FileUtil;
import com.hs.beijing_agent.util.HttpClientTemplate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiFansKafkaConsumer {

    private static HttpClientTemplate client = HttpClientTemplate.Factory.getClient();

    @Autowired
    private Environment environment;


    /**
     *@Description 1-接收kafka消息，推送订单给北京海关
     *@param
     *@return  void
     *@author  TangYujie
     *@date  2019/10/16 17:32
     */
    @KafkaListener( topics= "order_beijing")
    //public void getUbonexOrdedrData(String msg){
    public void getUbonexOrdedrData(ConsumerRecord<?, String> record){
        System.out.println("消费消息:" + record + ",消息分区:" + record.partition());

        String msg = record.value();

        System.out.println("getUbonexOrdedrData: " + msg);

        JSONObject dataJson = JSONObject.parseObject(String.valueOf(msg));
        Object orderId = dataJson.get("orderId");
        Object xmlData = dataJson.get("xmlData");
        //3-1-将xml数据保存到本地clinet下的send文件下
        String fileName = System.currentTimeMillis()+".xml";
        try {
            FileUtil.saveFileTolocal(String.valueOf(xmlData), Constant.CLIENT_SEND_PATH,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3-2-请求接口更新相关订单状态为正在推送
        String serverIp = environment.getProperty("server.ip");
        System.out.println("serverIp: " + serverIp);
        String returnUrl = serverIp + "api/beijing/customer/receive/sent/" + orderId;
        //String returnUrl = Constant.SERVER_IP + "api/beijing/customer/receive/sent/" + orderId;

        String resultData = null;
        try {
            resultData = client.execute(returnUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("resultData:" + resultData);

    }
}
