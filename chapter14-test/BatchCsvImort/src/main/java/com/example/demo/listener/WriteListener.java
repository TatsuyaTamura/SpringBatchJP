package com.example.demo.listener;

import java.util.List;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;
import com.example.demo.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WriteListener implements ItemWriteListener<Employee> {

    @Override
    public void beforeWrite(List<? extends Employee> items) {
        // Do nothing
    }

    @Override
    public void afterWrite(List<? extends Employee> items) {
        log.debug("AfterWrite: count={}", items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Employee> items) {
        log.error("WriteError: errorMessage={}", exception.getMessage(), exception);
    }
}
