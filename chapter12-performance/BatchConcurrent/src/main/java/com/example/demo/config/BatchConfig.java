package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

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
    @Qualifier("FirstTasklet")
    private Tasklet firstTasklet;

    @Autowired
    @Qualifier("SecondTasklet")
    private Tasklet secondTasklet;

    @Autowired
    @Qualifier("ThirdTasklet")
    private Tasklet thirdTasklet;

    /** FirstStepを生成 */
    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("FirstStep")
            .tasklet(firstTasklet)
            .build();
    }

    /** SecondStepを生成 */
    @Bean
    public Step secondStep() {
        return stepBuilderFactory.get("SecondStep")
            .tasklet(secondTasklet)
            .build();
    }

    /** ThirdStepを生成 */
    @Bean
    public Step thirdStep() {
        return stepBuilderFactory.get("ThirdStep")
            .tasklet(thirdTasklet)
            .build();
    }

    /** FirstStepのFlowを生成 */
    @Bean
    public Flow firstFlow() {
        return new FlowBuilder<SimpleFlow>("FirstFlow")
            .start(firstStep()) // Step1をセット
            .build(); // Flow生成
    }

    /** SecondStepのFlowを生成 */
    @Bean
    public Flow secondFlow() {
        return new FlowBuilder<SimpleFlow>("SecondFlow")
            .start(secondStep()) // Step2をセット
            .build(); // Flow生成
    }

    /** ThirdStepのFlowを生成 */
    @Bean
    public Flow thirdFlow() {
        return new FlowBuilder<SimpleFlow>("ThirdFlow3")
            .start(thirdStep()) // Step3をセット
            .build(); // Flow生成
    }

    /** 非同期実行のTaskExecutor */
    @Bean
    public TaskExecutor asyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("concurrent_");
    }

    /** Flow分割 */
    @Bean
    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("splitFlow")
            .split(asyncTaskExecutor()) // Flow分割
            .add(secondFlow(), thirdFlow()) // 同時実行
            .build();
    }

    /** Jobを生成 */
    @Bean
    public Job concurrentJob() throws Exception {
        return jobBuilderFactory.get("ConcurrentJob")
            .incrementer(new RunIdIncrementer())
                .start(firstFlow()) // 最初のFlow
                .next(splitFlow()) // Flowをセット
                .build() // Flowの生成
            .build(); // Jobの生成
    }
}
