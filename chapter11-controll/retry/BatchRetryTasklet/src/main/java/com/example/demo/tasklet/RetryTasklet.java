package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component("RetryTasklet")
@StepScope
@Slf4j
public class RetryTasklet implements Tasklet {

    @Value("${retry.num}")
    private Integer retryNum;

    private int count = 0;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        while (retryNum > count) {
            log.info("count={}", count + 1);
            count++;
            // リトライする
            return RepeatStatus.CONTINUABLE;
        }

        return RepeatStatus.FINISHED;
    }
}
