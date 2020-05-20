package com.itit.vo;


import java.util.Date;

/**
 * 视频页面显示实体扩展类
 */
public class VideoPageVo {
    private Integer id;
    private String name;
    private String url;
    private String description;
    private UserSearchVo author;
    private String num;
    private Byte status;
    private Date createdate;
    private String header;
    private String reason;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "VideoPageVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", num='" + num + '\'' +
                ", status=" + status +
                ", createdate=" + createdate +
                ", header='" + header + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
