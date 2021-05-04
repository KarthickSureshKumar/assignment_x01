package com.afkl.cases.df.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

@Configuration
public class CommonConfig {

	@Bean
	public ExecutorService asyncThreadPool() {
		return Executors.newCachedThreadPool(new CustomizableThreadFactory("parallel-thread"));
	}
}
