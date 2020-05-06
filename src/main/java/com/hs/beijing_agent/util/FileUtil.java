package com.hs.beijing_agent.util;


import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.entity.commom.Constant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private final static String  ENCODING = "utf-8";
    static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    public static void main(String[] args) throws IOException {
        String msg ="{\"orderId\":7286,\"xmlData\":\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n<ceb:CEB311Message guid=\\\"4CDE1CFD-EDED-46B1-946C-B8022E42FC94\\\" version=\\\"1.0\\\"  xmlns:ceb=\\\"http://www.chinaport.gov.cn/ceb\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\">\\n\\t<ceb:Order>\\n\\t\\t<ceb:OrderHead>\\n\\t\\t\\t<ceb:guid>4CDE1CFD-EDED-46B1-946C-B8022E42FC94</ceb:guid>\\n\\t\\t\\t<ceb:appType>1</ceb:appType>\\n\\t\\t\\t<ceb:appTime>20200506094656</ceb:appTime>\\n\\t\\t\\t<ceb:appStatus>2</ceb:appStatus>\\n\\t\\t\\t<ceb:orderType>I</ceb:orderType>\\n\\t\\t\\t<ceb:orderNo>OP147082699250635107</ceb:orderNo>\\n\\t\\t\\t<ceb:ebpCode>330136800U</ceb:ebpCode>\\n\\t\\t\\t<ceb:ebpName>杭州雪糕文化创意有限公司</ceb:ebpName>\\n\\t\\t\\t<ceb:ebcCode>330136800U</ceb:ebcCode>\\n\\t\\t\\t<ceb:ebcName>杭州雪糕文化创意有限公司</ceb:ebcName>\\n\\t\\t\\t<ceb:goodsValue>205.0</ceb:goodsValue>\\n\\t\\t\\t<ceb:freight>0.0</ceb:freight>\\n\\t\\t\\t<ceb:discount>0.0</ceb:discount>\\n\\t\\t\\t<ceb:taxTotal>0.0</ceb:taxTotal>\\n\\t\\t\\t<ceb:acturalPaid>205.0</ceb:acturalPaid>\\n\\t\\t\\t<ceb:currency>142</ceb:currency>\\n\\t\\t\\t<ceb:buyerRegNo>4094</ceb:buyerRegNo>\\n\\t\\t\\t<ceb:buyerName>杨虹</ceb:buyerName>\\n\\t\\t\\t<ceb:buyerTelephone>18840770257</ceb:buyerTelephone>\\n\\t\\t\\t<ceb:buyerIdType>1</ceb:buyerIdType>\\n\\t\\t\\t<ceb:buyerIdNumber>210802197712250065</ceb:buyerIdNumber>\\n\\t\\t\\t<ceb:consignee>王桂芹</ceb:consignee>\\n\\t\\t\\t<ceb:consigneeTelephone>18840770257</ceb:consigneeTelephone>\\n\\t\\t\\t<ceb:consigneeAddress>北七家翡翠公园17号地9号楼1单元1707</ceb:consigneeAddress>\\n\\t\\t</ceb:OrderHead>\\n\\t\\t<ceb:OrderList>\\n\\t\\t\\t<ceb:gnum>1</ceb:gnum>\\n\\t\\t\\t<ceb:itemName>ARS安速梦纳明Kid's清新口气防蛀牙儿童漱口水草莓味</ceb:itemName>\\n\\t\\t\\t<ceb:gmodel>品牌类型：境外品牌 出口优惠情况：无 规格型号：250ml</ceb:gmodel>\\n\\t\\t\\t<ceb:unit>035</ceb:unit>\\n\\t\\t\\t<ceb:qty>1.0</ceb:qty>\\n\\t\\t\\t<ceb:price>34.0</ceb:price>\\n\\t\\t\\t<ceb:totalPrice>34.0</ceb:totalPrice>\\n\\t\\t\\t<ceb:currency>142</ceb:currency>\\n\\t\\t\\t<ceb:country>116</ceb:country>\\n\\t\\t</ceb:OrderList>\\n\\t\\t<ceb:OrderList>\\n\\t\\t\\t<ceb:gnum>2</ceb:gnum>\\n\\t\\t\\t<ceb:itemName>mama&kids 新生儿宝宝婴儿泡沫沐浴露460ml无添加低刺激</ceb:itemName>\\n\\t\\t\\t<ceb:gmodel>品牌类型：境外品牌 出口优惠情况：无 规格型号：460ml</ceb:gmodel>\\n\\t\\t\\t<ceb:unit>035</ceb:unit>\\n\\t\\t\\t<ceb:qty>1.0</ceb:qty>\\n\\t\\t\\t<ceb:price>136.0</ceb:price>\\n\\t\\t\\t<ceb:totalPrice>136.0</ceb:totalPrice>\\n\\t\\t\\t<ceb:currency>142</ceb:currency>\\n\\t\\t\\t<ceb:country>116</ceb:country>\\n\\t\\t</ceb:OrderList>\\n\\t\\t<ceb:OrderList>\\n\\t\\t\\t<ceb:gnum>3</ceb:gnum>\\n\\t\\t\\t<ceb:itemName>KAI贝印温和刮感进口刀片专业修眉刀10支装</ceb:itemName>\\n\\t\\t\\t<ceb:gmodel>品牌类型：境外品牌 出口优惠情况：无 规格型号：10本 成分：刀片/不锈钢刀（双涂层切削刃处理）支架/冷轧钢板制成</ceb:gmodel>\\n\\t\\t\\t<ceb:unit>035</ceb:unit>\\n\\t\\t\\t<ceb:qty>1.0</ceb:qty>\\n\\t\\t\\t<ceb:price>35.0</ceb:price>\\n\\t\\t\\t<ceb:totalPrice>35.0</ceb:totalPrice>\\n\\t\\t\\t<ceb:currency>142</ceb:currency>\\n\\t\\t\\t<ceb:country>116</ceb:country>\\n\\t\\t</ceb:OrderList>\\n\\t</ceb:Order>\\n\\t<ceb:BaseTransfer>\\n\\t\\t<ceb:copCode>330136800U</ceb:copCode>\\n\\t\\t<ceb:copName>杭州雪糕文化创意有限公司</ceb:copName>\\n\\t\\t<ceb:dxpMode>DXP</ceb:dxpMode>\\n\\t\\t<ceb:dxpId>DXPENT0000022272</ceb:dxpId>\\n\\t\\t<ceb:note></ceb:note>\\n\\t</ceb:BaseTransfer>\\n</ceb:CEB311Message>\"}";

        JSONObject dataJson = JSONObject.parseObject(String.valueOf(msg));
        Object xmlData = dataJson.get("xmlData");
        String data = String.valueOf(xmlData);

        String clientSendPath = Constant.CLIENT_SEND_PATH;
        String fileName = System.currentTimeMillis()+".xml";
        saveFileTolocal(data,clientSendPath,fileName);
    }

    /**
     * @description:1-1-保存字符串到本地制定文件
     * @param:[data, path, filename]
     * @return:void
     * @date:2019/4/25
     * @author:tangyj
     * @remark:
     * */
   public static void saveFileTolocal(String data,String path,String filename) throws IOException {
       String content = String.valueOf(data).replaceAll("&", "&amp;");//处理&符号导致的XML解析问题
       String fullPath = path + "/" + filename ;
       Document document = null;
       try {
           document = DocumentHelper.parseText(content );
       } catch (DocumentException e) {
           e.printStackTrace();
       }
       OutputFormat format = OutputFormat.createPrettyPrint();
           /** 指定XML编码 */
           format.setEncoding("UTF-8");
           /** 将document中的内容写入文件中 */
           XMLWriter writer;
           try {
               OutputStream outputStream = new FileOutputStream(fullPath);
               writer = new XMLWriter(new OutputStreamWriter(outputStream ,"UTF-8"), format);
               writer.write(document);
               writer.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
    }
    /**
     * @description:2-获取某个目录下的全部文件
     * @param:[path]
     * @return:void
     * @date:2019/4/25
     * @author:tangyj
     * @remark:       
     * */
    public static  List<File> getFileListFromLocal(String path){
        List<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if(!file.isDirectory()){
            return null;
        }
        File[] files = file.listFiles();
        for(File fileItem : files){
            fileList.add(fileItem);
        }
        return fileList;
    }

    /**
     * @description:3-读取文件返回字符串
     * @param:[file]
     * @return:java.lang.String
     * @date:2019/4/25
     * @author:tangyj
     * @remark:       
     * */
    public static String readFile(File file){
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            reader = new InputStreamReader(new FileInputStream(file), ENCODING);
            //将输入流写入输出流
            char[] buffer = new char[512];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //返回转换结果
        if (writer != null){
            System.out.println("writer.toString(): " + writer.toString());
            return writer.toString();
        }
        return null;
    }
}
