package me.mcnelis.rudder.ml.supervised.classification;

import java.util.ArrayList;
import java.util.HashMap;

import me.mcnelis.rudder.data.RecordInterface;
import me.mcnelis.rudder.data.collections.RecordList;

public class NaiveBayesClassification {
	
	
	protected HashMap<String, ArrayList<BayesFeature>> classList = new HashMap<String, ArrayList<BayesFeature>>();
	
	protected RecordList<? extends RecordInterface> records;
	
	public void setData(RecordList<? extends RecordInterface> records) {
		this.records = records;
	}
	
	public void train() {
		
		for (RecordInterface r : records) {
			
			ArrayList<BayesFeature> featureList = null;
			if (!this.classList.containsKey(r.getLabel())) {
				featureList = new ArrayList<BayesFeature>();
			} else {
				featureList = this.classList.get(r.getLabel());
			}
			
			int idx = 0;
			for (Object f : r.getAllFeatures()) {
				BayesFeature bf = null;
				try {
					bf = featureList.get(idx);
					bf.add(f);	
				} catch (IndexOutOfBoundsException iobe) {
					if (f instanceof Double)
						bf = new BayesContinuousFeature();
					else
						bf = new BayesDiscreteFeature();
					bf.add(f);	
					featureList.add(bf);
				}	
				idx++;
			}
			this.classList.put(r.getLabel(), featureList);
		}
	}
	
	public HashMap<String, Double> getClassScores(RecordInterface r) {
		
		HashMap<String, Double> labelScores = new  HashMap<String, Double>();
		
		for (String label : this.classList.keySet()) {
			double scores = 0d;
			ArrayList<BayesFeature> featureList = this.classList.get(label);
			
			Object[] values = r.getAllFeatures();
			int idx = 0;
			for(BayesFeature bf :  featureList) {
				double rawScore = bf.getClassScore(values[idx]);
				if(rawScore == 0d) 
					rawScore = -1;
				double score = Math.log(rawScore);
				if (Double.isNaN(score))
					score = 0d;
				
				scores += score;
				idx++;
			}
			labelScores.put(label, scores);
		}
		
		double den = 0d;
		for(Double ps : labelScores.values()) {
			den += ps;
		}
		
		HashMap<String, Double> normalizedScores = new  HashMap<String, Double>();
		
		for (String label : labelScores.keySet()) {
			normalizedScores.put(label, labelScores.get(label) / den);
		}
		
		return normalizedScores;
	}
	
	public String getLabel(RecordInterface r) {
		HashMap<String, Double> labelScores = this.getClassScores(r);
		double maxScore = 0d;
		String myLabel = "";
		for (String label : labelScores.keySet()) {
			if (maxScore < labelScores.get(label)) {
				myLabel = label;
				maxScore = labelScores.get(label);
			}
		}
		return myLabel;
	}
}
