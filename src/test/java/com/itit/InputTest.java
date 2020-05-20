package com.itit;

import org.junit.Test;

import java.io.*;
import java.net.URL;

public class InputTest {
    @Test
    public void test(){
        try {
            InputStream in =  new URL("https://itit-1300622267.cos.ap-guangzhou.myqcloud.com/comment/video/1/vid%3D1_2020-4-16.txt").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String len = null;
            while ((len = reader.readLine()) != null){
                System.out.println(len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
