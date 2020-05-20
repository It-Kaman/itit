package com.itit.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itit.dao.Ifac.TeacherDao;
import com.itit.dao.Ifac.UserDao;
import com.itit.entry.Teacher;
import com.itit.entry.User;
import com.itit.query.TeacherQuery;
import com.itit.service.Ifac.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private UserDao userDao;
    @Override
    public void add(Integer userId,Integer job_id) {
        teacherDao.add(userId,job_id);
    }

    @Override
    public PageInfo<Teacher> findByQuery(TeacherQuery teacherQuery) {
        if (teacherQuery.isPagation()){
            PageHelper.startPage(teacherQuery.getPageNum()/teacherQuery.getPageSize()+1,teacherQuery.getPageSize());
            Page<Teacher> list = (Page<Teacher>)teacherDao.findByQuery(teacherQuery);
            return  list.toPageInfo();
        }else {
            PageInfo<Teacher> list = new PageInfo<>();
            list.setList(teacherDao.findByQuery(teacherQuery));
            return  list;
        }
    }

    @Override
    public int update(Teacher teacher) {
        return teacherDao.update(teacher);
    }

    @Override
    public int aduit(Teacher teacher) {
        int result = teacherDao.update(teacher);
        User user = teacher.getUser();
        if(teacher.getTeacher_status() == 1){
            user.setRole((byte)2);
            userDao.UpdateByUser(user);
        };
        return result;
    }

    @Override
    public int aduitByIds(int[] ids,Byte teacher_status) {
        //寻找是否存在当前用户
        for(int i = 0 ; i < ids.length;i++){
            Teacher teacher = teacherDao.queryById(ids[i]);
            if(teacher.getTeacher_status() != 0 ){
                continue;
            }
            System.out.println(teacher);
            User user = userDao.SelectAllByUserId(teacher.getUser().getId());
            if(user == null){
                try {
                    throw new LoginException("不存在当前用户");
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            }else {
                teacherDao.updateStatusById(ids[i],teacher_status);
                if(teacher_status == 1)
                {
                    User user1 = new User();
                    user1.setId(user.getId());
                    user1.setRole((byte) 2);
                    userDao.UpdateByUser(user1);
                }
            }
        }
        return ids.length;
    }

    @Override
    public int deleteByTeacher(Teacher teacher) {
        return teacherDao.deleteByTeacher(teacher);
    }


}
