package com.itit.vo;

import com.itit.entry.User;

public class UserSearchVo extends User {
    private Integer videoNum;
    private Integer articleNum;
    private String fansNum;

    public Integer getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(Integer videoNum) {
        this.videoNum = videoNum;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    @Override
    public String toString() {
        return "UserSearchVo{" +
                super.toString() +
                "videoNum=" + videoNum +
                ", articleNum=" + articleNum +
                ", fansNum='" + fansNum + '\'' +
                '}';
    }
}
