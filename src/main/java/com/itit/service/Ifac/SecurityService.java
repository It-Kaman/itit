package com.itit.service.Ifac;

import com.itit.entry.Security;

public interface SecurityService {
    public Security CheckSecurity(Integer id);

    Integer add(Security security);

    public Integer updateQ(Security security);
}
