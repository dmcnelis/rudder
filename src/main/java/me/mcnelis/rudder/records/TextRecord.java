package me.mcnelis.rudder.records;

public class TextRecord extends Record {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String key;
	
	public void setKey(String s) {
		this.key = s;
	}
	public String getKey() {
		return this.key;
	}
	public int compareTo(TextRecord o) {
		return key.compareTo(o.getKey());
	}

	public int compareTo(Record o) {
		if(o instanceof TextRecord)
			return compareTo((TextRecord) o);
		return 0;
	}
}
