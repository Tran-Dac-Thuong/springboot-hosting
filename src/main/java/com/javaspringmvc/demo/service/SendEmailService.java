package com.javaspringmvc.demo.service;

import com.javaspringmvc.demo.model.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String email, String name, String reply){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom("hoangdeptraibodoiqua4321@gmail.com", "Pillow-Mart");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Answer customer questions");
            mimeMessageHelper.setText("Hi, " + name + "\nWe have received your question\nOur answer is " + reply + "\nThank you for using our service");

            javaMailSender.send(mimeMessage);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendSubscribe(String email){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom("hoangdeptraibodoiqua4321@gmail.com", "Pillow-Mart");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Get promotions & updates!");
            mimeMessageHelper.setText("Congratulations! You will receive promotions and updates related to our services.\nThank you for using our service.");

            javaMailSender.send(mimeMessage);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
