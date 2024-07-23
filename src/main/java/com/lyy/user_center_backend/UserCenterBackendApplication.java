package com.lyy.user_center_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lyy.user_center_backend.mapper") // @MapperScan不仅扫描指定包中的所有Mapper接口，还隐含地将这些接口标记为Spring管理的Bean。
public class UserCenterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendApplication.class, args);
    }

}
