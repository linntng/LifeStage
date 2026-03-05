package com.loop.lifestage.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(ResourceNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public ResponseEntity<ErrorResponse> handleNotFound(
	ResourceNotFoundException ex, HttpServletRequest request) {
	return new ResponseEntity<ErrorResponse>(
		new ErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI()),
		HttpStatus.NOT_FOUND);
}

@ExceptionHandler(BadRequestException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<ErrorResponse> handleBadRequest(
	BadRequestException ex, HttpServletRequest request) {
	return new ResponseEntity<ErrorResponse>(
		new ErrorResponse(400, "Bad Request", ex.getMessage(), request.getRequestURI()),
		HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(ResourceAlreadyExistsException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
	ResourceAlreadyExistsException ex, HttpServletRequest request) {
	return new ResponseEntity<ErrorResponse>(
		new ErrorResponse(409, "Conflict", ex.getMessage(), request.getRequestURI()),
		HttpStatus.CONFLICT);
}

@ExceptionHandler(Exception.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public ResponseEntity<ErrorResponse> handleGenericException(
	Exception ex, HttpServletRequest request) {
	return new ResponseEntity<ErrorResponse>(
		new ErrorResponse(
			500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI()),
		HttpStatus.INTERNAL_SERVER_ERROR);
}
}
