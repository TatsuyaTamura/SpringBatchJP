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

    /** JobBuilderのFactoryクラス */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderのFactoryクラス */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /** HelloTasklet */
    @Autowired
    @Qualifier("HelloTasklet")
    private Tasklet helloTasklet;
    
    /** HelloTasklet2 */
    @Autowired
    @Qualifier("HelloTasklet2")
    private Tasklet helloTasklet2;

    /** TaskletのStepを生成 */
    @Bean
    public Step taskletStep1() {
        return stepBuilderFactory.get("HelloTaskletStep1") // Builderの取得
            .tasklet(helloTasklet) // Taskletのセット
            .build(); // Stepの生成
    }
    
    /** TaskletのStepを生成 */
    @Bean
    public Step taskletStep2() {
        return stepBuilderFactory.get("HelloTaskletStep2") // Builderの取得
            .tasklet(helloTasklet2) // Taskletのセット
            .build(); // Stepの生成
    }

    /** Jobを生成 */
    @Bean
    public Job taskletJob() throws Exception {
        return jobBuilderFactory.get("HelloWorldTaskletJob") // Builderの取得
            .incrementer(new RunIdIncrementer()) // IDのインクリメント
            .start(taskletStep1()) // 最初のStep
            .next(taskletStep2()) // 次のStep
            .build(); // Jobの生成
    }
}
