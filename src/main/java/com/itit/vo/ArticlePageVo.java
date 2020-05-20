package com.itit.vo;


import java.util.Date;

/**
 * 专栏页面显示实体扩展类
 */
public class ArticlePageVo {
    private Integer id;
    private String name;
    private String content;
    private String description;
    private UserSearchVo author;
    private String num;
    private Byte status;
    private Date createdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserSearchVo getAuthor() {
        return author;
    }

    public void setAuthor(UserSearchVo author) {
        this.author = author;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Override
    public String toString() {
        return "ArticlePageVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", num='" + num + '\'' +
                ", status=" + status +
                ", createdate=" + createdate +
                '}';
    }
}
