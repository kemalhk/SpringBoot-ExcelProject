package com.example.ExcelProject.repository;

import com.example.ExcelProject.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    //Job findByDepartmentCode(String departmentCode);
}
