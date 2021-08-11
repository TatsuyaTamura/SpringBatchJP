package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    /** JobBuilderのFactoryクラス */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /** StepBuilderのFactoryクラス */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Partitioner samplePartitioner;

    @Autowired
    private Tasklet workerTasklet;

    /** 非同期実行のTaskExecutor */
    @Bean
    public TaskExecutor asyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("worker_");
    }

    @Bean
    public Step workerStep() {
        return stepBuilderFactory.get("WorkerStep")
            .tasklet(workerTasklet)
            .build();
    }

    @Bean
    public Step partitionStep() {
        return stepBuilderFactory.get("PartitionStep")
            .partitioner("WorkerStep", samplePartitioner) // Partitioner
            .step(workerStep()) // step
            .gridSize(3) // 同時実行数
            .taskExecutor(asyncTaskExecutor())
            .build();
    }

    @Bean
    public Job partitionJob() {
        return jobBuilderFactory.get("PartitionJob")
            .start(partitionStep())
            .build();
    }
}
