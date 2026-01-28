package com.example.emailSend.EmailSend;

import org.apache.catalina.User;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelParseService {

    public List<UserModel> readExcel(MultipartFile file) throws IOException{
        List<UserModel> users = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        if(file.isEmpty()){
            throw new IOException("file is empty");
        }

        try(InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is)){
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

           if(rowIterator.hasNext()){
               rowIterator.next();
           }
           while(rowIterator.hasNext()){
               Row row = rowIterator.next();
               String id = dataFormatter.formatCellValue(row.getCell(0));
               String name = dataFormatter.formatCellValue(row.getCell(1));
               String email = dataFormatter.formatCellValue(row.getCell(2));
               String message= dataFormatter.formatCellValue(row.getCell(3));
               UserModel user = new UserModel();
               user.setId(id);
               user.setUsername(name);
               user.setEmail(email);
               user.setMessage(message);
               users.add(user);

               System.out.println(id+" "+name +" "+email+" "+message);
           }

        }
        catch (Exception e) {
            throw new IOException("Failed to read Excel file: " + e.getMessage(), e);
        }
        return users;
    }
}
