package com.afkl.cases.df.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import com.afkl.cases.df.config.ServicePropConfig;
import com.afkl.cases.df.model.AirportDetails;
import com.afkl.cases.df.model.Fare;
import com.afkl.cases.df.model.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@TestPropertySource(locations = { "classpath:application.yml", "classpath:application-test.yml" })
@ActiveProfiles("test")
public class ApplicationTest implements JUnitConstant {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	OAuth2RestTemplate oauth2RestTemplate;

	@Autowired
	ServicePropConfig servicePropConfig;

	@Value("${tokenService}")
	private String tokenUrl;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void configureMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}

	@Test
	public void testRetrieveAllAirport() throws Exception {
		Mockito.when(
				oauth2RestTemplate.getForObject(HTTP_LOCALHOST_8080_AIRPORTS.concat(LANG_EN), AirportDetails.class))
				.thenReturn(objectMapper.readValue(MOCK_AIRPORT_RESPONSE, AirportDetails.class));
		mockMvc.perform(get("/airport/all")).andExpect(status().isOk());
	}

	@Test
	public void testRetrieveFareService() throws Exception {
		Mockito.when(oauth2RestTemplate.getForObject(HTTP_LOCALHOST_8080_FARES_HOU_LXA_CURRENCY_EUR, Fare.class))
				.thenReturn(objectMapper.readValue(FARE_MOCK_RESPONSE, Fare.class));
		Mockito.when(oauth2RestTemplate.getForObject(HTTP_LOCALHOST_8080_AIRPORTS_HOU, Location.class))
				.thenReturn(objectMapper.readValue(LOCATION_MOCK, Location.class));
		Mockito.when(oauth2RestTemplate.getForObject(HTTP_LOCALHOST_8080_AIRPORTS_LXA, Location.class))
				.thenReturn(objectMapper.readValue(LOCATION_MOCK1, Location.class));
		mockMvc.perform(get("/fares/{origin}/{destination}", "HOU", "LXA")).andExpect(status().isOk());
	}

	@Test
	public void testRetrieveFareServiceException() throws Exception {
		Mockito.when(oauth2RestTemplate.getForObject("http://localhost:8080/fares/yyy/zzz?currency=EUR", Fare.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		mockMvc.perform(get("/fares/{origin}/{destination}", "yyy", "zzz")).andExpect(status().isBadRequest());
	}

	@Test
	public void testRetrieveFaresFailureCase() throws Exception {
		mockMvc.perform(get("/fares/{origin}/{destination}", " ", "LXA")).andExpect(status().isBadRequest());
	}

	@Test
	public void testRetrieveAllAirportException() throws Exception {
		Mockito.when(oauth2RestTemplate.getForObject("http://localhost:8080/airports?page=100&size=200&lang=en",
				AirportDetails.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		mockMvc.perform(get("/airport/all?page={0}&size={1}", 100, 200)).andExpect(status().isBadRequest());
	}
}
