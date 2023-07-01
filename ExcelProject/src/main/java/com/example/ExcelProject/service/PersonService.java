package com.example.ExcelProject.service;


import com.example.ExcelProject.dto.PersonDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.io.InputStream;
import java.util.List;

public interface PersonService {

    //////////////////////////////////////////
    PersonDto getPersonById(Long id);

    /////////////////////////////////

    // Save operation
    PersonDto save(PersonDto personDto);

    // Delete operation
    void delete(Long id);

    // Read operation
    List<PersonDto> getAll();

    // Update operation
    PersonDto updatePerson(Long id, PersonDto personDto);

//    List<PersonDto> importPersonsFromExcel(String filePath);

    List<PersonDto> readPersonsFromExcel(InputStream inputStream) throws Exception;



    Page<PersonDto> getAll(Pageable pageable);
}
