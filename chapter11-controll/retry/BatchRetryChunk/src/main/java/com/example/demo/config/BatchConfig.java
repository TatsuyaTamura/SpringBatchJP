package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryListener;

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
    private ItemReader<String> reader;

    @Autowired
    private ItemProcessor<String, String> processor;

    @Autowired
    private ItemWriter<String> writer;

    @Autowired
    private RetryListener retryListener;

    @Value("${retry.num}")
    private Integer retryNum;

    /** Stepを生成 */
    @Bean
    public Step retryChunkStep() {
        return stepBuilderFactory.get("RetryChunkStep")
            .<String, String>chunk(10)
            .reader(this.reader)
            .processor(this.processor)
            .writer(this.writer)
            .faultTolerant() // リトライ
            .retryLimit(this.retryNum) // リトライ回数
            .retry(Exception.class) // リトライ対象の例外クラス
            .listener(retryListener) // listener
            .build();
    }

    /** Jobを生成 */
    @Bean
    public Job retryTaskletJob() throws Exception {
        return jobBuilderFactory.get("RetryChunkJob")
            .incrementer(new RunIdIncrementer())
            .start(retryChunkStep())
            .build();
    }
}
