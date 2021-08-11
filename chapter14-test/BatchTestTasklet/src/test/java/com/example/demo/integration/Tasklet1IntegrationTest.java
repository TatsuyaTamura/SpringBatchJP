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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import com.example.demo.component.SampleComponent;
import com.example.demo.config.BatchConfig;
import com.example.demo.tasklet.Tasklet1;
import com.example.demo.tasklet.Tasklet2;
import lombok.extern.slf4j.Slf4j;

@SpringBatchTest
@ContextConfiguration(classes = {BatchConfig.class,
        Tasklet1.class, Tasklet2.class, SampleComponent.class})
@EnableAutoConfiguration
@Slf4j
@DisplayName("Tasklet1のIntegrationTest")
public class Tasklet1IntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @BeforeAll
    public static void initAll() {
        log.info("=== Tasklet1 IntegrationTest Start ===");
    }

    @AfterAll
    public static void tearDownAll() {
        log.info("=== Tasklet1 IntegrationTest End ===");
    }

    @Test
    @DisplayName("Tasklet1のステータスが完了すること")
    public void checkStatus() {
        // 準備
        // Job起動パラメーター
        JobParameters jobParams = new JobParametersBuilder()
                .addString("param", "paramTest")
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
