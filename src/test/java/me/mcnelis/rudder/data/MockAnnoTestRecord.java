package me.mcnelis.rudder.data;

public class MockAnnoTestRecord
{
	double doubleLabel;
	String stringLabel;
	
	double dVal1;
	double dVal2;
	@NumericFeature
	double dv3;
	String sVal1;
	String sVal2;
	@Label(setlabel="setTestLabel", type=FeatureType.TEXT)
	String testLabel2;
	
	public MockAnnoTestRecord(double dL, String sL, double dv1, double dv2,double dv3,String sv1, String sv2)
	{
		this.doubleLabel = dL;
		this.stringLabel = sL;
		this.dVal1 = dv1;
		this.dVal2 = dv2;
		this.dv3 = dv3;
		this.sVal1 = sv1;
		this.sVal2 = sv2;
	}
	
	public void setTestLabel(String s)
	{
		this.testLabel2 = s;
	}
	
	@Label(type=FeatureType.NUMERIC)
	public double getDoubleLabel()
	{
		return doubleLabel;
	}
	
	public void setLabel(Object o)
	{
		if(o instanceof Double)
		{
			this.setDoubleLabel((Double)o);
		}
		else if (o instanceof String)
		{
			this.setStringLabel((String) o);
		}
	}
	
	public void setLabel(String s)
	{
		this.setStringLabel(s);
	}
	
	public void setLabel(Double d)
	{
		this.setDoubleLabel(d);
	}
	
	public void setDoubleLabel(double doubleLabel)
	{
		this.doubleLabel = doubleLabel;
	}
	
	public void setStringLabel(String s)
	{
		this.stringLabel = s;
	}
	@Label(type=FeatureType.TEXT)
	public String getStringLabel()
	{
		return stringLabel;
	}
	public void setStringFeature(String stringLabel)
	{
		this.stringLabel = stringLabel;
	}
	
	@NumericFeature
	public double getdVal1()
	{
		return dVal1;
	}
	public void setdVal1(double dVal1)
	{
		this.dVal1 = dVal1;
	}
	
	@NumericFeature
	public double getdVal2()
	{
		return dVal2;
	}
	public void setdVal2(double dVal2)
	{
		this.dVal2 = dVal2;
	}
	
	@TextFeature
	public String getsVal1()
	{
		return sVal1;
	}
	public void setsVal1(String sVal1)
	{
		this.sVal1 = sVal1;
	}
	
	@TextFeature
	public String getsVal2()
	{
		return sVal2;
	}
	public void setsVal2(String sVal2)
	{
		this.sVal2 = sVal2;
	}
	
}
