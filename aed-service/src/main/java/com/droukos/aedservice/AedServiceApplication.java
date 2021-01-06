package com.droukos.aedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import reactor.blockhound.BlockHound;

@SpringBootApplication
public class AedServiceApplication {

    public static void main(String[] args) {
        //BlockHound.install();
        SpringApplication.run(AedServiceApplication.class, args);
    }

}
