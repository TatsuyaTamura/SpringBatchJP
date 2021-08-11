package com.example.demo.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;
import com.example.demo.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class EmployeeSkipListener implements SkipListener<Employee, Employee> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn("Skip by Read Error: errorMessage={}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Employee item, Throwable t) {
        log.warn("Skip by Write Error: item={}", item);
    }

    @Override
    public void onSkipInProcess(Employee item, Throwable t) {
        log.warn("Skip by Process Error: item={}", item);
    }
}
