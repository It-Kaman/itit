package com.itit.dao.Ifac;

import com.itit.entry.admin.Admin;
import com.itit.query.Admin.AdminQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminDao{
	public List<Admin> findByQuery(AdminQuery adminQuery);
	public int add(Admin admin);
	Admin findById(Integer id);
	int update(Admin admin);
	Integer deleteByIds(Integer[] ids);
	int findCountById(Integer parentId);
	public Admin login(@Param("loginName") String loginName, @Param("password") String password);
	public int bind(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
	public int unbind(@Param("userId") Integer userId);
}
