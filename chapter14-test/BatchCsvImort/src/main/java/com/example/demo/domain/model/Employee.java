package com.example.demo.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Employee {
    @NotNull
    @Id
    private Integer id;
    @NotNull
    private String name;
    @Min(20)
    private Integer age;
    private Integer gender;
    @Transient
    private String genderString;

    /** 性別の文字列を数値に変換 */
    public void convertGenderStringToInt() {
        // 文字列を数値に変換
        if ("男性".equals(genderString)) {
            gender = 1;
        } else if ("女性".equals(genderString)) {
            gender = 2;
        } else {
            String errorMsg = "Gender string is invalid:" + genderString;
            throw new IllegalStateException(errorMsg);
        }
    }
}
