package com.example.demo.tasklet;

import java.util.Random;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component("RandomTasklet")
@StepScope
@Slf4j
public class RandomTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {

        // 0 or 1の値をランダムに生成する
        Random random = new Random();
        int randomValue = random.nextInt(2);
        log.info("randomValue={}", randomValue);

        // StepExecutionContextに生成した値を登録
        ExecutionContext stepExecutionContext =
                contribution.getStepExecution().getExecutionContext();
        stepExecutionContext.put("randomValue", randomValue);

        return RepeatStatus.FINISHED;
    }
}
