package com.example.demo.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Employee {
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private Integer gender;
    @Transient
    private String genderString;

    /** 性別の数値を文字列に変換 */
    public void convertGenderIntToString() {
        // 数値を文字列に変換
        if (gender == 1) {
            genderString = "男性";
        } else if (gender == 2) {
            genderString = "女性";
        } else {
            String errorMsg = "Gender is invalid:" + gender;
            throw new IllegalStateException(errorMsg);
        }
    }
}
