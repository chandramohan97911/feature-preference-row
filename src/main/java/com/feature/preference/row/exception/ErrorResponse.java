package com.feature.preference.row.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(value = Include.NON_NULL)
public class ErrorResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private ErrorCode errorCode;
	private String message;
	
	@JsonProperty("Error")
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
		
}
