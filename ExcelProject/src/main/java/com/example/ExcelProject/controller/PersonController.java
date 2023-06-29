package com.example.ExcelProject.controller;


import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.service.PersonService;
import lombok.RequiredArgsConstructor;
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

//    @GetMapping("/{id}")
//    public ResponseEntity<Person> getPersonById(@PathVariable Long id){
//        Person person = personService.getPersonById(id);
//        if(person !=null)
//        {
//            return  ResponseEntity.ok(person);
//        }
//        return ResponseEntity.notFound().build();
//    }



//    @PutMapping("/{id}")
//    public ResponseEntity<Person> updatePerson(@PathVariable Long id,@RequestBody Person updatedPerson){
//        Person person = personService.getPersonById(id);
//        if (person != null) {
//            updatedPerson.setId(id);
//            Person updated = personService.updatePerson(updatedPerson);
//            return ResponseEntity.ok(updated);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
//        Person person = personService.getPersonById(id);
//        if (person != null) {
//            personService.deletePerson(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }






}
