package me.mcnelis.ml.rudder;

public class DoubleRecord extends Record {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Double key;
	
	public void setKey(Double d) {
		this.key = d;
	}
	public Double getKey() {
		return this.key;
	}
	
	public int compareTo(DoubleRecord o) {
		return key.compareTo(o.getKey());
	}

	public int compareTo(Record o) {
		if(o instanceof DoubleRecord)
			return compareTo((DoubleRecord) o);
		return 0;
	}

}
