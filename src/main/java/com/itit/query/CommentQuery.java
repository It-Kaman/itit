package com.itit.query;

import com.itit.entry.Comment;
import com.itit.entry.User;

import java.util.Date;

public class CommentQuery extends BaseQuery {
    private Integer object_id;
    private User from_id;
    private User to_id;
    private Date createdate;

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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getObject_id() {
        return object_id;
    }

    public void setObject_id(Integer object_id) {
        this.object_id = object_id;
    }

    @Override
    public String toString() {
        return "CommentQuery{" +
                "object_id=" + object_id +
                ", from_id=" + from_id +
                ", to_id=" + to_id +
                ", createdate=" + createdate +
                '}';
    }
}
