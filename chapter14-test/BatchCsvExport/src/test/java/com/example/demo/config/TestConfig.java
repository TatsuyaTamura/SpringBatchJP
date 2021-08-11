package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfig {

    @Autowired
    @Qualifier("JdbcCursorJob")
    private Job jdbcCursorJob;

    @Bean
    @Primary
    public Job testJob() {
        return jdbcCursorJob;
    }
}
