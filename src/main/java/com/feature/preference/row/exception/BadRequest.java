package com.feature.preference.row.exception;

public class BadRequest extends ApplicationException{
	
	private static final long serialVersionUID = 1L;

	public BadRequest(ErrorCode errorCode,String message,Throwable arg1)
	{
		this.errorCode=errorCode;
		this.message=message;
	} 

	public BadRequest(ErrorCode errorCode,String message)
	{
		super(errorCode+":"+message);
		this.errorCode=errorCode;
		this.message=message;
	} 
	
}
