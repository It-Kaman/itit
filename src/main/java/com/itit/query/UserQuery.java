package com.itit.query;

public class UserQuery extends BaseQuery {
    private Integer id;
    private String username;
    private String password;
    private Byte sex;
    private Integer age;
    private String anothername;
    private Byte role;
    private String phone;
    private String sign;
    private String email;
    private Boolean fans = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFans() {
        return fans;
    }

    public void setFans(Boolean fans) {
        this.fans = fans;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
