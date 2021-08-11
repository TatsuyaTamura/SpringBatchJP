package com.example.demo.config.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;

@Configuration
public class JpaPagingBatchConfig extends BaseConfig {

    /** EntityManagerFactory(JPAで必要) */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /** JpaPagingItemReader */
    @Bean
    @StepScope
    public JpaPagingItemReader<Employee> jpaPagingReader() {
        // SQL
        String sql = "select * from employee where gender = :genderParam order by id";
        // クエリーの設定
        JpaNativeQueryProvider<Employee> queryProvider = 
                new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(sql);
        queryProvider.setEntityClass(Employee.class);

        // クエリーに渡すパラメーター
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("genderParam", 1);

        return new JpaPagingItemReaderBuilder<Employee>()
            .entityManagerFactory(entityManagerFactory)
            .name("jpaPagingItemReader")
            .queryProvider(queryProvider)
            .parameterValues(parameterValues)
            .pageSize(5)
            .build();
    }

    /** JpaPagingItemReaderを使用するStepの生成 */
    @Bean
    public Step exportJpaPagingStep() throws Exception {
        return this.stepBuilderFactory.get("ExportJpaPagingStep")
            .<Employee, Employee>chunk(10)
            .reader(jpaPagingReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
    }

    /** JpaPagingItemReaderを使用するJobの生成 */
    @Bean("JpaPagingJob")
    public Job exportJpaPagingJob() throws Exception {
        return this.jobBuilderFactory.get("ExportJpaPagingJob")
            .incrementer(new RunIdIncrementer())
            .start(exportJpaPagingStep())
            .build();
    }
}
