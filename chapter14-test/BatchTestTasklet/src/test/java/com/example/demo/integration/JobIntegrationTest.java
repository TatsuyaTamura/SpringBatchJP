package com.example.demo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.example.demo.BatchTestTaskletApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBatchTest
@ContextConfiguration(classes = {BatchTestTaskletApplication.class})
@Slf4j
@DisplayName("JobのIntegrationTest")
public class JobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @BeforeAll
    public static void initAll() {
        log.info("=== Job IntegrationTest Start ===");
    }
    
    @AfterAll
    public static void tearDownAll() {
        log.info("=== Job IntegrationTest End ===");
    }

    @Test
    @DisplayName("Job実行:StepとJobのステータスが完了であること")
    public void executeJob() throws Exception {
        // 準備
        // Job起動パラメーター
        JobParameters jobParams = new JobParametersBuilder()
                    .addString("param", "JobTest")
                    .toJobParameters();

        // テスト
        JobExecution jobExecution = jobLauncherTestUtils
                .launchJob(jobParams);

        // 検証
        // Stepのステータス
        jobExecution.getStepExecutions().forEach(stepExecution ->
            assertThat(ExitStatus.COMPLETED)
            .isEqualTo(stepExecution.getExitStatus()));
        // Jobのステータス
        assertThat(ExitStatus.COMPLETED).isEqualTo(jobExecution.getExitStatus());
    }
    
    @Test
    @DisplayName("Step1実行:Stepのステータスが完了であること")
    public void executeStep1() throws Exception {
        // 準備
        // Job起動パラメーター
        JobParameters jobParams = new JobParametersBuilder()
                    .addString("param", "StepTest")
                    .toJobParameters();

        // テスト
        JobExecution jobExecution = jobLauncherTestUtils
                .launchStep("Step1", jobParams);

        // 検証
        // Stepのステータス
        jobExecution.getStepExecutions().forEach(stepExecution ->
            assertThat(ExitStatus.COMPLETED)
            .isEqualTo(stepExecution.getExitStatus()));
    }
}
