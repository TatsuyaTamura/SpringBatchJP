package com.example.demo.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import io.micrometer.core.instrument.util.StringUtils;

public class RequiredValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters)
            throws JobParametersInvalidException {

        // パラメーター取得
        String key = "require1";
        String require1 = parameters.getString(key);

        // 必須入力チェック
        if (StringUtils.isEmpty(require1)) {
            String errorMsg = "Not enterd: " + key;
            throw new JobParametersInvalidException(errorMsg);
        }
    }
}