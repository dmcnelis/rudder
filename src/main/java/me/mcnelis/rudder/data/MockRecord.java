package me.mcnelis.rudder.data;

public class MockRecord extends Record {
	
	@NumericFeature
	protected double feature1;
	
	@NumericFeature
	protected double feature2;
	
	@NumericFeature
	protected double feature3;
	
	
	protected double nonFeature;
	
	@Label
	protected double doubleLabel;
	
	@Label
	protected  String stringLabel;
}
