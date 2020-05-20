package com.itit.entry;

public class Fans {
    private Integer id;
    private Integer User_id;
    private  Integer Fan_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return User_id;
    }

    public void setUser_id(Integer user_id) {
        User_id = user_id;
    }

    public Integer getFan_id() {
        return Fan_id;
    }

    public void setFan_id(Integer fan_id) {
        Fan_id = fan_id;
    }

    @Override
    public String toString() {
        return "Fans{" +
                "id=" + id +
                ", User_id=" + User_id +
                ", Fan_id=" + Fan_id +
                '}';
    }
}
