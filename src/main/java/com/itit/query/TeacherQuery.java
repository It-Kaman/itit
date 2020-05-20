package com.itit.query;

import com.itit.entry.User;

public class TeacherQuery extends BaseQuery {
    private Byte teacher_status;
    private String username;
    private String anothername;
    private Integer user_id;

    @Override
    public String toString() {
        return "TeacherQuery{" +
                "teacher_status=" + teacher_status +
                ", username='" + username + '\'' +
                ", anothername='" + anothername + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Byte getTeacher_status() {
        return teacher_status;
    }

    public void setTeacher_status(Byte teacher_status) {
        this.teacher_status = teacher_status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnothername() {
        return anothername;
    }

    public void setAnothername(String anothername) {
        this.anothername = anothername;
    }
}
