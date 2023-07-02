package com.example.ExcelProject.service;


import com.example.ExcelProject.dto.PersonDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ByteArrayOutputStream;


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



    List<PersonDto> readPersonsFromExcel(InputStream inputStream) throws Exception;



    void generateExcel(HttpServletResponse response) throws IOException;





    Page<PersonDto> getAll(Pageable pageable);
}
