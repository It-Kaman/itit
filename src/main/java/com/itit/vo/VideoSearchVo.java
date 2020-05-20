package com.itit.vo;

import com.itit.entry.Video;
import com.itit.util.StringUtil;

public class VideoSearchVo extends Video {

    //点击次数
    private String hotNum;

    public String getHotNum() {
        return hotNum;
    }

    public void setHotNum(String hotNum) {
        this.hotNum = hotNum;
    }

    @Override
    public String toString() {
        return "VideoSearchVo{" +
                super.toString() +
                "hotNum='" + hotNum + '\'' +
                '}';
    }
}
