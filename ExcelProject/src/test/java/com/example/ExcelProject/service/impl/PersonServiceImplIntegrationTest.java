package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonServiceImplIntegrationTest {

    @Autowired
    private PersonService personService;

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
    personDto.setJob(jobDto);

    // PersonDto'yu kaydet
    PersonDto result = personService.save(personDto);
    assertTrue(result.getId() > 0L);
}
    @Test
    void testSaveOnlyPersons(){
        // PersonDto nesnesini oluştur
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        PersonDto result = personService.save(personDto);
        System.out.println(result);
        assertTrue(result.getId() > 0L);
    }
    @Test
    void testSaveJob(){

        JobDto jobDto = new JobDto();
        jobDto.setDepartmentName("Test-DepartmentName");
        jobDto.setDepartmentCode("Test-Code");
        JobDto result = personService.saveJob(jobDto);
        assertTrue(result.getId() > 0L);
    }

//    @Test
//    void testGetAll(){
//        testSave();
//        List<PersonDto> allPersons = personService.getAll();
//        // Geri dönen liste boş olmamalı
//        assertTrue(!allPersons.isEmpty());
//
//        // Geri dönen listedeki kişi sayısı 1 olmalı
//        assertEquals(1, allPersons.size());
//    }

    @Test
    void testUpdatePerson(){
        JobDto jobDto = new JobDto();

        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        JobDto savedJobDto = personService.saveJob(jobDto);
        jobDto.setId(savedJobDto.getId());

        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        personDto.setJob(jobDto);

        PersonDto savedPersonDto = personService.save(personDto);


        PersonDto updatedPersonDto = new PersonDto();
        updatedPersonDto.setName("Updated-Name");
        updatedPersonDto.setSurname("Updated-Surname");
        updatedPersonDto.setAge(35);
        updatedPersonDto.setJob(jobDto);

        // Kişiyi güncelle
        PersonDto result = personService.updatePerson(savedPersonDto.getId(), updatedPersonDto);

        // Güncellenen kişinin bilgileri kontrol edilir
        assertNotNull(result);
        assertEquals(savedPersonDto.getId(), result.getId());
        assertEquals(updatedPersonDto.getName(), result.getName());
        assertEquals(updatedPersonDto.getSurname(), result.getSurname());
        assertEquals(updatedPersonDto.getAge(), result.getAge());


    }
    @Test
    void testDelete(){
        JobDto jobDto = new JobDto();

        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        JobDto savedJobDto = personService.saveJob(jobDto);

        // Person kaydı oluştur
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        personDto.setJob(jobDto);

        jobDto.setId(savedJobDto.getId());

        personDto.setJob(jobDto);
        //kayıt et
        PersonDto savedPersonDto = personService.save(personDto);
        personService.delete(savedPersonDto.getId());

        PersonDto deletedPersonDto = personService.getPersonById(personDto.getId());

        assertNull(deletedPersonDto);

    }
    @Test
    void testGetAll() {
        // JOB kaydı oluştur
        JobDto jobDto = new JobDto();

        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        JobDto savedJobDto = personService.saveJob(jobDto);

        // Person kaydı oluştur
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        personDto.setJob(jobDto);

        jobDto.setId(savedJobDto.getId());

        personDto.setJob(jobDto);
        //kayıt et
        PersonDto savedPersonDto = personService.save(personDto);

        // getAll metodunu çağır
        List<PersonDto> allPersons = personService.getAll();

        System.out.println(allPersons);
        // Geri dönen liste boş olmamalı
        assertTrue(!allPersons.isEmpty());

        assertEquals(1, allPersons.size());

        // Geri dönen kişinin bilgileri doğru olmalı
        PersonDto retrievedPersonDto = allPersons.get(0);
        assertEquals(savedPersonDto.getId(), retrievedPersonDto.getId());
        assertEquals(savedPersonDto.getName(), retrievedPersonDto.getName());
        assertEquals(savedPersonDto.getSurname(), retrievedPersonDto.getSurname());
        assertEquals(savedPersonDto.getAge(), retrievedPersonDto.getAge());

        // Geri dönen kişinin iş bilgileri doğru olmalı
        JobDto retrievedJobDto = retrievedPersonDto.getJob();
        assertNotNull(retrievedJobDto);
        assertEquals(jobDto.getId(), retrievedJobDto.getId());
        assertEquals(jobDto.getDepartmentCode(), retrievedJobDto.getDepartmentCode());
        assertEquals(jobDto.getDepartmentName(), retrievedJobDto.getDepartmentName());
    }
}
