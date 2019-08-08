package com.huishuo.hdata.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;

public class FileTool {

    private static final Logger logger = LoggerFactory.getLogger(FileTool.class);

    /**
     * 写文件; 如果文件已存在会覆盖
     * @param rootPath 根目录
     * @param fileName 文件名
     * @param content 内容
     */
    public static boolean writeFile(String rootPath, String fileName, String content){

        FileWriter writer = null;
        try {
            File file = Paths.get(rootPath, fileName).toFile();
            if (!file.exists()){
                createFile(file);
                logger.info(String.format("file is not exists, create new file. file=%s", file.getAbsolutePath()));
            }else{
                logger.info(String.format("file is exists, cover file. file=%s", file.getAbsolutePath()));
            }
            writer = new FileWriter(file);
            writer.write(content);
        }catch (IOException ioe){
            logger.error(String.format("write XML File error. message=%s", ExceptionUtils.getStackTrace(ioe)));
            return false;
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            }catch (IOException ioe){
                logger.error(String.format("close fileWriter error. message=%s", ExceptionUtils.getStackTrace(ioe)));
            }
        }
        return true;
    }


    public static String readFile(String filePath){
        StringBuilder content = new StringBuilder();
        BufferedReader bf = null;
        try {
            File file = Paths.get(filePath).toFile();
            if (!file.exists()){
                logger.error(String.format("file is not exists. file=%s", file.getAbsolutePath()));
                return null;
            }

            bf = new BufferedReader(new FileReader(file));

            int temp;
            while ((temp = bf.read())!=-1) {
                //if not end,the total content add the value of the stream read this time
                content.append((char)temp);
            }
        }catch (IOException ioe){
            logger.error(String.format("Write XML File error, message=%s", ExceptionUtils.getStackTrace(ioe)));
        }finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                logger.error(String.format("close file reader error. message=%s", ExceptionUtils.getStackTrace(e)));
            }
        }
        return content.toString();
    }

    public static void deleteFile(String filePath){
        File file = Paths.get(filePath).toFile();
        if (file.isFile() && file.exists()){
            if (!file.delete()){
                logger.error("file delete fail. file={}", filePath);
            }
        }else{
            logger.error(String.format("%s is not file or not exists.", filePath));
        }
    }

    public static boolean fileExists(String filePath){
        File file = Paths.get(filePath).toFile();
        return file.isFile() && file.exists();
    }

    public static void createFile(File file) throws IOException {
        File fileParent = file.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }
        file.createNewFile();
    }
}
