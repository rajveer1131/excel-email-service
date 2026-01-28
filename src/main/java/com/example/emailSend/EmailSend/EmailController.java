package com.example.emailSend.EmailSend;

import jakarta.mail.MessagingException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class EmailController {

    @Autowired
    EmailSendService emailSendService;

    @Autowired
    ExcelParseService excelParseService;

//    @GetMapping("/")
//    public String Emailtrigger(){
//        List<String> list = Arrays.asList("user1@yopmail.com","user2@yopmail.com","user3@yopmail.com","user4@yopmail.com");
//        for(String s:list){
//            emailSendService.sendSimpleMessage(s,"Email service test","Hi how are you");
//        }
//
//        return "Success";
//    }

    @PostMapping("/read")
    public List<UserModel> excelRead(@RequestParam("file") MultipartFile file){
        try{
            return emailSendService.sendMessageFromExcelData(file);


        }catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error reading Excel file: " + e.getMessage(),
                    e
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
