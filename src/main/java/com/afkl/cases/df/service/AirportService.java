package com.afkl.cases.df.service;

import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.AirportDetails;

public interface AirportService {
	AirportDetails getAllAvailableAirports(Integer page, Integer size, String lang, String term)
			throws ApplicationException;
}
