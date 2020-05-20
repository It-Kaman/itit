package com.itit.dao.Ifac;

import com.itit.entry.Security;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityDao {
    public Security GetSecurityById(Integer id);

    public Integer add(Security security);
    public Integer updateQ(Security security);
}
