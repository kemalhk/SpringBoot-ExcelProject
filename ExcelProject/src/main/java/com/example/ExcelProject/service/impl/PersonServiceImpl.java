package com.example.ExcelProject.service.impl;

import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.entity.Person;
import com.example.ExcelProject.repository.PersonRepository;
import com.example.ExcelProject.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    @Transactional
    public PersonDto save(PersonDto personDto) {
        Assert.notNull(personDto.getName(), "Ad alanı zorunludur.");
        Assert.notNull(personDto.getSurname(), "Soyad alanı zorunludur.");

        Person person = new Person();
        person.setName(personDto.getName());
        person.setSurname(personDto.getSurname());
        person.setAge(personDto.getAge());

        Person savedPerson = personRepository.save(person);

        PersonDto savedPersonDto = new PersonDto();
        savedPersonDto.setId(savedPerson.getId());
        savedPersonDto.setName(savedPerson.getName());
        savedPersonDto.setSurname(savedPerson.getSurname());
        savedPersonDto.setAge(savedPerson.getAge());

        return savedPersonDto;
    }

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonDto> getAll() {
        List<Person> persons = personRepository.findAll();

        return persons.stream()
                .map(person -> {
                    PersonDto personDto = new PersonDto();
                    personDto.setId(person.getId());
                    personDto.setName(person.getName());
                    personDto.setSurname(person.getSurname());
                    personDto.setAge(person.getAge());

                    return personDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto updatePerson(Long id, PersonDto personDto) {
        return null;
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
}
