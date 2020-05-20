package com.itit.query;

import java.math.BigInteger;

public class SearchTagQuery extends BaseQuery {
    private String searchName;
    private BigInteger num;

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
}
