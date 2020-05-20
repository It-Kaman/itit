package com.itit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:config/application-bean.xml"})
@MapperScan("com.itit.dao")
public class ItitApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItitApplication.class, args);
    }

}
