package me.mcnelis.rudder.data;
import java.lang.reflect.Field;

import me.mcnelis.rudder.data.Label;

public class MockRecord{
	
	@NumericFeature
	public double feature1;
	
	@NumericFeature
	public double feature2;
	
	@NumericFeature
	public double feature3;
	
	
	protected double nonFeature;
	
	@Label(setlabel="setDoubleLabel")
	protected double doubleLabel;
	
	@Label(setlabel="setStringLabel")
	protected  String stringLabel;
	
	public void setLabel(double d)
	{
		this.doubleLabel = d;
	}
	
	public void setLabel(String s)
	{
		this.stringLabel = s;
	}
	
	public void setDoubleLabel(double d)
	{
		this.doubleLabel = d;
	}
	
	public void setStringLabel(String s)
	{
		this.stringLabel = s;
	}
	
	public void setFeature(String field, double val)
	{
		Field[] fields = this.getClass().getFields();
		for(Field f : fields)
		{
			if(f.getName().equalsIgnoreCase(field))
			{
				f.setAccessible(true);
				try
				{
					f.set(this, val);
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
