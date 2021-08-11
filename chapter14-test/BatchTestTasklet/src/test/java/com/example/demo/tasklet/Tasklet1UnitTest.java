package com.example.demo.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
@DisplayName("Tasklet1のUnitTest")
public class Tasklet1UnitTest {

    @InjectMocks
    private Tasklet1 tasklet1;
    
    @BeforeAll
    public static void initAll() {
        log.info("=== Tasklet1 UnitTest Start ===");
    }
    
    @AfterAll
    public static void tearDownAll() {
        log.info("=== Tasklet1 UnitTest End ===");
    }

    @Test
    @DisplayName("RepeatStatusがFINISHEDで終了すること")
    public void checkRepeatStatus() throws Exception {
        // 準備
        StepContribution contribution = getStepContribution();
        // テスト
        RepeatStatus repeatStatus = tasklet1.execute(contribution, getChunkContext());
        // 検証
        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
    }

    /** JobExecution生成 */
    public JobExecution getJobExecution() {
        // Jobパラメーター生成
        JobParameters params = new JobParametersBuilder()
            .addString("param", "paramTest")
            .toJobParameters();

        // JobExecution生成
        String jobName = "UnitTestJob";
        Long instanceId = 1L;
        Long executionId = 1L;
        JobExecution execution = MetaDataInstanceFactory
                .createJobExecution(jobName, instanceId, executionId, params);
        execution.getExecutionContext().putString("jobKey", "jobValue");
        
        return execution;
    }

    /** StepExecution生成 */
    public StepExecution getStepExecution() {
        // StepExecution生成
        StepExecution execution = new StepExecution("stepName", getJobExecution());
        execution.getExecutionContext().putString("stepKey", "stepValue");
        
        return execution;
    }

    /** StepContribution生成 */
    public StepContribution getStepContribution() {
        // StepExecution生成
        StepExecution execution = getStepExecution();
        // StepContribution生成
        StepContribution contribution = execution.createStepContribution();
        
        return contribution;
    }

    /** ChunkContext生成 */
    public ChunkContext getChunkContext() {
        // StepExecution生成
        StepExecution execution = getStepExecution();
        // StepContext生成
        StepContext stepContext = new StepContext(execution);
        // ChunkContext生成
        ChunkContext chunkContext = new ChunkContext(stepContext);
        
        return chunkContext;
    }
}
