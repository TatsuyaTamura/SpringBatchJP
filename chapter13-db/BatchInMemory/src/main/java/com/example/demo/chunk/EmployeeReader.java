package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import com.example.demo.domain.Employee;

@Component
@StepScope
public class EmployeeReader implements ItemReader<Employee> {

    private int count = 1;

    private Employee employee = new Employee(1, "user", 20, 1);

    @Override
    public Employee read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException {
        if (count == 1) {
            count++;
            return employee;
        } else {
            return null;
        }
    }
}
