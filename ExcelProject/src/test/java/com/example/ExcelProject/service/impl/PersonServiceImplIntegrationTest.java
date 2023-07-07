package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.repository.JobRepository;
import com.example.ExcelProject.repository.PersonRepository;
import com.example.ExcelProject.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonServiceImplIntegrationTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JobRepository jobRepository;

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
    void testUpdatePerson() {
        // Create and save a job
        JobDto jobDto = new JobDto();
        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        JobDto savedJobDto = personService.saveJob(jobDto);
        jobDto.setId(savedJobDto.getId());

        // Create and save a person
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        personDto.setJob(jobDto);
        PersonDto savedPersonDto = personService.save(personDto);

        // Create an updated person
        PersonDto updatedPersonDto = new PersonDto();
        updatedPersonDto.setId(savedPersonDto.getId());
        updatedPersonDto.setName("Updated-Name");
        updatedPersonDto.setSurname("Updated-Surname");
        updatedPersonDto.setAge(35);
        updatedPersonDto.setJob(jobDto);

        // Update the person
        PersonDto result = personService.updatePerson(savedPersonDto.getId(), updatedPersonDto);

        // Assert the updated person's information
        assertNotNull(result);
        System.out.println(result);
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

    @Test
    void testReadPersonsFromExcel() throws Exception {
        // Create and save a job
        JobDto jobDto = new JobDto();
        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        JobDto savedJobDto = personService.saveJob(jobDto);

        String filePath = "C:\\Users\\kemal.kara\\Downloads\\persons.xlsx";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        List<PersonDto> result = personService.readPersonsFromExcel(fileInputStream);

        assertNotNull(result);
        assertEquals(1, result.size());

        for (PersonDto personDto : result) {
            personDto.getId();
            Optional<Person> optionalPerson = personRepository.findById(personDto.getId());

            assertTrue(optionalPerson.isPresent());
            Person savedPerson = optionalPerson.get();

            assertEquals(personDto.getName(), savedPerson.getName());
            assertEquals(personDto.getSurname(), savedPerson.getSurname());
            assertEquals(personDto.getAge(), savedPerson.getAge());

            JobDto personJobDto = personDto.getJob();
            assertNotNull(personJobDto);
            personDto.getId();
            if (savedJobDto != null) {
                Job existingJob = jobRepository.findByDepartmentCode(savedJobDto.getDepartmentCode());
                assertNotNull(existingJob);

                savedPerson.setJob(existingJob);
            } else {
                savedPerson.setJob(null);
            }

            personRepository.save(savedPerson);
        }
    }
}
