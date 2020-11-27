package com.droukos.edgeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class EdgeServiceApplicationTests {

	@Test
	void contextLoads() {
		LocalDate userDate = LocalDate.parse("0911/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate startDate = LocalDate.parse("07/11/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate endDate = LocalDate.parse("10/11/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		System.out.println(startDate.compareTo(userDate));
		System.out.println(endDate.compareTo(userDate));
	}

}
