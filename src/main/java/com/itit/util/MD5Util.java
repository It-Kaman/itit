package com.itit.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    //MD5
        //单向加密(摘要)[不可逆]
        //组成
            //32位的数字和字母
        //如何生成
            //使用JDK提供的摘要类生成字节码数组
            //字节码数组中有符号变成无符号[负数统统变成正数]
            //每一个无符号要转换为2位16进制[变成16进制]
        private static String[] hex ={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        public static String degist(String param){

            StringBuffer result = new StringBuffer();
            try{
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] bys = messageDigest.digest(param.getBytes());
                for(int by: bys){
                    if(by<0){
                        by = by + 256;
                    }
                    int h = by/16;
                    int l = by%16;
                    result.append(hex[h]).append(hex[l]);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return result.toString();
        }


        public static void main(String[] args){
            System.out.println(degist(degist(degist("123456mywms"))));
        }
}
