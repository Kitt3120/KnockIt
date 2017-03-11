package de.spezipaul.knockit.exceptions;

public class NoKittException extends Exception {
	
	public NoKittException() {
		super();
	}

	public NoKittException(String msg){
		super(msg);
	}
	
	public NoKittException(Throwable cause){
		super(cause);
	}
	
	public NoKittException(String msg, Throwable cause){
		super(msg, cause);
	}

}
