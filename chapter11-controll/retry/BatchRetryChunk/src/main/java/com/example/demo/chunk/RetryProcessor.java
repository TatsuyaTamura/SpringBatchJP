package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class RetryProcessor implements ItemProcessor<String, String> {

    @Value("${retry.num}")
    private Integer retryNum;

    private int count = 1;

    @Override
    public String process(String item) throws Exception {

        if ("World".equals(item) && (retryNum > count)) {
            count++;
            throw new Exception("Retry Test");
        }

        // 文字列の加工
        item = item + "★";
        log.info("Processor:{}", item);

        return item;
    }
}
