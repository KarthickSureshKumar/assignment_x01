package com.afkl.cases.df.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

@Profile("test")
@Configuration
public class TestConfig {

	@Profile("test")
	@Bean
	public OAuth2RestTemplate oauth2RestTemplate() {
		return Mockito.mock(OAuth2RestTemplate.class);
	}
}
