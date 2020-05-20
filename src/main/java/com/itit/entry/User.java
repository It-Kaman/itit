package com.itit.entry;

import org.springframework.context.annotation.Bean;

public class User {
    private Integer id;
    private String username;
    private String password;
    private Byte sex;
    private Integer age;
    private String anothername;
    private Byte role;
    private String phone;
    private String sign;
    private String website;
    private String header;
    private String email;
    private Integer job_id;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    private Security security;

    public User(Integer id, String username, String password, Byte sex, Integer age, String anothername, Byte role, String phone, String sign, String website, String header, String email, Security security) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.anothername = anothername;
        this.role = role;
        this.phone = phone;
        this.sign = sign;
        this.website = website;
        this.header = header;
        this.email = email;
        this.security = security;
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, Byte role) {
        this.id = id;
        this.role = role;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAnothername() {
        return anothername;
    }

    public void setAnothername(String anothername) {
        this.anothername = anothername;
    }

    public Byte getRole() {
        return role;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", anothername='" + anothername + '\'' +
                ", role=" + role +
                ", phone='" + phone + '\'' +
                ", sign='" + sign + '\'' +
                ", website='" + website + '\'' +
                ", header='" + header + '\'' +
                ", email='" + email + '\'' +
                ", security=" + security +
                '}';
    }
}

