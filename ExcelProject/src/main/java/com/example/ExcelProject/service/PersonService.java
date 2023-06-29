package com.example.ExcelProject.service;

import com.example.ExcelProject.dto.PersonDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

public interface PersonService {

    // Save operation
    PersonDto save(PersonDto personDto);

    // Delete operation
    void delete(Long id);

    // Read operation
    List<PersonDto> getAll();

    // Update operation
    PersonDto updatePerson(Long id, PersonDto personDto);



    Page<PersonDto> getAll(Pageable pageable);
}
