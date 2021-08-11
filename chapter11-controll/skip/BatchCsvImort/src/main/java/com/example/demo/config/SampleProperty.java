package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.ToString;

@Component
@PropertySource("classpath:property/sample.properties")
@Getter
@ToString
public class SampleProperty {
    
    @Value("${csv.path}")
    private String csvPath;
}
