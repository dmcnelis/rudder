package me.mcnelis.rudder.data;

public class MockRecord extends Record {
	
	@Feature
	protected double feature1;
	
	@Feature
	protected double feature2;
	
	@Feature
	protected double feature3;
	
	protected double nonFeature;
	
	@Label
	protected double doubleLabel;
	
	@Label
	protected  String stringLabel;
}
