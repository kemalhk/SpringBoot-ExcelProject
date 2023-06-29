package com.example.ExcelProject.repository;

import com.example.ExcelProject.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {


}
