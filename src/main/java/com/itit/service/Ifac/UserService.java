package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.User;
import com.itit.query.UserQuery;
import com.itit.vo.UserSearchVo;

import java.util.List;

public interface UserService {
    public User login(String username,String password);

    public int SelectCountByUser(User user);

    public boolean InsertIntoUserByRegister(User user);

    public User SelectAllByUserId(Integer id);

    public int UpdateByUser(User user);

    public String SecurityPassword(Integer userId);

    User selectSecurityByUserId(Integer userId);

    int updateSecruityId(Integer security_id,Integer id);

    PageInfo<UserSearchVo> queryByUserQuery(UserQuery userQuery);
}
