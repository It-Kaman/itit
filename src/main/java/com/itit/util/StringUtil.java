package com.itit.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    public static final String USER_SESSION_KEY = "user";
    public static final String ERROR_SESSION_KEY ="error";
    public static final String MSG = "msg";
    public static final String ADMIN_SESSION_KEY = "admin";
    public static final Integer PAGESIZE = 10;

    //判断是否为空
    public static boolean isEmpty(String str){
        if (str == null || str.trim() == "" || str.equals("")) {
            return true;
        }else {
            return false;
        }
    }

    /*
        文件命名格式化
        时间形式的文件打散
     */
    public static String dateFiles(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String[] dateStr = dateFormat.format(date).split("-");
        StringBuffer sf = new StringBuffer();
        for (String str : dateStr){
            sf.append("/").append(str);
        }
        return sf.toString();
    }

    //数量格式化
    public static String NumFormate(int num){
        if(num/10000>=1){
            BigDecimal bg = new BigDecimal((double)num/10000);

            return bg.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue() + "万";
        }else {
            return num + "";
        }
    }

    //日期转字符串格式类型
    public static String DateFormate(String formate,Date date){
        if (StringUtil.isEmpty(formate)){
            formate = "yyyy:MM:dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
        return simpleDateFormat.format(date);
    }

    //Size
    public static String FileSizeFormate(long size){
        if (size / (1024 * 1024 * 1024)>1){
            return (size/ (1024 * 1024 * 1024)) + "G";
        }else if(size /(1024*1024)>1){
            return (size/ (1024 * 1024)) + "M";
        }else if(size / 1024 > 1){
            return (size/1024) + "K";
        }else {
            return size + "B";
        }
    }
}
