package com.example.ExcelProject.controller;


import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
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

//    @PutMapping("/{id}")
//
//    public ResponseEntity<Person> updatePerson(@PathVariable Long id,@RequestBody PersonDto updatedPerson){
//        PersonDto personDto = personService.getPersonById(id);
//        if (personDto != null) {
//            updatedPerson.setId(id);
//            PersonDto updated = personService.updatePerson(id, updatedPerson);
//
//            return ResponseEntity.ok(updated);
//        }
//        return ResponseEntity.notFound().build();
//    }
//







}
