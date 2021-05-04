package com.afkl.cases.df.serviceimpl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.afkl.cases.df.config.ServicePropConfig;
import com.afkl.cases.df.constant.AppConstant;
import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.AirportDetails;
import com.afkl.cases.df.service.AirportService;

@Service
public class AirportServiceImpl implements AirportService {

	@Autowired
	OAuth2RestTemplate oauth2RestTemplate;

	@Autowired
	ServicePropConfig servicePropConfig;

	/**
	 * Get all available airport
	 * 
	 */
	@Override
	public AirportDetails getAllAvailableAirports(Integer page, Integer size, String lang, String term)
			throws ApplicationException {
		UriComponentsBuilder builder = uriBuilder(page, size, lang, term);
		AirportDetails airportDetails = oauth2RestTemplate.getForObject(builder.build().toUriString(),
				AirportDetails.class);
		if (null == airportDetails) {
			throw new ApplicationException(AppConstant.NOT_FOUND);
		}
		return airportDetails;
	}

	/**
	 * Builds URI based on request params
	 * 
	 * @param page
	 * @param size
	 * @param lang
	 * @param term
	 * @return
	 */
	private UriComponentsBuilder uriBuilder(Integer page, Integer size, String lang, String term) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(servicePropConfig.getAllAirportUrl());
		if (ObjectUtils.allNotNull(page, size) && size > 0 && page > 0) {
			builder.queryParam(AppConstant.PAGE, page);
			builder.queryParam(AppConstant.SIZE, size);
		}
		if (StringUtils.isNotBlank(lang)) {
			builder.queryParam(AppConstant.LANG, lang);
		}
		if (StringUtils.isNotBlank(term)) {
			builder.queryParam(AppConstant.TERM, term);
		}
		return builder;
	}

}
