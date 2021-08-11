package com.example.demo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import com.example.demo.BatchCsvExportApplication;
import com.example.demo.config.SampleProperty;

@SpringBatchTest
@ContextConfiguration(classes = {BatchCsvExportApplication.class})
@DisplayName("CsvExportJobのIntegrationTest")
public class CsvExportIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private SampleProperty property;

    /** 想定結果のファイルパス */
    private static final String EXPECTED_FILE_PATH = "src/test/resources/file/result.csv";

    @Test
    @Sql("/sql/test_data.sql")
    @DisplayName("ファイルが出力されていること")
    public void checkStatus() throws Exception {
        // テスト
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // 検証
        // Stepのステータス
        jobExecution.getStepExecutions().forEach(stepExecution -> assertThat(ExitStatus.COMPLETED)
            .isEqualTo(stepExecution.getExitStatus()));
        // Jobのステータス
        assertThat(ExitStatus.COMPLETED).isEqualTo(jobExecution.getExitStatus());
        // ファイルの中身が同一かチェック
        AssertFile.assertFileEquals(new FileSystemResource(EXPECTED_FILE_PATH),
                new FileSystemResource(property.outputPath()));
    }
}
