package com.example.ExcelProject.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.ExcelProject.dto.JobDto;
import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.service.PersonService;
import com.example.ExcelProject.util.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor

public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons(){
        return ResponseEntity.ok(personService.getAll());
    }
    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto){
        return ResponseEntity.ok(personService.save(personDto));
    }

    @PostMapping("/job")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto){
        return ResponseEntity.ok(personService.saveJob(jobDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id){
        PersonDto personDto = personService.getPersonById(id);
        if(personDto !=null)
        {
            return  ResponseEntity.ok(personDto);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) throws Exception {
        PersonDto personDto = personService.getPersonById(id);
        if (personDto != null) {
            personService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable Long id, @RequestBody PersonDto updatedPerson) {
        PersonDto personDto = personService.getPersonById(id);
        if (personDto != null) {
            updatedPerson.setId(id);
            PersonDto updated = personService.updatePerson(id, updatedPerson);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<List<PersonDto>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            List<PersonDto> persons = personService.readPersonsFromExcel(file.getInputStream());
            return ResponseEntity.ok(persons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/export")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        personService.generateExcel(response);
    }


}
