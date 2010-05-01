package com.livegather.android.exceptions;

public class InvalidRequestException extends Exception 
{
	private static final long serialVersionUID = 1021083710280294844L;

	public InvalidRequestException(String msg)
	{
		super(msg);
	}
	
	public InvalidRequestException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
