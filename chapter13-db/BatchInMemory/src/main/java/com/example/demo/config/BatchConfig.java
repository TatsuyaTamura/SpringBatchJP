package com.example.demo.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.domain.Employee;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemReader<Employee> employeeReader;

    /** DataSource(JDBCで必要) */
    @Autowired
    private DataSource postgresDataSource;

    @Autowired
    @Qualifier("JpaWriter")
    private ItemWriter<Employee> jpaWriter;

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
            .dataSource(this.postgresDataSource) // DataSourceのセット
            .build(); // writerの生成
    }

    /** Stepの生成 */
    @Bean
    public Step inMemoryStep() {
        return this.stepBuilderFactory.get("InMemoryStep")
            .<Employee, Employee>chunk(1)
            .reader(employeeReader)
            .writer(jpaWriter)
            .build();
    }

    /** Jobの生成 */
    @Bean
    public Job inMemoryJob() {
        return this.jobBuilderFactory.get("InMemoryJob")
            .incrementer(new RunIdIncrementer())
            .start(inMemoryStep())
            .build();
    }
}
