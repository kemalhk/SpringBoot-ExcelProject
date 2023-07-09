package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.repository.JobRepository;
import com.example.ExcelProject.repository.PersonRepository;
import com.example.ExcelProject.service.PersonService;
import com.example.ExcelProject.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
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
    @DisplayName("TestSave")
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
        assertEquals(result.getName(), personDto.getName());
        assertEquals(result.getId(), 1L);

    }

    @Test
    @DisplayName(" TestSaveOnlyPersons")
    void testSaveOnlyPersons() {
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
        assertEquals(result.getName(), personDto.getName());
        assertEquals(result.getSurname(), personDto.getSurname());
        assertEquals(result.getAge(), personDto.getAge());
        assertEquals(result.getId(), 1L);
    }

    @Test
    @DisplayName(" testSaveJob")
    void testSaveJob() {

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
        assertEquals(result.getDepartmentCode(), jobDto.getDepartmentCode());
        assertEquals(result.getDepartmentName(), jobDto.getDepartmentName());

    }

    @Test
    @DisplayName("testGetAll")
    void testGetAll() {
        // Test verileri
        Person person = new Person();
        person.setName("Test-Name");
        person.setSurname("Test-Surname");
        person.setAge(1);

        Job job = new Job();
        job.setId(1L);
        job.setDepartmentCode("Test-Code");
        job.setDepartmentName("Test-DepartmentName");
        person.setJob(job);

        // Create a mock of PersonRepository
        PersonRepository personRepository = mock(PersonRepository.class);

        // Mock repository response
        when(personRepository.findAll()).thenReturn(Collections.singletonList(person));

        // Call the method
        List<Person> persons = personRepository.findAll();


        assertEquals(1, persons.size());
        assertEquals("Test-Name", persons.get(0).getName());
        assertEquals("Test-Surname", persons.get(0).getSurname());
        assertEquals(1, persons.get(0).getAge());
        assertEquals(1L, persons.get(0).getJob().getId());
        assertEquals("Test-Code", persons.get(0).getJob().getDepartmentCode());
        assertEquals("Test-DepartmentName", persons.get(0).getJob().getDepartmentName());

    }


    @Test
    @DisplayName("Update ")
    void testUpdate() {

        // Job nesnesi oluştur
        Job job = new Job();
        job.setId(1L);
        job.setDepartmentName("Test-DepartmentName");
        job.setDepartmentCode("Test-DepartmentCode");

        // Person nesnesi oluştur
        Person person = new Person();
        person.setId(1L);
        person.setName("Test-Name");
        person.setSurname("Test-Surname");
        person.setAge(30);
        person.setJob(job);

        // Update için PersonDto nesnesi oluştur
        PersonDto personDto = new PersonDto();
        personDto.setId(1L);
        personDto.setName("Test-Name-Update");
        personDto.setSurname("Test-Surname-Update");
        personDto.setAge(30);

        // Update için JobDto nesnesi oluştur
        JobDto jobDto = new JobDto();
        jobDto.setId(2L);
        jobDto.setDepartmentName("Test-DepartmentName-Update");
        jobDto.setDepartmentCode("Test-DepartmentCode-Update");
        personDto.setJob(jobDto);


        // personRepository.save() metodunun davranışı taklit edilir
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // personRepository.findById() metodunun davranışı taklit edilir
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // personService'nin updatePerson() metoduna 1L kimlik ve personDto parametreleriyle çağrı yapılır
        PersonDto updatedPersonDto = personService.updatePerson(1L, personDto);

        // updatedPersonDto'nun null olmadığı doğrulanır
        assertNotNull(updatedPersonDto);

        // updatedPersonDto'nun kimliğinin, orijinal personDto ile aynı olduğu doğrulanır
        assertEquals(personDto.getId(), updatedPersonDto.getId());

        // updatedPersonDto'nun adının, orijinal personDto ile aynı olduğu doğrulanır
        assertEquals(personDto.getName(), updatedPersonDto.getName());

        // updatedPersonDto'nun soyadının, orijinal personDto ile aynı olduğu doğrulanır
        assertEquals(personDto.getSurname(), updatedPersonDto.getSurname());

        // updatedPersonDto'nun yaşının, orijinal personDto ile aynı olduğu doğrulanır
        assertEquals(personDto.getAge(), updatedPersonDto.getAge());

        // updatedPersonDto'nun işinin, orijinal personDto ile aynı olduğu doğrulanır
        assertEquals(personDto.getJob(), updatedPersonDto.getJob());

        // personRepository.save() metodunun, doğru Person nesnesiyle bir kez çağrıldığı doğrulanır
        Mockito.verify(personRepository, Mockito.times(1)).save(person);

        // personRepository.findById() metodunun, doğru kimlik parametresiyle bir kez çağrıldığı doğrulanır
        Mockito.verify(personRepository, Mockito.times(1)).findById(1L);

    }

    @Test
    @DisplayName("Delete Person")
    void testDeletePersonById() {
        Long id = 1L;
        Person person = new Person();
        person.setId(id);

        // Kişi varsa davranışı tanımlanıyor
        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        personService.delete(id);

        // Kişinin silindiği doğrulanıyor
        Mockito.verify(personRepository, Mockito.times(1)).deleteById(id);

        // Kişi yoksa davranışı tanımlanıyor
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Kişinin olmadığı durumu test ediyoruz ve bir istisna fırlatılması gerekiyor
        assertThrows(ResponseStatusException.class, () -> {
            personService.delete(id);
        });

        Mockito.verify(personRepository, Mockito.times(1)).deleteById(id);

    }

    @Test
    @DisplayName("Real Data Import")
    void testReadPersonsFromRealExcel() {
        try {
            InputStream inputStream = new FileInputStream("C:/Users/kemal/Downloads/testData.xlsx");


            JobDto jobDto = new JobDto();

            jobDto.setDepartmentName("Test-DepartmentName");
            jobDto.setDepartmentCode("Test-DepartmentCode");

            PersonDto personDto = new PersonDto();
            personDto.setName("Test-Name");
            personDto.setSurname("Test-Surname");
            personDto.setAge(25);
            personDto.setJob(jobDto);


            Person person = new Person();
            person.setId(1L);
            person.setName("Test-Name");
            person.setSurname("Test-Surname");
            person.setAge(25);
            person.setJob(new Job(1L, "Test-DepartmentName", "Test-DepartmentCode"));


            // Bir taklit Job oluştur
            Job job = new Job(1L, "Test-DepartmentName", "Test-DepartmentCode");

            // jobRepository.findByDepartmentCode çağrıldığında taklit Job nesnesini döndürmek için jobRepository'yi taklit et
            when(jobRepository.findByDepartmentCode(jobDto.getDepartmentCode())).thenReturn(job);

            when(personRepository.save(any(Person.class))).thenReturn(person);

            List<PersonDto> result = personService.readPersonsFromExcel(inputStream);

            // Sonucun boş olmadığını doğrula
            assertNotNull(result);
            // jobRepository.findByDepartmentCode yönteminin taklit JobDto'dan alınan departman kodu ile bir kez çağrıldığını doğrula
            Mockito.verify(jobRepository, Mockito.times(1)).findByDepartmentCode(jobDto.getDepartmentCode());

            // personRepository.save yönteminin herhangi bir Person nesnesi ile bir kez çağrıldığını doğrula
            Mockito.verify(personRepository, Mockito.times(1)).save(any(Person.class));

            System.out.println(result);
            System.out.println(personDto);
            // Sonucun  PersonDto'yu içerdiğini doğrula
            assertTrue(result.contains(personDto));

        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("ReadPersonsFromExcel")
    public void testReadPersonsFromExcel() throws Exception {

        // Bir taklit JobDto oluştur
        JobDto jobDto = new JobDto();

        jobDto.setDepartmentName("Test-DepartmentName");
        jobDto.setDepartmentCode("Test-DepartmentCode");

        // Test verileri
        PersonDto personDto1 = new PersonDto();
        personDto1.setName("Test-Name");
        personDto1.setSurname("Test-Surname");
        personDto1.setAge(25);
        personDto1.setJob(jobDto);

        PersonDto personDto2 = new PersonDto();
        personDto2.setName("Test-Name-2");
        personDto2.setSurname("Test-Surname-2");
        personDto2.setAge(25);
        personDto2.setJob(jobDto);


        List<PersonDto> personsDto = new ArrayList<>();
        personsDto.add(personDto1);
        personsDto.add(personDto2);

        // ExcelUtils'den dönecek sonuçlar
        List<PersonDto> excelUtilsResult = new ArrayList<>();
        excelUtilsResult.add(personDto1);
        excelUtilsResult.add(personDto2);


        // Excel dosyasını oluştur
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        String[] header = {"Name", "Surname", "Age", "DepartmentName", "DepartmentCode"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        String[] data1 = {"Test-Name", "Test-Surname", "25", "Test-DepartmentName", "Test-DepartmentCode"};
        String[] data2 = {"Test-Name-2", "Test-Surname-2", "25", "Test-DepartmentName", "Test-DepartmentCode"};

        Row row1 = sheet.createRow(1);
        for (int i = 0; i < data1.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(data1[i]);
        }

        Row row2 = sheet.createRow(2);
        for (int i = 0; i < data2.length; i++) {
            Cell cell = row2.createCell(i);
            cell.setCellValue(data2[i]);
        }

        // Workbook'u InputStream'e dönüştür
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Workbook'u kapat
        workbook.close();

        // Mocklama işlemleri
        // personService'i mock olarak tanımla
        PersonService personServiceMock = Mockito.mock(PersonService.class);
        PersonServiceImpl personServiceImpl = Mockito.mock(PersonServiceImpl.class);
        when(personServiceMock.readPersonsFromExcel(any(InputStream.class))).thenReturn(excelUtilsResult);
        when(jobRepository.findByDepartmentCode("Test-DepartmentCode")).thenReturn(new Job(1L, "Test-DepartmentName", "Test-DepartmentCode"));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            person.setId(1L); // Simüle edilmiş bir ID ataması
            return person;
        });

        // Metodu çağır
        List<PersonDto> result = personService.readPersonsFromExcel(inputStream);

        // Sonuçları kontrol et
        assertEquals(personsDto.size(), result.size());
        for (int i = 0; i < personsDto.size(); i++) {
            PersonDto expectedPersonDto = personsDto.get(i);
            PersonDto actualPersonDto = result.get(i);

            assertEquals(expectedPersonDto.getName(), actualPersonDto.getName());
            assertEquals(expectedPersonDto.getSurname(), actualPersonDto.getSurname());
            assertEquals(expectedPersonDto.getAge(), actualPersonDto.getAge());
            assertEquals(expectedPersonDto.getJob(), actualPersonDto.getJob());
        }

        // Metod çağrılarını ve parametrelerini kontrol et
        //verify(personServiceMock, times(1)).readPersonsFromExcel(any(InputStream.class));
        //verify(personServiceMock, times(1)).readPersonsFromExcel(inputStream);
        //verify(personService,times(1)).readPersonsFromExcel(any(InputStream.class));
        //verify(personService, times(1)).readPersonsFromExcel(inputStream);
        verify(jobRepository, times(2)).findByDepartmentCode("Test-DepartmentCode");
        verify(personRepository, times(personsDto.size())).save(any(Person.class));
    }

    @Test
    @DisplayName("testGenerateExcel")
    void testGenerateExcel() throws Exception{

        List<Person> persons = new ArrayList<>();

        Person person1 = new Person();
        person1.setId(1L);
        person1.setName("John");
        person1.setSurname("Doe");
        person1.setAge(25);
        Job job1 = new Job();
        job1.setId(1L);
        job1.setDepartmentName("IT");
        job1.setDepartmentCode("IT-001");
        person1.setJob(job1);

        Person person2 = new Person();
        person2.setId(2L);
        person2.setName("Jane");
        person2.setSurname("Smith");
        person2.setAge(30);

        Job job2 = new Job();
        job2.setId(2L);
        job2.setDepartmentName("HR");
        job2.setDepartmentCode("HR-001");
        person2.setJob(job2);

        persons.add(person1);
        persons.add(person2);

        // Mock nesneleri oluştur
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);
        Workbook workbook = Mockito.mock(Workbook.class);


        // PersonRepository findAll() işlemini taklit et
        when(personRepository.findAll()).thenReturn(persons);


        when(response.getOutputStream()).thenReturn(outputStream);

        // Metodu çağır
        personService.generateExcel(response);

        // Workbook'u doğrula
        verify(response, times(1)).setContentType("application/octet-stream");
        verify(response, times(1)).setHeader("Content-Disposition", "attachment; filename=persons.xls");
        verify(personRepository, times(1)).findAll();
//        verify(workbook, times(1)).write(outputStream);
//        verify(workbook, times(1)).close();
//        verify(outputStream, times(1)).close();

    }

    @Test
    @DisplayName("testGetPersonById")
    void testGetPersonById() {
        Long id = 1L;
        Person person = new Person();
        person.setId(id);
        person.setName("Test-Name");
        person.setSurname("Test-Surname");
        person.setAge(25);

        Job job = new Job();
        job.setId(1L);
        job.setDepartmentName("Test-DepartmentName");
        job.setDepartmentCode("Test-DepartmentCode");

        person.setJob(job);

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));

        PersonDto result = personService.getPersonById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(person.getName(), result.getName());
        assertEquals(person.getSurname(), result.getSurname());
        assertEquals(person.getAge(), result.getAge());

        JobDto jobDto = result.getJob();
        assertNotNull(jobDto);
        assertEquals(job.getId(), jobDto.getId());
        assertEquals(job.getDepartmentName(), jobDto.getDepartmentName());
        assertEquals(job.getDepartmentCode(), jobDto.getDepartmentCode());

        Mockito.verify(personRepository, Mockito.times(1)).findById(id);
    }

}
