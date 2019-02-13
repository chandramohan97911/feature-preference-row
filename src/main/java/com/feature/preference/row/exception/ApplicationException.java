package com.feature.preference.row.exception;

public class ApplicationException extends Exception{
	private static final long serialVersionUID = 1L;
	ErrorCode errorCode;
	String message;
	
	public ApplicationException(){
		
	}
	
	public ApplicationException(ErrorCode errorCode,String message,Throwable arg1)
	{
		super(errorCode+":"+message,arg1);
		this.errorCode=errorCode;
		this.message=message;
	} 
	
	public ApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public ApplicationException(ErrorCode errorCode,String message)
	{
		super(errorCode+":"+message);
		this.errorCode=errorCode;
		this.message=message;
	} 
	
	
	public ApplicationException(String arg0) {
		super(arg0);
	}

	public ApplicationException(Throwable arg0) {
		super(arg0);
	}

	
	
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
