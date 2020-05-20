package com.itit.entry;

import java.util.List;

public class ACMiddle {
    private Integer id;
    private Article article;
    private List<Tag> tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "ACMiddle{" +
                "id=" + id +
                ", article=" + article +
                ", tag=" + tag +
                '}';
    }
}
