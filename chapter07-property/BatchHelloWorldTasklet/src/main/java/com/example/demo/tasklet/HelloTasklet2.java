package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component("HelloTasklet2")
@StepScope
@Slf4j
public class HelloTasklet2 implements Tasklet {
    
    @Value("#{JobExecutionContext['jobKey']}")
    private String jobValue;
    
    @Value("#{StepExecutionContext['stepKey']}")
    private String stepValue;
            
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        
        log.info("Hello World2");

        // JobExecutionContextから値を取得
        log.info("jobKey={}", jobValue);
        
        // StepExecutionContextから値を取得
        log.info("stepKey={}", stepValue);

        return RepeatStatus.FINISHED;
    }
}