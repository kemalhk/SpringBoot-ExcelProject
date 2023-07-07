package com.example.ExcelProject.util;

import com.example.ExcelProject.dto.PersonDto;
import com.example.ExcelProject.dto.JobDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static List<PersonDto> readPersonsFromExcel(InputStream inputStream) throws Exception {
        List<PersonDto> persons = new ArrayList<>();

        // Excel dosyasını oku
        Workbook workbook = new XSSFWorkbook(inputStream);

        // İlk sayfayı al
        Sheet sheet = workbook.getSheetAt(0);


        // Satırları oku
        Iterator<Row> rowIterator = sheet.iterator();
        //başlıkları atla
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Sütunları oku
            Iterator<Cell> cellIterator = row.cellIterator();

            // PersonDto oluştur
            PersonDto personDto = new PersonDto();

            // İlk sütundaki veriyi al (ad)
            Cell nameCell = cellIterator.next();
            personDto.setName(nameCell.getStringCellValue());

            // İkinci sütundaki veriyi al (soyad)
            Cell surnameCell = cellIterator.next();
            personDto.setSurname(surnameCell.getStringCellValue());


            // Üçüncü sütundaki veriyi al (yaş)
            Cell ageCell = cellIterator.next();
            if (ageCell.getCellType() == CellType.NUMERIC) {
                personDto.setAge((int) ageCell.getNumericCellValue());
            } else {
                // Dizeyi (string) sayıya dönüştürmek için gerekli işlemleri yapın
                String ageString = ageCell.getStringCellValue();
                int age = Integer.parseInt(ageString);
                personDto.setAge(age);
            }

            // Daha fazla sütun varsa, iş verilerini oku
            if (cellIterator.hasNext()) {

                // JobDto oluştur
                JobDto jobDto = new JobDto();

                // Dördüncü sütundaki veriyi al (iş adı)
                Cell jobNameCell = cellIterator.next();
                jobDto.setDepartmentName(jobNameCell.getStringCellValue());

                // Beşinci sütundaki veriyi al (iş kodu)
                Cell jobCodeCell = cellIterator.next();
                jobDto.setDepartmentCode(jobCodeCell.getStringCellValue());

                // altıncı sütundaki veriyi al (job id)
//                Cell jobIdCell = cellIterator.next();
//                if (jobIdCell.getCellType() == CellType.NUMERIC) {
//                    jobDto.setId((long) jobIdCell.getNumericCellValue());
//                } else {
//                    // Dizeyi (string) sayıya dönüştürmek için gerekli işlemleri yapın
//                    String jobIdString = jobIdCell.getStringCellValue();
//                    long jobId = Long.parseLong(jobIdString);
//                    jobDto.setId(jobId);
//                }

                personDto.setJob(jobDto);
            }

            // PersonDto'yu listeye ekle
            persons.add(personDto);
        }

        // Workbook'i kapat
        workbook.close();

        return persons;
    }


    public void exportPersonsToExcel(List<PersonDto> persons, OutputStream outputStream) throws Exception {
        // Workbook oluştur
        Workbook workbook = new XSSFWorkbook();

        // Yeni bir sayfa oluştur
        Sheet sheet = workbook.createSheet("Persons");

        // Başlık satırını oluştur
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Surname");
        headerRow.createCell(2).setCellValue("Age");
        headerRow.createCell(3).setCellValue("Job Department Name");
        headerRow.createCell(4).setCellValue("Job Department Code");

        // Kişi verilerini yazdır
        int rowIndex = 1;
        for (PersonDto personDto : persons) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(personDto.getName());
            row.createCell(1).setCellValue(personDto.getSurname());
            row.createCell(2).setCellValue(personDto.getAge());

            if (personDto.getJob() != null) {
                row.createCell(3).setCellValue(personDto.getJob().getDepartmentName());
                row.createCell(4).setCellValue(personDto.getJob().getDepartmentCode());
            }
        }

        // Workbook'u çıktı akışına yazdır
        workbook.write(outputStream);

        // Workbook'u kapat
        workbook.close();
    }
}
