package com.afkl.cases.df.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties
public class ServicePropConfig {

	@Value("${fareService}")
	private String fareServiceUrl;

	@Value("${airportService}")
	private String airportServiceUrl;

	@Value("${allAirportService}")
	private String allAirportUrl;
}
