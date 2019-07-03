package com.hs.beijing_agent.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.entity.commom.Constant;
import com.hs.beijing_agent.util.FileUtil;
import com.hs.beijing_agent.util.HttpClientTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushOrderToBeijingService {

    private static HttpClientTemplate client = HttpClientTemplate.Factory.getClient();

    
    
    /**
     * @description:1-请求需要推送的订单
     * @param:[]
     * @return:void
     * @date:2019/4/25
     * @author:tangyj
     * @remark:       
     * */
    public static void pushOrderXmlMessageBiz() throws IOException {
        //1-请求订单Xml数据
        String receiveUrl = Constant.SERVER_IP + "api/beijing/customer/message/list";
        String result = client.execute(receiveUrl);
        //2-返回数据异常兼容
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = String.valueOf(jsonObject.get("code"));
        if(!code.equals("200")){
            System.out.println("返回数据异常，不是200");
            return;
        }
        //3-解析数据并执行对应逻辑
        String xmlListStr = String.valueOf(jsonObject.get("data"));
        JSONArray jsonArray = JSONObject.parseArray(xmlListStr);
        for(int i=0; i<jsonArray.size(); i ++ ){
            JSONObject dataJson = JSONObject.parseObject(String.valueOf(jsonArray.get(i)));
            Object orderId = dataJson.get("orderId");
            Object xmlData = dataJson.get("xmlData");
            //3-1-将xml数据保存到本地clinet下的send文件下
            String fileName = System.currentTimeMillis()+".xml";
            FileUtil.saveFileTolocal(String.valueOf(xmlData),Constant.CLIENT_SEND_PATH,fileName);
            //FileUtil.createXml(String.valueOf(xmlData),Constant.CLIENT_SEND_PATH,fileName);
            //3-2-请求接口更新相关订单状态为正在推送
            String returnUrl = Constant.SERVER_IP + "api/beijing/customer/receive/sent/" + orderId;
            String resultData = client.execute(returnUrl);
            System.out.println("resultData:" + resultData);
        }
    }

    //2-读取receive下的回执报文，并返给服务端，成功后删除receive下的回执报文
    public static void handleReceiveMessageBiz() throws IOException {
        //1-获取receive下的回执报文
        List<File> fileList = FileUtil.getFileListFromLocal(Constant.CLIENT_RECEIVE_PATH);
        if(fileList.size() == 0){
            return;
        }
        //2-读取报文文件的内容
        List<String> strList = new ArrayList<String>();
        for(File file : fileList){
            String str = FileUtil.readFile(file);
            str = str.replace("UTF-8","GBK");
            System.out.println("读取报文文件的内容:" + str);
            strList.add(str);
        }
        //2-将数据发送给项目服务器
        String returnUrl = Constant.SERVER_IP + "/api/beijing/customer/receive/message";
        Map<String,Object> reqMap = new HashMap<String,Object>();
        reqMap.put("dataList",strList);
        String result = client.excuteJson(returnUrl, reqMap);
        //3-(发送成功后)删除receive下的回执报文
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = String.valueOf(jsonObject.get("code"));
        if(!code.equals("200")){
            return;
        }
        for(File file : fileList){
            if(file.exists() && !file.isDirectory()){
                System.out.println("回执成功！删除文件");
                file.delete();
            }
        }
    }


    public static void main(String[] args){
        try {
            pushOrderXmlMessageBiz();
            //handleReceiveMessageBiz();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
