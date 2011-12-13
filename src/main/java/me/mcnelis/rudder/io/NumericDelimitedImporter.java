package me.mcnelis.rudder.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import me.mcnelis.rudder.exceptions.IllegalRecordException;
import me.mcnelis.rudder.features.FeatureList;
import me.mcnelis.rudder.features.NumericFeature;
import me.mcnelis.rudder.records.DoubleRecord;
import me.mcnelis.rudder.records.RecordSet;
import au.com.bytecode.opencsv.CSVReader;


public class NumericDelimitedImporter implements Import {

	protected String fileUri;
	protected File file;
	protected Character delim = ',';
	protected Character quoteChar = '"';
	protected Character escapeChar = '\\';
	protected Integer skipLineNumber = 0;
	protected Boolean strictQuotes = false;
	protected Boolean ignoreLeadingWhitespace = false;
	protected Integer sampleFieldIndex;
	
	public RecordSet loadRecords() {
		CSVReader reader = null;
		try {
			reader = new CSVReader(
					new FileReader(this.fileUri),
							this.delim,
							this.quoteChar,
							this.escapeChar,
							this.skipLineNumber
							);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		};
		List<String[]> fileValues = null;
		try {
			fileValues = reader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		RecordSet set = new RecordSet();
		
		for (String[] fields : fileValues) {
			DoubleRecord record = new DoubleRecord();
			FeatureList features = new FeatureList();
			for (int i=0; i<fields.length; i++) {
				Double fieldVal = null;
				try {
					fieldVal = Double.parseDouble(fields[i]);
				} catch (NumberFormatException nfe) {
					fieldVal = Double.NaN;
				}
				if (this.sampleFieldIndex != null && i == this.sampleFieldIndex)
					record.setKey(fieldVal);
				else {
					features.add(new NumericFeature(("field" + i), fieldVal));
				}
			}
			record.setFeatures(features);
			try {
				set.addAndCheck(record);
			} catch (IllegalRecordException e) {
				continue;
			}
		}
		
		return set;
	}

	public void setData(Object o) {

		if (!(o instanceof String)) {
			return;
		}
		
		this.fileUri = (String) o;
	}
	
	public void setDelimiter(char delim) {
		this.delim = delim;
	}

	/**
	 * @return the fileUri
	 */
	public String getFileUri() {
		return fileUri;
	}

	/**
	 * @param fileUri the fileUri to set
	 */
	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the delim
	 */
	public Character getDelim() {
		return delim;
	}

	/**
	 * @param delim the delim to set
	 */
	public void setDelim(Character delim) {
		this.delim = delim;
	}

	/**
	 * @return the quoteChar
	 */
	public Character getQuoteChar() {
		return quoteChar;
	}

	/**
	 * @param quoteChar the quoteChar to set
	 */
	public void setQuoteChar(Character quoteChar) {
		this.quoteChar = quoteChar;
	}

	/**
	 * @return the escapeChar
	 */
	public Character getEscapeChar() {
		return escapeChar;
	}

	/**
	 * @param escapeChar the escapeChar to set
	 */
	public void setEscapeChar(Character escapeChar) {
		this.escapeChar = escapeChar;
	}

	/**
	 * @return the skipLineNumber
	 */
	public Integer getSkipLineNumber() {
		return skipLineNumber;
	}

	/**
	 * @param skipLineNumber the skipLineNumber to set
	 */
	public void setSkipLineNumber(Integer skipLineNumber) {
		this.skipLineNumber = skipLineNumber;
	}

	/**
	 * @return the strictQuotes
	 */
	public Boolean getStrictQuotes() {
		return strictQuotes;
	}

	/**
	 * @param strictQuotes the strictQuotes to set
	 */
	public void setStrictQuotes(Boolean strictQuotes) {
		this.strictQuotes = strictQuotes;
	}

	/**
	 * @return the ignoreLeadingWhitespace
	 */
	public Boolean getIgnoreLeadingWhitespace() {
		return ignoreLeadingWhitespace;
	}

	/**
	 * @param ignoreLeadingWhitespace the ignoreLeadingWhitespace to set
	 */
	public void setIgnoreLeadingWhitespace(Boolean ignoreLeadingWhitespace) {
		this.ignoreLeadingWhitespace = ignoreLeadingWhitespace;
	}
	
	/**
	 * 
	 * @param Index of field to use as predicted value, starting with 0 for the first column
	 */
	public void setExampleField(int idx) {
		this.sampleFieldIndex = idx;
	}

}
