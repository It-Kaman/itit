package com.itit.entry;

import org.springframework.stereotype.Component;

public class Security {
    private Integer id;
    private Byte security_q1;
    private String security_a1;
    private Byte security_q2;
    private String security_a2;

    public Security() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getSecurity_q1() {
        return security_q1;
    }

    public void setSecurity_q1(Byte security_q1) {
        this.security_q1 = security_q1;
    }

    public String getSecurity_a1() {
        return security_a1;
    }

    public void setSecurity_a1(String security_a1) {
        this.security_a1 = security_a1;
    }

    public Byte getSecurity_q2() {
        return security_q2;
    }

    public void setSecurity_q2(Byte security_q2) {
        this.security_q2 = security_q2;
    }

    public String getSecurity_a2() {
        return security_a2;
    }

    public void setSecurity_a2(String security_a2) {
        this.security_a2 = security_a2;
    }


    @Override
    public String toString() {
        return "Security{" +
                "id=" + id +
                ", security_q1=" + security_q1 +
                ", security_a1='" + security_a1 + '\'' +
                ", security_q2=" + security_q2 +
                ", security_a2='" + security_a2 + '\'' +
                '}';
    }
}
