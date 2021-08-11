package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.component.SampleComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component("Tasklet2")
@StepScope
@Slf4j
public class Tasklet2 implements Tasklet {

    @Autowired
    private SampleComponent component;
    
    @Getter
    private int randomValue;
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {

        log.info("Tasklet2");
        
        randomValue = component.random();
        log.info("randomValue={}", randomValue);

        return RepeatStatus.FINISHED;
    }
}
