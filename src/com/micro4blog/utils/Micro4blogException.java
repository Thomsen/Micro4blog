package com.micro4blog.utils;

public class Micro4blogException extends Exception {

	public int statusCode = -1;
	
	public Micro4blogException() {
		super();
	}
	
	public Micro4blogException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}
	
	public Micro4blogException(String msg) {
		super(msg);
	}
	
	public Micro4blogException(Exception cause) {
		super(cause);
	}

	public Micro4blogException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}
	
	public Micro4blogException(String msg, Exception cause) {
		super(msg, cause);
	}
	
	public Micro4blogException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;
	}
	
	public Micro4blogException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public Micro4blogException(Throwable throwable) {
		super(throwable);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
	
}
