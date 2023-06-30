package com.example.ExcelProject.repository;

import com.example.ExcelProject.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job,Long> {
}
