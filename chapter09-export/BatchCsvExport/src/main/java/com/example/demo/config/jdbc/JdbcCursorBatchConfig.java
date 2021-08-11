package com.example.demo.config.jdbc;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;

@Configuration
public class JdbcCursorBatchConfig extends BaseConfig {

    /** DataSource(JDBCで必要) */
    @Autowired
    private DataSource dataSource;

    /** SELECT用のSQL */
    private static final String SELECT_EMPLOYEE_SQL =
            "SELECT * FROM employee where gender = ?";

    /** JdbcCursorItemReader */
    @Bean
    @StepScope
    public JdbcCursorItemReader<Employee> jdbcCursorReader() {
        // クエリーに渡すパラメーター
        Object[] params = new Object[] {1};
        
        // RowMapper
        RowMapper<Employee> rowMapper = 
                new BeanPropertyRowMapper<>(Employee.class);
        
        return new JdbcCursorItemReaderBuilder<Employee>() // Builder生成
            .dataSource(this.dataSource) // DataSourceのセット
            .name("jdbcCursorItemReader") // 名前のセット
            .sql(SELECT_EMPLOYEE_SQL) // SQLのセット
            .queryArguments(params) // パラメーター
            .rowMapper(rowMapper) // rowMapperのセット
            .build(); // readerの生成
    }

    /** Stepの生成 */
    @Bean
    public Step exportJdbcCursorStep() throws Exception {
        return this.stepBuilderFactory.get("ExportJdbcCursorStep")
            .<Employee, Employee>chunk(10)
            .reader(jdbcCursorReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
    }

    /** Jobの生成 */
    @Bean("JdbcCursorJob")
    public Job exportJdbcCursorJob() throws Exception {
        return this.jobBuilderFactory.get("ExportJdbcCursorJob")
            .incrementer(new RunIdIncrementer())
            .start(exportJdbcCursorStep())
            .build();
    }
}
