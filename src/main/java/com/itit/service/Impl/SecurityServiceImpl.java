package com.itit.service.Impl;

import com.itit.dao.Ifac.SecurityDao;
import com.itit.entry.Security;
import com.itit.service.Ifac.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Autowired
    private SecurityDao securityDao;

    @Override
    public Security CheckSecurity(Integer id){
        Security security = new Security();
        security = securityDao.GetSecurityById(id);
        return security;
    }

    @Override
    public Integer add(Security security) {
        return securityDao.add(security);
    }

    @Override
    public Integer updateQ(Security security) {
        return securityDao.updateQ(security);
    }
}
