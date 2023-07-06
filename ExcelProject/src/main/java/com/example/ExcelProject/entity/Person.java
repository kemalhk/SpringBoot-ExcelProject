package com.example.ExcelProject.entity;

import lombok.*;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(length = 50,name = "name")
    private String name;

    @Column(length = 50,name = "surname")
    private String surname;

   private Integer age;

    @OneToOne
    @JoinColumn(name = "job_id")
    private Job job;

}
