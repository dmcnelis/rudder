package me.mcnelis.rudder.exceptions;

public class FeatureNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FeatureNotFoundException() {super();}
	
	public FeatureNotFoundException(String info) {
		super(info);
	}

}
