package com.hs.beijing_agent.util;


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

    /**
     * @description:1-1-保存字符串到本地制定文件
     * @param:[data, path, filename]
     * @return:void
     * @date:2019/4/25
     * @author:tangyj
     * @remark:
     * */
   public static void saveFileTolocal(String data,String path,String filename) throws IOException {
       String content = data ;
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
