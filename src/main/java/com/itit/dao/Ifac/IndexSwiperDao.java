package com.itit.dao.Ifac;

import com.itit.entry.admin.Swiper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IndexSwiperDao {
    public List<Swiper> queryAll();
}
