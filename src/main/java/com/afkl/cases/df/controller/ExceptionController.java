package com.afkl.cases.df.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.afkl.cases.df.constant.AppConstant;
import com.afkl.cases.df.exception.ApplicationException;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Slf4j
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<String> handleApplicationException(Throwable t) {
		log.error("Unable to process request.", t);
		if (t.getMessage().contains(AppConstant.SERVICE_EXCEPTION)) {
			return new ResponseEntity<String>(AppConstant.SERVICE_EXCEPTION, HttpStatus.BAD_REQUEST);
		} else if (t.getMessage().contains(AppConstant.NOT_FOUND)) {
			return new ResponseEntity<String>(AppConstant.NOT_FOUND, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>(AppConstant.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<String> handleBadRequest(Throwable t) {
		log.error("Unable to process request.", t);
		return new ResponseEntity<String>(AppConstant.SERVICE_EXCEPTION, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> handleGlobalException(Throwable t) {
		log.error("Unable to process request.", t);
		return new ResponseEntity<String>(SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<String> handleGlobalException(HttpServerErrorException e) {
		return new ResponseEntity<String>(e.getStatusCode());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest() {
		return new ResponseEntity<String>(BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneric(Throwable t) {
		log.error("Unable to process request.", t);
		return new ResponseEntity<String>(AppConstant.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
