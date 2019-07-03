package com.hs.beijing_agent.controller;


import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.entity.commom.Constant;
import com.hs.beijing_agent.entity.enums.ResponseMessageEnum;
import com.hs.beijing_agent.util.FileUtil;
import com.hs.beijing_agent.util.HttpClientTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 *@描述 推送支付单给海关
 *@参数
 *@返回值
 *@创建人  TangYujie
 *@创建时间  2019/7/2 15:16
 */
@RestController
@RequestMapping(value="/api/beijing/")
public class PushPayOrderToBeijingController extends BaseController{


    private static HttpClientTemplate client = HttpClientTemplate.Factory.getClient();

    /**
     *@描述 1-获取单个订单的支付订单推送报文信息，处于测试阶段
     *@参数
     *@返回值
     *@创建人  TangYujie
     *@创建时间  2019/7/2 15:15
     */
    @RequestMapping(value="/payorder/single/{orderId}",method = RequestMethod.GET,produces = {JSON_UTF8})
    public String  getSinglePayOrderMessage(@PathVariable(value="orderId",required = true)Long orderId) throws IOException {
        //1-请求订单Xml数据
        String receiveUrl = Constant.SERVER_IP + "api/beijing/customer/get/payOrderXML/" + orderId;
        String result = client.execute(receiveUrl);
        //2-返回数据异常兼容
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = String.valueOf(jsonObject.get("code"));
        if(!code.equals("200")){
            System.out.println("返回数据异常，不是200");
            return ResponseMessageEnum.SERVER_DATA_STATUS_ERROR.appendEmptyData();
        }
        //3-解析数据并执行对应逻辑
        String xmlStr = String.valueOf(jsonObject.get("data"));
        JSONObject dataJson  = JSONObject.parseObject(xmlStr);;
        //Object orderId = dataJson.get("orderId");
        Object xmlData = dataJson.get("xmlData");
        //3-1-将xml数据保存到本地clinet下的send文件下
        String fileName = System.currentTimeMillis()+".xml";
        FileUtil.saveFileTolocal(String.valueOf(xmlData),Constant.CLIENT_SEND_PATH,fileName);
        //FileUtil.createXml(String.valueOf(xmlData),Constant.CLIENT_SEND_PATH,fileName);
        //3-2-请求接口更新相关订单状态为正在推送
/*        String returnUrl = Constant.SERVER_IP + "api/beijing/customer/receive/sent/" + orderId;
        String resultData = client.execute(returnUrl);
        System.out.println("resultData:" + resultData);*/

        return ResponseMessageEnum.SUCCESS.appendEmptyData();

    }

}
