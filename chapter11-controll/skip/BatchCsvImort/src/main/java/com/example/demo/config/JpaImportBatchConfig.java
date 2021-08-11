package com.example.demo.config;

import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.model.Employee;

@Configuration
public class JpaImportBatchConfig extends BaseConfig {

    /** EntityManagerFactory(JPAで必要) */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /** Writer(JPA) */
    @Bean
    public JpaItemWriter<Employee> jpaWriter() {
        JpaItemWriter<Employee> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(this.entityManagerFactory);
        return writer;
    }

    /** Stepの生成(JPA) */
    @Bean
    public Step csvImportJpaStep() {
        return this.stepBuilderFactory.get("CsvImportJpaStep")
            .<Employee, Employee>chunk(10)
            .reader(csvReader()).listener(this.readListener)
            .processor(compositeProcessor()).listener(this.processListener)
            .writer(jpaWriter()).listener(this.writeListener)
            .build();
    }

    /** Jobの生成(JPA) */
    @Bean("JpaJob")
    public Job csvImportJpaJob() {
        return this.jobBuilderFactory.get("CsvImportJpaJob")
            .incrementer(new RunIdIncrementer())
            .start(csvImportJpaStep())
            .build();
    }
}
