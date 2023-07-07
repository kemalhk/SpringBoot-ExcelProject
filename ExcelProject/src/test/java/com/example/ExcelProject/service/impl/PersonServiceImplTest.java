package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.repository.JobRepository;
import com.example.ExcelProject.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {
    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private JobRepository jobRepository;


    @Test
    void getPersonById() {
    }

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
        // metodunun personMock nesnesini döndürmesi için yapılandırma yapılır
        Mockito.when(personRepository.save(ArgumentMatchers.any(Person.class))).thenReturn(personMock);

        PersonDto result = personService.save(personDto);

        // Sonuçların doğruluğu assert edilir
        assertEquals(result.getName(),personDto.getName());
        assertEquals(result.getId(),1L);


    }

    @Test
    void getAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void updatePerson() {
    }

    @Test
    void testGetAll() {
    }



}