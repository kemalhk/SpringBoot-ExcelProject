package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.repository.JobRepository;
import com.example.ExcelProject.repository.PersonRepository;
import com.example.ExcelProject.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonServiceImplIntegrationTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private JobRepository jobRepository;

    @Test
    void testSave() {
        //test verileri
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1L);
        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
        personDto.setJob(jobDto);

        // Person sınıfı için mock oluşturulur
        Person personMock = Mockito.mock(Person.class);
        // personMock nesnesi üzerindeki getId() metodu için beklenen değer ayarlanır
        Mockito.when(personMock.getId()).thenReturn(1L);
        // save metodunun personMock nesnesini döndürmesi
        Mockito.when(personRepository.save(ArgumentMatchers.any(Person.class))).thenReturn(personMock);

        PersonDto result = personService.save(personDto);

        // Sonuçların doğruluğu assert edilir
        assertEquals(result.getName(),personDto.getName());
        assertEquals(result.getId(),1L);

    }
    @Test
    void testSaveOnlyPersons(){
        // veri oluştur
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);

        // Person sınıfı için mock oluşturulur
        Person personMock = Mockito.mock(Person.class);
        // personMock nesnesi üzerindeki getId() metodu için beklenen değer ayarlanır
        Mockito.when(personMock.getId()).thenReturn(1L);

        // save metodunun personMock nesnesini döndürmesi
        Mockito.when(personRepository.save(ArgumentMatchers.any(Person.class))).thenReturn(personMock);
        PersonDto result = personService.save(personDto);

        // Sonuçların doğruluğu assert edilir
        assertEquals(result.getName(),personDto.getName());
        assertEquals(result.getSurname(),personDto.getSurname());
        assertEquals(result.getAge(),personDto.getAge());
        assertEquals(result.getId(),1L);
    }
    @Test
    void testSaveJob(){

        JobDto jobDto = new JobDto();
        jobDto.setDepartmentName("Test-DepartmentName");
        jobDto.setDepartmentCode("Test-Code");

        Job jobMock = Mockito.mock(Job.class);
        //jobmock beklenen değer atama
        Mockito.when(jobMock.getId()).thenReturn(1L);

        // save methodunun mock dönmesi
        Mockito.when(jobRepository.save(ArgumentMatchers.any(Job.class))).thenReturn(jobMock);
        JobDto result = personService.saveJob(jobDto);

        // kontrol
        assertTrue(result.getId() > 0L);
        assertEquals(result.getDepartmentCode(),jobDto.getDepartmentCode());
        assertEquals(result.getDepartmentName(),jobDto.getDepartmentName());

    }

    @Test
    void testGetAll() {
        // Test verileri
        Person person = new Person();
        person.setName("Test-Name");
        person.setSurname("Test-Surname");
        person.setAge(1);

        Job job = new Job();
        job.setDepartmentCode("Test-Code");
        job.setDepartmentName("Test-DepartmentName");


        // Create a mock of PersonRepository
        PersonRepository personRepository = mock(PersonRepository.class);

        // Mock repository response
        when(personRepository.findAll()).thenReturn(Collections.singletonList(person));

        // Call the method
        List<Person> persons = personRepository.findAll();

        // Assert the result
        assertEquals(1, persons.size());
        assertEquals("Test-Name", persons.get(0).getName());
        assertEquals("Test-Surname", persons.get(0).getSurname());
        assertEquals(1, persons.get(0).getAge());
    }


    @Test
    void testUpdatePerson() {
        //test verileri
        PersonDto personDto = new PersonDto();
        personDto.setName("Test-Name");
        personDto.setSurname("Test-Surname");
        personDto.setAge(1);
        JobDto jobDto = new JobDto();
        jobDto.setId(1L);
        jobDto.setDepartmentCode("Test-Code");
        jobDto.setDepartmentName("Test-DepartmentName");
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



}
