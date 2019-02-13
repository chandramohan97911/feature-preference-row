package com.feature.preference.row.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class ExceptionControllerAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(NumberFormatException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0007);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ex.errorCode);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateOrderException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(DuplicateOrderException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0001);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(InvalidDataException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0006);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}

		
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0007);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	 }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0007);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	 }
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleMethodException(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(ErrorCode.FPR_0007);
		error.setMessage(ex.getMessage());
		logException(ex);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	 }
	 @ExceptionHandler(IllegalStateException.class) 
	 public ResponseEntity<ErrorResponse> assertionException(final IllegalStateException e) {
		 ErrorResponse error = new ErrorResponse();
			error.setErrorCode(ErrorCode.FPR_0007);
			error.setMessage(e.getMessage());
			logException(e);
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }
	
	private void logException(Exception e){
		logger.error("Exception occured while invoking API{}",e);
	}

}