package com.afkl.cases.df.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.AirportDetails;
import com.afkl.cases.df.service.AirportService;

@RestController
@RequestMapping("/airport")
public class AirportController {

	@Autowired
	AirportService airportService;

	/**
	 * To get all available airports
	 * 
	 * @param page
	 * @param size
	 * @param lang
	 * @param term
	 * @return
	 * @throws ApplicationException
	 */
	@GetMapping("/all")
	public AirportDetails getAllAvailableAirport(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "lang", defaultValue = "en") String lang,
			@RequestParam(value = "term", required = false) String term) throws ApplicationException {
		return airportService.getAllAvailableAirports(page, size, lang, term);
	}
}
