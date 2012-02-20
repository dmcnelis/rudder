package me.mcnelis.rudder.ml.supervised.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.mcnelis.rudder.data.RecordInterface;
import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RecordList;

public class NaiveBayesClassification
{

	protected Map<String, List<BayesFeature>> classList = new HashMap<String, List<BayesFeature>>();

	protected IRudderList<?> records;

	public void setData(IRudderList<?> records)
	{
		
		this.records = records;
		
	}

	public void train()
	{

		for (Object r : records)
		{

			List<BayesFeature> featureList = null;
			if (!this.classList.containsKey(records.getStringLabel(r)))
			{
				featureList = new ArrayList<BayesFeature>();
			}
			else
			{
				featureList = this.classList.get(records.getStringLabel(r));
			}

			int idx = 0;
			for (Object f : records.getRecordFeatures(r))
			{
				BayesFeature bf = null;
				try
				{
					bf = featureList.get(idx);
					bf.add(f);
				}
				catch (IndexOutOfBoundsException iobe)
				{
					if (f instanceof Double)
					{
						bf = new BayesContinuousFeature();
					}
					else
					{
						bf = new BayesDiscreteFeature();
					}
					bf.add(f);
					featureList.add(bf);
				}
				idx++;
			}
			this.classList.put(records.getStringLabel(r), featureList);
		}
	}

	public Map<String, Double> getClassScores(Object r)
	{

		HashMap<String, Double> labelScores = new HashMap<String, Double>();

		for (String label : this.classList.keySet())
		{
			double scores = 0d;
			List<BayesFeature> featureList = this.classList.get(label);

			Object[] values = this.records.getRecordFeatures(r).toArray();
			int idx = 0;
			for (BayesFeature bf : featureList)
			{
				double rawScore = bf.getClassScore(values[idx]);
				if (rawScore == 0d)
				{
					rawScore = -1;
				}
				
				double score = Math.log(rawScore);
				if (Double.isNaN(score))
				{
					score = 0d;
				}

				scores += score;
				idx++;
			}
			
			labelScores.put(label, scores);
		}

		double den = 0d;
		for (Double ps : labelScores.values())
		{
			den += ps;
		}

		HashMap<String, Double> normalizedScores = new HashMap<String, Double>();

		for (Entry<String, Double> label : labelScores.entrySet())
		{
			normalizedScores.put(label.getKey(),
					labelScores.get(label.getKey()) / den);
		}

		return normalizedScores;
	}

	public String getLabel(Object r)
	{
		Map<String, Double> labelScores = this.getClassScores(r);
		double maxScore = 0d;
		String myLabel = "";
		for (Entry<String, Double> label : labelScores.entrySet())
		{
			
			if (maxScore < labelScores.get(label.getKey()))
			{
				myLabel = label.getKey();
				maxScore = labelScores.get(label.getKey());
			}
		}
		return myLabel;
	}
}
