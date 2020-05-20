package com.itit.query;

import com.itit.entry.User;

import java.util.Date;

public class ArticleQuery extends BaseQuery {
    private String name;
    private String content;
    private User author;
    private String num;
    private String description;
    private Date createDate;
    private Byte status;
    private boolean time;

    private Integer[] column;

    public Integer[] getColumn() {
        return column;
    }

    public void setColumn(Integer[] column) {
        this.column = column;
    }

    public boolean isTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ArticleQuery{" +
                super.getPageNum() + " " +
                super.getPageSize() + " " +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", num='" + num + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
