package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.admin.Admin;
import com.itit.query.Admin.AdminQuery;

public interface AdminService {
     Admin login(String loginName, String password);
     PageInfo<Admin> findByQuery(AdminQuery e);

     int add(Admin admin);

     Admin findById(Integer id);

     int update(Admin t);

     int deleteByIds(Integer[] id);
}
