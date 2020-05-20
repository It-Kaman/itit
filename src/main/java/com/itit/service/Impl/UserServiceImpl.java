package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.UserDao;
import com.itit.entry.User;
import com.itit.query.UserQuery;
import com.itit.service.Ifac.UserService;
import com.itit.vo.UserSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    public User login(String username,String password){
        User user = userDao.SelectUserByUsernameAndPassword(username,password);
        return user;
    }
    @Override
    public int SelectCountByUser(User user) {
        return userDao.SelectCountByUser(user);
    }
    @Override
    public boolean InsertIntoUserByRegister(User user) {
        if(userDao.InsertIntoUserByRegister(user) == 1){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public User SelectAllByUserId(Integer id) {
        return userDao.SelectAllByUserId(id);
    }
    @Override
    public int UpdateByUser(User user) {
        return userDao.UpdateByUser(user);
    }
    @Override
    public String SecurityPassword(Integer userId) {
        User user = userDao.SelectAllByUserId(userId);
        return user.getPassword();
    }
    @Override
    public User selectSecurityByUserId(Integer userId) {
        return userDao.selectSecurityByUserId(userId);
    }
    @Override
    public int updateSecruityId(Integer security_id, Integer id) {
        return userDao.updateSecruityId(security_id,id);
    }

    @Override
    public PageInfo<UserSearchVo> queryByUserQuery(UserQuery userQuery) {
        if(userQuery.isPagation())
        {
            PageHelper.startPage(userQuery.getPageNum() / userQuery.getPageSize() + 1, userQuery.getPageSize());
            Page<UserSearchVo> list = (Page<UserSearchVo>) userDao.queryByUserQuery(userQuery);
            return list.toPageInfo();
        }else {
            PageInfo<UserSearchVo> pageInfo = new PageInfo<UserSearchVo>();
            List<UserSearchVo> list = userDao.queryByUserQuery(userQuery);
            pageInfo.setList(list);
            return pageInfo;
        }
    }


}
