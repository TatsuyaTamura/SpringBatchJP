package com.example.demo.config;

import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.model.Employee;

@Configuration
public class SkipImportBatchConfig extends BaseConfig {

    /** Listener */
    @Autowired
    private SkipListener<Employee, Employee> employeeSkipListener;

    @Autowired
    private MyBatisBatchItemWriter<Employee> mybatisWriter;

    /** Stepの生成(Skip) */
    @Bean
    public Step csvImportSkipStep() {
        return this.stepBuilderFactory.get("CsvImportSkipStep")
            .<Employee, Employee>chunk(10)
            .reader(csvReader()).listener(this.readListener)
            .processor(genderConvertProcessor).listener(this.processListener)
            .writer(mybatisWriter)
            .faultTolerant() // FaultTolerant
            .skipLimit(Integer.MAX_VALUE) // 最大件数
            .skip(RuntimeException.class) // 例外クラス
            .listener(this.employeeSkipListener) // listener
            .build();
    }

    /** Jobの生成(Skip) */
    @Bean("SkipJob")
    public Job csvImportSkipJob() {
        return this.jobBuilderFactory.get("CsvImportSkipJob")
            .incrementer(new RunIdIncrementer())
            .start(csvImportSkipStep())
            .build();
    }
}
