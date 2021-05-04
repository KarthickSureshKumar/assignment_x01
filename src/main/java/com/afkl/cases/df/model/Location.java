package com.afkl.cases.df.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Data
public class Location {

	private String code, name, description;
	private Coordinates coordinates;

}
