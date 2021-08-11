package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class HelloReader implements ItemReader<String> {

    // 出力用文字列
    private String[] input = {"Hello", "World", "hoge", "fuga", null, "The World"};
    private int index = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException,
            NonTransientResourceException {

        // 配列の文字列を取得
        String message = input[index++];
        log.info("Read:{}", message);

        return message;
    }
}
