package com.feature.preference.row.exception;

public class DuplicateOrderException extends Exception{
	private static final long serialVersionUID = 1L;
	Integer errorCode;
	String message;
	
	public DuplicateOrderException(){
		
	}
	
	public DuplicateOrderException(Integer errorCode,String message,Throwable arg1)
	{
		super(errorCode+":"+message,arg1);
		this.errorCode=errorCode;
		this.message=message;
	} 
	
	public DuplicateOrderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public DuplicateOrderException(Integer errorCode,String message)
	{
		super(errorCode+":"+message);
		this.errorCode=errorCode;
		this.message=message;
	} 
	
	
	public DuplicateOrderException(String arg0) {
		super(arg0);
	}

	public DuplicateOrderException(Throwable arg0) {
		super(arg0);
	}

	
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
