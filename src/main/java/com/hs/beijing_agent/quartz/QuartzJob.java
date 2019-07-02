package com.hs.beijing_agent.quartz;

/* *
 *@Description:
 *@Author:TYJ
 *@Date: create in  2019/6/10 11:13
 */

import com.hs.beijing_agent.service.PushOrderToBeijingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class QuartzJob {

    Logger log = LoggerFactory.getLogger(QuartzJob.class);


    //1-定时请求要推送给海关的订单
    public void getPushOrderList(){
        try {
            log.info("PushOrderToBeijingService.pushOrderXmlMessageBiz()  is strating ");
            PushOrderToBeijingService.pushOrderXmlMessageBiz();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //2-定时获取海关返回的信息，并传给业务后台
    public void getPushOrderResponse(){
        try {
            log.info("PushOrderToBeijingService.handleReceiveMessageBiz()  is strating ");
            PushOrderToBeijingService.handleReceiveMessageBiz();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
