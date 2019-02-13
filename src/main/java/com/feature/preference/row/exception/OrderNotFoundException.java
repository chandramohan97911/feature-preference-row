package com.feature.preference.row.exception;

public class OrderNotFoundException extends ApplicationException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OrderNotFoundException(ErrorCode errorCode,String message,Throwable arg1)
	{
		super(errorCode,message,arg1);
		this.errorCode=errorCode;
		this.message=message;
	} 
	public OrderNotFoundException(ErrorCode errorCode,String message)
	{
		super(errorCode+":"+message);
		this.errorCode=errorCode;
		this.message=message;
	}
}
