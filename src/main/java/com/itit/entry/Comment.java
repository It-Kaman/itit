package com.itit.entry;

import java.util.Date;

public class Comment {
    private Integer id;
    private String content;
    private Integer object_id;
    private User from_id;
    private User to_id;
    private Date create_date;
    private Byte status = 0;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getObject_id() {
        return object_id;
    }

    public void setObject_id(Integer object_id) {
        this.object_id = object_id;
    }

    public User getFrom_id() {
        return from_id;
    }

    public void setFrom_id(User from_id) {
        this.from_id = from_id;
    }

    public User getTo_id() {
        return to_id;
    }

    public void setTo_id(User to_id) {
        this.to_id = to_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", object_id=" + object_id +
                ", from_id=" + from_id +
                ", to_id=" + to_id +
                ", create_date=" + create_date +
                '}';
    }
}
