package com.itit.dao.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.User;
import com.itit.query.UserQuery;
import com.itit.vo.UserSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    User SelectUserByUsernameAndPassword(@Param("username")String username,@Param("password")  String password);

    int SelectCountByUser(User user);
    int InsertIntoUserByRegister(User user);
    User SelectAllByUserId(Integer id);
    int UpdateByUser(User user);
    List<User> selectUserByUser(User user);
    User selectSecurityByUserId(Integer userId);
    int updateSecruityId(@Param("security_id") Integer security_id,@Param("id") Integer id);
    List<UserSearchVo> queryByUserQuery(UserQuery userQuery);
}
