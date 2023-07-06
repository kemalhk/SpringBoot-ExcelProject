package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PersonServiceImplIntegrationTest {

    @Autowired
    private PersonService personService;

//    @Test
//    void testSave() {
//        PersonDto personDto = new PersonDto();
//        personDto.setName("Test-Name");
//        personDto.setSurname("Test-Surname");
//        personDto.setAge(1);
//
//        JobDto jobDto = new JobDto();
//        jobDto.setId(1L);
//        jobDto.setDepartmentCode("Test-Code");
//        jobDto.setDepartmentName("Test-DepartmentName");
//        JobDto resultJob = personService.saveJob(jobDto);
//        jobDto.setId(resultJob.getId()); // jobDto'nun ID'sini güncelle
//
//        personDto.setJob(jobDto);
//
//        PersonDto result = personService.save(personDto);
//
//        assertTrue(result.getId() > 0L);
//
//    }
@Test
void testSave() {
     // JOB kaydı oluştur
    JobDto jobDto = new JobDto();

    jobDto.setDepartmentCode("Test-Code");
    jobDto.setDepartmentName("Test-DepartmentName");
    JobDto savedJobDto = personService.saveJob(jobDto);

    // PersonDto nesnesini oluştur
    PersonDto personDto = new PersonDto();
    personDto.setName("Test-Name");
    personDto.setSurname("Test-Surname");
    personDto.setAge(1);

    // PersonDto'yu kaydet
    PersonDto result = personService.save(personDto);
    assertTrue(result.getId() > 0L);
}
    @Test
    void testSaveJob(){

        JobDto jobDto = new JobDto();
        jobDto.setDepartmentName("Test-DepartmentName");
        jobDto.setDepartmentCode("Test-Code");
        JobDto result = personService.saveJob(jobDto);
    }
}
