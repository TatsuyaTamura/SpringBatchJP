package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component("HelloTasklet")
@StepScope
@Slf4j
public class HelloTasklet implements Tasklet {
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        log.info("Hello World");
        
        // JobExecutionContextの取得
        ExecutionContext jobContext = contribution.getStepExecution() // StepExecution
            .getJobExecution() // JobExecution
            .getExecutionContext(); // JobExecutionContext
        // Mapに値登録
        jobContext.put("jobKey", "jobValue");

        // StepExecutionContextの取得
        ExecutionContext stepContext = contribution.getStepExecution() // StepExecution
            .getExecutionContext(); // StepExecutionContext
        // Mapに値登録
        stepContext.put("stepKey", "stepValue");
        
        return RepeatStatus.FINISHED;
    }
}