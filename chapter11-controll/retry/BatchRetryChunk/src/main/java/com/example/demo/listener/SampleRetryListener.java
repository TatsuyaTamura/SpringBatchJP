package com.example.demo.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SampleRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
            RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
            Throwable throwable) {
        // Do nothing
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
            Throwable throwable) {
        log.info("Retry OnError: count={}, message={}", context.getRetryCount(),
                throwable.getMessage());
    }
}
