package com.itit.service.Ifac;

import com.github.pagehelper.PageInfo;
import com.itit.entry.Teacher;
import com.itit.query.TeacherQuery;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TeacherService {
    public void add(Integer userid,Integer job_id);

    public PageInfo<Teacher> findByQuery(TeacherQuery teacherQuery);

    public int update(Teacher teacher);

    public int aduit(Teacher teacher);

    public int aduitByIds(int[] ids,Byte teacher_status);
    public int deleteByTeacher(Teacher teacher);
}
