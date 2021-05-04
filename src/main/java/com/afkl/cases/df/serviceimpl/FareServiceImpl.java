package com.afkl.cases.df.serviceimpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.afkl.cases.df.config.ServicePropConfig;
import com.afkl.cases.df.constant.AppConstant;
import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.Fare;
import com.afkl.cases.df.model.FareDetail;
import com.afkl.cases.df.model.Location;
import com.afkl.cases.df.service.FareService;

@Service
public class FareServiceImpl implements FareService {

	@Autowired
	OAuth2RestTemplate oauth2RestTemplate;

	@Autowired
	ServicePropConfig servicePropConfig;

	@Autowired
	ExecutorService executorService;

	/**
	 * Get the fare details from fare and airport service asynchronously
	 * 
	 * 
	 */
	@Override
	public FareDetail getFareDetails(String origin, String destination, String currency) throws ApplicationException {
		try {
			Map<String, String> propMap = new HashMap<String, String>();
			propMap.put(AppConstant.ORIGIN, origin);
			propMap.put(AppConstant.DESTINATION, destination);
			propMap.put(AppConstant.CURRENCY, currency);
			CompletableFuture<Optional<Fare>> fareFuture = CompletableFuture.supplyAsync(() -> {
				return Optional.ofNullable(oauth2RestTemplate
						.getForObject(buildUri(servicePropConfig.getFareServiceUrl(), propMap), Fare.class));
			}, executorService);
			if (fareFuture.get().isPresent()) {
				FareDetail fareDetails = new FareDetail();
				Fare fare = fareFuture.get().get();
				CompletableFuture<Optional<Location>> optOriginDetail = CompletableFuture.supplyAsync(() -> {
					return Optional
							.ofNullable(oauth2RestTemplate.getForObject(
									buildUri(servicePropConfig.getAirportServiceUrl(),
											Collections.singletonMap(AppConstant.CODE, fare.getOrigin())),
									Location.class));
				}, executorService);

				CompletableFuture<Optional<Location>> optDestDetail = CompletableFuture.supplyAsync(() -> {
					return Optional.ofNullable(oauth2RestTemplate.getForObject(
							buildUri(servicePropConfig.getAirportServiceUrl(),
									Collections.singletonMap(AppConstant.CODE, fare.getDestination())),
							Location.class));
				}, executorService);
				List<Optional<Location>> details = Stream.of(optOriginDetail, optDestDetail)
						.map(CompletableFuture::join).collect(Collectors.toList());
				details.stream().filter(Optional::isPresent).forEach(i -> {
					Location loc = i.get();
					if (loc.getCode().equals(fare.getOrigin())) {
						fareDetails.setOrgin(loc);
					} else if (loc.getCode().equals(fare.getDestination())) {
						fareDetails.setDestination(loc);
					}
				});
				fareDetails.setAmount(fare.getAmount());
				fareDetails.setCurrency(fare.getCurrency());
				return fareDetails;
			} else {
				throw new ApplicationException(AppConstant.NOT_FOUND);
			}
		} catch (HttpClientErrorException | InterruptedException | ExecutionException ex) {
			throw new ApplicationException(AppConstant.SERVICE_EXCEPTION, ex);
		}
	}

	/**
	 * 
	 * URI Builder
	 * 
	 * @param url
	 * @param uriVariables
	 * @return
	 */

	private String buildUri(String url, Map<String, String> uriVariables) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		return builder.buildAndExpand(uriVariables).toUriString();
	}

}
