package com.itit.query;


public class BaseQuery {
    private Integer id;
    private Integer pageNum;
    private Integer pageSize;
    private boolean pagation;

    public boolean isPagation() {
        return pagation;
    }

    public void setPagation(boolean pagation) {
        this.pagation = pagation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "BaseQuery{" +
                "id=" + id +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pagation=" + pagation +
                '}';
    }
}
