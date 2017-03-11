package de.spezipaul.knockit.exceptions;

public class NoKittDescriptionException extends Exception {
	
	public NoKittDescriptionException() {
		super();
	}

	public NoKittDescriptionException(String msg){
		super(msg);
	}
	
	public NoKittDescriptionException(Throwable cause){
		super(cause);
	}
	
	public NoKittDescriptionException(String msg, Throwable cause){
		super(msg, cause);
	}

}
