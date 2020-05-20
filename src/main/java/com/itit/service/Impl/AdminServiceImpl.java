package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.AdminDao;
import com.itit.entry.admin.Admin;
import com.itit.query.Admin.AdminQuery;
import com.itit.service.Ifac.AdminService;
import com.itit.util.MD5Util;
import com.itit.util.StatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Value("${admin.header}")
    private String admin_header;
    @Value("${admin.password}")
    private String admin_password;
    @Autowired
    private AdminDao adminDao;

    /*@Autowired
    public RoleMapper roleMapper;*/

    @Override
    public PageInfo<Admin> findByQuery(AdminQuery adminQuery) {
        if (adminQuery.isPagation()){
            PageHelper.startPage(adminQuery.getPageNum()/adminQuery.getPageSize()+1,adminQuery.getPageSize());
            Page<Admin> list = (Page<Admin>)adminDao.findByQuery(adminQuery);
            return  list.toPageInfo();
        }else {
            PageInfo<Admin> list = new PageInfo<>();
            list.setList(adminDao.findByQuery(adminQuery));
            return  list;
        }
    }

    @Override
    public int add(Admin user) {
        //对密码加密 MD5
        user.setHeader(admin_header);
        user.setPassword(admin_password);
        user.setStatus(StatusUtil.status_user_active);
        int result = adminDao.add(user);
        return result;
    }

    @Override
    public Admin findById(Integer id) {
        return adminDao.findById(id);
    }

    @Override
    public int update(Admin user) {
       /* //解绑
        ((AdminMapper)baseMapper).unbind(user.getId());
        for(Role r : user.getRoles()){
            ((AdminMapper)baseMapper).bind(user.getId(),r.getId());
        }
        return baseMapper.update(user);*/
       return 0;
    }

    @Override
    public Admin login(String loginName, String password) {
        //对密码加密 MD5
        password = MD5Util.degist(password + "itit");
        System.out.println(password);
        return adminDao.login(loginName,password);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        /*for(Integer id : ids){
            adminDao.unbind(id);
        }*/
        return adminDao.deleteByIds(ids);
    }
}
