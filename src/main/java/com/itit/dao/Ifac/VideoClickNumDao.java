package com.itit.dao.Ifac;

import org.springframework.stereotype.Repository;

@Repository
public interface VideoClickNumDao {
    public int deleteByIds(int[] id);
}
