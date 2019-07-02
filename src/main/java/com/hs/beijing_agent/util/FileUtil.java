package com.hs.beijing_agent.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private final static String  ENCODING = "utf-8";

    /**
     * @description:1-保存字符串到本地制定文件
     * @param:[data, path, filename]
     * @return:void
     * @date:2019/4/25
     * @author:tangyj
     * @remark:
     * */
    public static void saveFileTolocal(String data,String path,String filename) throws IOException {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File distFile = new File(path + "\\" + filename);
            if (!distFile.getParentFile().exists()) {
                distFile.getParentFile().mkdirs();
            }
            bufferedReader = new BufferedReader(new StringReader(data));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024];         //字符缓冲区
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
