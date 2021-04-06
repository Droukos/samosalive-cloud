package com.droukos.authservice;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

@SpringBootTest
@AllArgsConstructor
public class EmailTest {

   //private static EmailSender emailSender;

    //@BeforeAll
    //public static void setupOnce(@Autowired EmailSender emailSender) {
    //    EmailTest.emailSender = emailSender;
    //}

    @Test
    void test() {
        System.out.println("hey");
        SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        System.out.println(key);
        String base64Encoded = TextCodec.BASE64.encode(key.getEncoded());
        System.out.println(base64Encoded);
        //emailSender.sendSimpleMessage("kostas131996@gmail.com", "yo", "oooi");
    }
}
