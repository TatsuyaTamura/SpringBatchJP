package com.example.demo.worker;

import java.util.Map;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class WorkerTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        // パーティショナーから渡された値を取得
        Map<String, Object> context = chunkContext.getStepContext()
                .getStepExecutionContext();
        String value = (String) context.get("sampleKey");
        log.info("value={}", value);

        return RepeatStatus.FINISHED;
    }
}
