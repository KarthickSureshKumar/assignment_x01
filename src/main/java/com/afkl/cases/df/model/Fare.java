package com.afkl.cases.df.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(NON_NULL)
@Data
public class Fare {
	double amount;
	Currency currency;
	String origin, destination;

}
