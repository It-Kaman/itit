package com.itit.util;

import java.io.*;

/**
 *输入流转文件
 */
public class FileUtil {
    public static void multiPartFileToFile(InputStream is, File file){
        try(OutputStream out = new FileOutputStream(file)){
            byte[] buffer = new byte[1024];
            int num = -1;
            while ((num = is.read(buffer)) != -1){
                out.write(buffer,0,num);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
