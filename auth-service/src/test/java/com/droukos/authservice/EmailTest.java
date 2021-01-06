package com.droukos.authservice;

import com.droukos.authservice.service.mail.EmailSender;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
public class EmailTest {

   private static EmailSender emailSender;

    @BeforeAll
    public static void setupOnce(@Autowired EmailSender emailSender) {
        EmailTest.emailSender = emailSender;
    }
    @Test
    void test() {
        emailSender.sendSimpleMessage("kostas131996@gmail.com", "yo", "oooi");
    }
}
