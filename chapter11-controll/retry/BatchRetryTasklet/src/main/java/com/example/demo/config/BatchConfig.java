package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    /** StepBuilderのFactoryクラス. */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** JobBuilderのFactoryクラス. */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private Tasklet retryTasklet;

    /** TaskletのStepを生成 */
    @Bean
    public Step retryTaskletStep() {
        return stepBuilderFactory.get("RetryTaskletStep")
            .tasklet(retryTasklet)
            .build();
    }

    /** TaskletのJobを生成 */
    @Bean
    public Job retryTaskletJob() throws Exception {
        return jobBuilderFactory.get("RetryTaskletJob")
            .incrementer(new RunIdIncrementer())
            .start(retryTaskletStep())
            .build();
    }
}
