package com.afkl.cases.df.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afkl.cases.df.constant.AppConstant;
import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.FareDetail;
import com.afkl.cases.df.service.FareService;

@RestController
@RequestMapping("/fares")
public class FareController {

	@Autowired
	FareService fareService;

	/**
	 *
	 * To get the fare offers for the provided origin and destination
	 *
	 * @param origin
	 * @param destination
	 * @param currency
	 * @return
	 * @throws ApplicationException
	 */
	@GetMapping("{origin}/{destination}")
	public FareDetail getFareOffers(@PathVariable("origin") String origin,
			@PathVariable("destination") String destination,
			@RequestParam(value = "currency", defaultValue = "EUR") String currency) throws ApplicationException {
		if (StringUtils.isNoneBlank(origin, destination))
			return fareService.getFareDetails(origin, destination, currency);
		throw new ApplicationException(AppConstant.SERVICE_EXCEPTION);
	}

}
