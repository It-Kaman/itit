package com.itit.dao.Ifac;

import com.itit.entry.Teacher;
import com.itit.query.TeacherQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherDao {
    /*插入数据*/
    public void add(@Param("userId") Integer userId,@Param("job_id") Integer job_id);

    public List<Teacher> findByQuery(TeacherQuery teacherQuery);

    public Teacher queryById(Integer id);

    public int update(Teacher teacher);

    public int updateStatusById(@Param("id") int id,@Param("teacher_status")Byte teacher_status);

    public int deleteByTeacher(Teacher teacher);
}
