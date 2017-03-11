package de.spezipaul.knockit.exceptions;

public class NoLastDamagerException extends Exception {
	
	public NoLastDamagerException() {
		super();
	}

	public NoLastDamagerException(String msg){
		super(msg);
	}
	
	public NoLastDamagerException(Throwable cause){
		super(cause);
	}
	
	public NoLastDamagerException(String msg, Throwable cause){
		super(msg, cause);
	}

}
