package com.afkl.cases.df.service;

import com.afkl.cases.df.exception.ApplicationException;
import com.afkl.cases.df.model.FareDetail;

public interface FareService {

	FareDetail getFareDetails(String origin, String destination, String currency) throws ApplicationException;
}
