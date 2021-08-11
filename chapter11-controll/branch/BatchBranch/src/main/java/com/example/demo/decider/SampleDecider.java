package com.example.demo.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class SampleDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        // ランダム値をStepExecutionContextから取得する
        int randomValue = stepExecution.getExecutionContext()
                .getInt("randomValue");

        // ステータスを変更する
        if (randomValue == 0) {
            String status = FlowExecutionStatus.COMPLETED.getName();
            return new FlowExecutionStatus(status);
        } else {
            String status = FlowExecutionStatus.FAILED.getName();
            return new FlowExecutionStatus(status);
        }
    }
}
