package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("Tasklet1")
    private Tasklet tasklet1;

    @Autowired
    @Qualifier("Tasklet2")
    private Tasklet tasklet2;

    @Bean
    public Step testStep1() {
        return stepBuilderFactory.get("Step1")
            .tasklet(tasklet1)
            .build();
    }

    @Bean
    public Step testStep2() {
        return stepBuilderFactory.get("Step2")
            .tasklet(tasklet2)
            .build();
    }

    @Bean
    public Job testTaskletJob() {
        return jobBuilderFactory.get("TaskletJob")
            .incrementer(new RunIdIncrementer())
            .start(testStep1())
            .next(testStep2())
            .build();
    }
}
