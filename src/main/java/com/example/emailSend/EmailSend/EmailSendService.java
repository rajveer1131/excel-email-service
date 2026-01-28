package com.example.emailSend.EmailSend;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.List;

@Service
public class EmailSendService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExcelParseService excelParseService;



    public void sendSimpleMessage(UserModel user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("useradmin@yopmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Test Email");

        String messageBody=
                "Hi "+user.getUsername()+
                "\n How is life? Fine i guess.\n\n Pleasse see this attached message "+
                user.getMessage()+
                "\n\nThanks take care";

        message.setText(messageBody);
        mailSender.send(message);
    }

    public List<UserModel> sendMessageFromExcelData(MultipartFile file) throws IOException, MessagingException {
        List<UserModel> users = excelParseService.readExcel(file);
        for (UserModel user:users){
            sendHtmlMailMessage(user,file);
        }
        return users;
    }

    public void sendHtmlMailMessage(UserModel user,MultipartFile file) throws IOException,MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        mimeMessageHelper.setSubject("Test Email");
        mimeMessageHelper.setTo(user.getEmail());

        String template="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Email</title>\n" +
                "</head>\n" +
                "<body style=\"margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif;\">\n" +
                "\n" +
                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\" style=\"padding:30px 0;\">\n" +
                "\n" +
                "            <!-- Email Card -->\n" +
                "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                   style=\"background:#ffffff; border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.1);\">\n" +
                "\n" +
                "                <!-- Header -->\n" +
                "                <tr>\n" +
                "                    <td style=\"background:#4f46e5; color:#ffffff; padding:20px; border-radius:10px 10px 0 0;\">\n" +
                "                        <h2 style=\"margin:0;\">\uD83D\uDCE9 Hello {{username}}</h2>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <!-- Body -->\n" +
                "                <tr>\n" +
                "                    <td style=\"padding:25px; color:#333;\">\n" +
                "                        <p style=\"font-size:15px;\">\n" +
                "                            How is life? \uD83D\uDE0A Hope everything is going great!\n" +
                "                        </p>\n" +
                "\n" +
                "                        <p style=\"margin-top:20px; font-weight:bold;\">\n" +
                "                            \uD83D\uDCCC Attached Message:\n" +
                "                        </p>\n" +
                "\n" +
                "                        <div style=\"background:#f1f5f9; padding:15px; border-left:4px solid #4f46e5; border-radius:6px;\">\n" +
                "                            {{message}}\n" +
                "                        </div>\n" +
                "\n" +
                "                        <p style=\"margin-top:25px;\">\n" +
                "                            Thanks for taking the time to read this.\n" +
                "                        </p>\n" +
                "\n" +
                "                        <p style=\"margin-top:10px;\">\n" +
                "                            Take care,<br>\n" +
                "                            <strong>Team Support</strong>\n" +
                "                        </p>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <!-- Footer -->\n" +
                "                <tr>\n" +
                "                    <td style=\"background:#f8fafc; padding:15px; text-align:center; font-size:12px; color:#6b7280; border-radius:0 0 10px 10px;\">\n" +
                "                        © 2026 Your Company • All rights reserved\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "            </table>\n" +
                "\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        String htmlBody = template
                .replace("{{username}}", user.getUsername())
                .replace("{{message}}", user.getMessage());
        mimeMessageHelper.setText(htmlBody,true);
        mimeMessageHelper.addAttachment(file.getOriginalFilename(),file);
mailSender.send(mimeMessage);
    }
}