package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.entity.Job;
import com.example.ExcelProject.repository.JobRepository;
import com.example.ExcelProject.repository.PersonRepository;
import com.example.ExcelProject.service.PersonService;
import com.example.ExcelProject.util.ExcelUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;


    private final JobRepository jobRepository;
    @Override
    public PersonDto getPersonById(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            PersonDto personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setName(person.getName());
            personDto.setSurname(person.getSurname());
            personDto.setAge(person.getAge());

            Job job = person.getJob();
            if (job != null) {
                JobDto jobDto = new JobDto();
                jobDto.setId(job.getId());
                jobDto.setDepartmentName(job.getDepartmentName());
                jobDto.setDepartmentCode(job.getDepartmentCode());
                personDto.setJob(jobDto);
            }

            return personDto;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public PersonDto save(PersonDto personDto) {
        Assert.notNull(personDto.getName(), "Ad alanı zorunludur.");
        Assert.notNull(personDto.getSurname(), "Soyad alanı zorunludur.");

        Person person = new Person();
        person.setName(personDto.getName());
        person.setSurname(personDto.getSurname());
        person.setAge(personDto.getAge());


        if (personDto.getJob() != null) {
            Job job = new Job();
            job.setId(personDto.getJob().getId());
            job.setDepartmentName(personDto.getJob().getDepartmentName());
            job.setDepartmentCode(personDto.getJob().getDepartmentCode());
            person.setJob(job);
        }

        final Person personDb = personRepository.save(person);
        personDto.setId(personDb.getId());


        return personDto;

    }


    @Override
    public List<PersonDto> getAll(){
        List<Person> persons = personRepository.findAll();
        List<PersonDto> personDtos = new ArrayList<>();
        persons.forEach(it ->{
            PersonDto personDto=new PersonDto();
            personDto.setId(it.getId());
            personDto.setName(it.getName());
            personDto.setSurname(it.getSurname());
            personDto.setAge(it.getAge());

            Job job = it.getJob();
            if (job != null) {
                JobDto jobDto = new JobDto();
                jobDto.setId(job.getId());
                jobDto.setDepartmentName(job.getDepartmentName());
                jobDto.setDepartmentCode(job.getDepartmentCode());
                personDto.setJob(jobDto);
            }
            personDtos.add(personDto);
        });
        return personDtos;
    }


    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public PersonDto updatePerson(Long id, PersonDto personDto) {

        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            person.setName(personDto.getName());
            person.setSurname(personDto.getSurname());
            person.setAge(personDto.getAge());

            JobDto jobDto = personDto.getJob();
            if (jobDto != null) {
                Job job = new Job();
                job.setId(jobDto.getId());
                job.setDepartmentName(jobDto.getDepartmentName());
                job.setDepartmentCode(jobDto.getDepartmentCode());
                person.setJob(job);
            }
            else {
                // İş nesnesi null olarak ayarlanırsa, kişinin iş bilgisi de silinebilir
                person.setJob(null);
            }

            // Kişinin güncellenmiş hali veritabanına kaydedilir
            personRepository.save(person);
            return personDto;
        }
        else
        {
            // Kişi bulunamazsa veya güncelleme başarısız olursa null döndür
            return null;
        }
    }

    @Override
    public Page<PersonDto> getAll(Pageable pageable) {
        Page<Person> persons = personRepository.findAll(pageable);

        return persons.map(person -> {
            PersonDto personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setName(person.getName());
            personDto.setSurname(person.getSurname());
            personDto.setAge(person.getAge());
            return personDto;
        });
    }


    @Override
    public List<PersonDto> readPersonsFromExcel(InputStream inputStream) throws Exception {
        List<PersonDto> persons = ExcelUtils.readPersonsFromExcel(inputStream);
        for (PersonDto personDto : persons) {
            Person person = new Person();
            person.setName(personDto.getName());
            person.setSurname(personDto.getSurname());
            person.setAge(personDto.getAge());

            if (personDto.getJob() != null) {
                Job job = new Job();
                job.setId(personDto.getJob().getId());
                job.setDepartmentName(personDto.getJob().getDepartmentName());
                job.setDepartmentCode(personDto.getJob().getDepartmentCode());

                // Mevcut departmanı kontrol etmek ve ID'sini bulmak için gerekli işlemler
                Job existingJob = jobRepository.findByDepartmentCode(job.getDepartmentCode());
                if (existingJob != null) {
                    job.setId(existingJob.getId());
                }
                person.setJob(job);
            }
            personRepository.save(person);
        }
        return persons;

    }



    @Override
    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Person> persons = personRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Persons Info");
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Person ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Age");
        headerRow.createCell(4).setCellValue("Department Name");
        headerRow.createCell(5).setCellValue("Department Code");
        headerRow.createCell(6).setCellValue("Department ID");

        int rowIndex = 1;
        for (Person person : persons) {
            HSSFRow dataRow = sheet.createRow(rowIndex);
            dataRow.createCell(0).setCellValue(person.getId());
            dataRow.createCell(1).setCellValue(person.getName());
            dataRow.createCell(2).setCellValue(person.getSurname());
            dataRow.createCell(3).setCellValue(person.getAge());
            dataRow.createCell(4).setCellValue(person.getJob().getDepartmentName());
            dataRow.createCell(5).setCellValue(person.getJob().getDepartmentCode());
            dataRow.createCell(6).setCellValue(person.getJob().getId());
            rowIndex++;
        }

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=persons.xls";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }



}
