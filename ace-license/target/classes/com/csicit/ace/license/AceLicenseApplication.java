package com.csicit.ace.license;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.csicit.ace.license.mapper")
public class AceLicenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AceLicenseApplication.class, args);
    }

}
