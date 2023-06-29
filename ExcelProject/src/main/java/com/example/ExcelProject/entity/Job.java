package com.example.ExcelProject.entity;

import lombok.*;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "job")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,name = "departmentName")
    private String departmentName;

    @OneToOne(mappedBy = "job")
    private Person person;
    @Column(length = 50,name = "departmentCode")
    private String departmentCode;

}
