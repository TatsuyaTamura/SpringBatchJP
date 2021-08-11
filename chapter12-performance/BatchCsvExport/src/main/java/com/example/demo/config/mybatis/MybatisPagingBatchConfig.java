package com.example.demo.config.mybatis;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;

@Configuration
public class MybatisPagingBatchConfig extends BaseConfig {

    /** SqlSessionFactory(MyBatisで必要) */
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /** MyBatisPagingItemReader */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<Employee> mybatisPagingReader() throws Exception {
        // クエリーに渡すパラメーター
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("genderParam", 1);

        return new MyBatisPagingItemReaderBuilder<Employee>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.demo.repository.EmployeeMapper.findByGenderPaging")
                .parameterValues(parameterValues) // パラメーター
                .pageSize(10) // ページサイズ
                .build(); // readerの生成
    }

    /** MybatisPagingItemReaderを使用するStepの生成 */
    @Bean
    public Step exportMybatisPagingStep() throws Exception {
        return this.stepBuilderFactory.get("ExportMybatisPagingStep")
            .<Employee, Employee>chunk(10)
            .reader(mybatisPagingReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
    }

    /** MybatisPagingItemReaderを使用するJobの生成 */
    @Bean("MybatisPagingJob")
    public Job exportMybatisPagingJob() throws Exception {
        return this.jobBuilderFactory.get("ExportMybatisPagingJob")
            .incrementer(new RunIdIncrementer())
            .start(exportMybatisPagingStep())
            .build();
    }
}
