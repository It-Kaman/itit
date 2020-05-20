package com.itit.query;

import java.util.Arrays;

public class SearchQuery extends BaseQuery {
    private Byte status;
    private String name;
    private boolean time;
    private boolean fans;
    private Integer[] column;
    private Byte tagStatus = 0;


    public Byte getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(Byte tagStatus) {
        this.tagStatus = tagStatus;
    }

    public boolean isTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public boolean isFans() {
        return fans;
    }

    public void setFans(boolean fans) {
        this.fans = fans;
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

    public Integer[] getColumn() {
        return column;
    }

    public void setColumn(Integer[] column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", fans=" + fans +
                ", column=" + Arrays.toString(column) +
                ", tagStatus=" + tagStatus +
                '}';
    }
}
