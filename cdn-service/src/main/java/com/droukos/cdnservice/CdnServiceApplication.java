package com.droukos.cdnservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class CdnServiceApplication {

	public static void main(String[] args) {
		BlockHound.install();
		SpringApplication.run(CdnServiceApplication.class, args);
	}

}
