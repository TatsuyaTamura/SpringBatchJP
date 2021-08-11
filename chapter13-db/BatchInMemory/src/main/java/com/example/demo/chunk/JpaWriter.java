package com.example.demo.chunk;

import java.util.List;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.domain.Employee;
import com.example.demo.repository.EmployeeRepository;

@Component("JpaWriter")
@StepScope
public class JpaWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepository repository;

    @Override
    @Transactional
    public void write(List<? extends Employee> items) throws Exception {
        repository.saveAllAndFlush(items);
    }
}
