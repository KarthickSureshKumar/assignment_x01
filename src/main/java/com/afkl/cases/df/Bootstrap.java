package com.afkl.cases.df;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import java.io.IOException;

@EnableEncryptableProperties
@SpringBootApplication
public class Bootstrap {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Bootstrap.class, args);
	}

}
