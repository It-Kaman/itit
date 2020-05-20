package com.itit.entry;
/*老师表实体*/
public class Teacher {
    /*老师id*/
    private Integer id;
    /*老师状态*/
    private Byte teacher_status;
    /*关联用户*/
    private User user;

    private Byte job_id;

    public Teacher() {
    }


    public Byte getJob_id() {
        return job_id;
    }

    public void setJob_id(Byte job_id) {
        this.job_id = job_id;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getTeacher_status() {
        return teacher_status;
    }

    public void setTeacher_status(Byte teacher_status) {
        this.teacher_status = teacher_status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", teacher_status=" + teacher_status +
                ", user=" + user +
                '}';
    }
}
