package com.itit.query;

import com.itit.entry.User;

import java.util.Arrays;
import java.util.Date;

public class VideoQuery extends BaseQuery {
    private String name;
    private Byte status;
    private User author;
    private Date startDate;
    private Date endDate;
    private boolean time;
    private Integer[] column;
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "VideoQuery{" +
                super.toString()+
                "name='" + name + '\'' +
                ", status=" + status +
                ", author=" + author +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", time=" + time +
                ", column=" + Arrays.toString(column) +
                '}';
    }
}
