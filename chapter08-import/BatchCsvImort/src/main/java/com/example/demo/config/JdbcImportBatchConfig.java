package com.example.demo.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.model.Employee;

@Configuration
public class JdbcImportBatchConfig extends BaseConfig {

    /** DataSource(JDBCで必要) */
    @Autowired
    private DataSource dataSource;

    /** insert-sql(JDBC用) */
    private static final String INSERT_EMPLOYEE_SQL =
            "INSERT INTO employee (id, name, age, gender)"
            + " VALUES (:id, :name, :age, :gender)";

    /** Writer(JDBC) */
    @Bean
    @StepScope
    public JdbcBatchItemWriter<Employee> jdbcWriter() {
        // Provider生成
        BeanPropertyItemSqlParameterSourceProvider<Employee> provider =
                new BeanPropertyItemSqlParameterSourceProvider<>();

        // 設定
        return new JdbcBatchItemWriterBuilder<Employee>() // Builderの生成
            .itemSqlParameterSourceProvider(provider) // provider
            .sql(INSERT_EMPLOYEE_SQL) // SQLのセット
            .dataSource(this.dataSource) // DataSourceのセット
            .build(); // writerの生成
    }

    /** Stepの生成(JDBC) */
    @Bean
    public Step csvImportJdbcStep() {
        return this.stepBuilderFactory.get("CsvImportJdbcStep")
            .<Employee, Employee>chunk(10)
            .reader(csvReader()).listener(this.readListener)
            .processor(compositeProcessor()).listener(this.processListener)
            .writer(jdbcWriter()).listener(this.writeListener)
            .build();
    }

    /** Jobの生成(JDBC) */
    @Bean("JdbcJob")
    public Job csvImportJdbcJob() {
        return this.jobBuilderFactory.get("CsvImportJdbcJob")
            .incrementer(new RunIdIncrementer())
            .start(csvImportJdbcStep())
            .build();
    }
}
