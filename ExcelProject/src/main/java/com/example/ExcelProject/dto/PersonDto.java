package com.example.ExcelProject.dto;

import com.example.ExcelProject.entity.Job;
import lombok.Data;
import java.util.List;

@Data
public class PersonDto {

        private Long id;
        private String name;
        private String surname;
        private Integer age;
        private JobDto job;


}
