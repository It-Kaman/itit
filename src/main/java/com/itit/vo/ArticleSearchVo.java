package com.itit.vo;

import com.itit.entry.Article;
import com.itit.entry.Tag;
import com.itit.entry.Video;

import java.util.List;

public class ArticleSearchVo extends Article {

    //点击次数
    private String clickNum;

    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    @Override
    public String toString() {
        return "ArticleSearchVo{" +
                super.toString() +
                "clickNum='" + clickNum + '\'' +
                ", tags=" + tags +
                '}';
    }
}
