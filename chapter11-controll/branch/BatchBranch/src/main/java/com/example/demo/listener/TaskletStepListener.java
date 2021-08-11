package com.example.demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component("TaskletStepListener")
@Slf4j
public class TaskletStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Do nothing
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info(stepExecution.toString());
        return stepExecution.getExitStatus();
    }
}
