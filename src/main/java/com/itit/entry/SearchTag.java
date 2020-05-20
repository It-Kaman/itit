package com.itit.entry;

import java.math.BigInteger;

public class SearchTag {
    private Integer id;
    private String searchName;
    private BigInteger num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public BigInteger getNum() {
        return num;
    }

    public void setNum(BigInteger num) {
        this.num = num;
    }
    @Override
    public String toString() {
        return "SearchTag{" +
                "id=" + id +
                ", searchName='" + searchName + '\'' +
                ", num=" + num +
                '}';
    }

    public static void main(String[] args){
        SearchTag searchTag = new SearchTag();
        searchTag.setNum(new BigInteger("1"));
        System.out.println(searchTag.getNum().add(new BigInteger("1")));
    }
}
