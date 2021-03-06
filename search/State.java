package edu.oregonstate.search;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import edu.oregonstate.features.FeatureFactory;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;

/**
 * represent the state  
 * 
 * every state consists of a set of CorefClusters, in order to make it generic
 * 
 * @author Jun Xie (xie@eecs.oregonstate.edu)
 *
 */
public class State<T> implements Serializable {
	
	private static final long serialVersionUID = 8666265337578515592L;
	
	/** state */
	private Map<Integer, T> state;
	
	/** id */
	private String id;
	
	/** features */
	private Counter<String> mfeatures;
	
	/* numerical feature used to calculate the heuristic score */
	private double[] numericalFeatures;
	
	/** metric score, respectively F1, Precision and Recall */
	private double[] mMetricScore;
	
	/** cost score */
	private double mCostScore;
	
	/** score detail information, PrecisionNum, PrecisionDen, RecallNum, RecallDen, which can be used to calculate the overall performance of the algorithm */
	private String scoreDetailInformation;
	
	/* feature string of the state which is used to debug */
	private String featureString;

	/* the metric F1 score, for example, if we use Pairwise as metric score, then the F1score is Pairwise F1 score */
	private double F1score;
	
	/* description */
	private String actionDescription;
	
	/* predicted centroid of the ith cluster */
	private HashMap<String, ClassicCounter<String>> mToClusterPredictedCentroid;
	
	/* predicted centroid of the jth cluster */
	private HashMap<String, ClassicCounter<String>> mFromClusterPredictedCentroid;

	public State() {
		state = new HashMap<Integer, T>();
		id = "";
		mfeatures = new ClassicCounter<String>();
		mMetricScore = new double[3];
		mCostScore = 0.0;
		scoreDetailInformation = "";
		F1score = 0.0;
		featureString = "";
		numericalFeatures = new double[FeatureFactory.getFeatureTemplate().size()];
		actionDescription = "";
	}
	
	/* set numerical feature which is used for Perceptron update */
	public void setNumericalFeatures(double[] features) {
		assert features.length == numericalFeatures.length;
		System.arraycopy(features, 0, numericalFeatures, 0, features.length);
	}
	
	/* get numerical feature */
	public double[] getNumericalFeatures() {
		return numericalFeatures;
	}
	
	/* set metric F1 score */
	public void setF1Score(double score) {
		F1score = score;
	}
	
	/* get metric F1 score */
	public double getF1Score() {
		return F1score;
	}
	
	/* set score detail information */
	public void setScoreDetailInformation(String scoreInformation) {
		scoreDetailInformation = scoreInformation;
	}
	
	/* get score detail information */
	public String getScoreDetailInformation() {
		return scoreDetailInformation;
	}
	
	/* set features */
	public void setFeatures(Counter<String> featrues) {
		mfeatures = featrues;
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		List<String> features = FeatureFactory.getFeatureTemplate();
		for (int i = 0; i < features.size(); i++) {
			String feature = features.get(i);
			double value = mfeatures.getCount(feature);
			numericalFeatures[i] = value;
			if (value != 0) {
				if (i == features.size() - 1) {
					sb.append(feature + " : " + value);
				} else {
					sb.append( feature + " : " + value + ", ");
				}
			}
		}
		
		sb.append("]");
		
		featureString = sb.toString().trim();
	}
	
	/* get features */
	public Counter<String> getFeatures() {
		return mfeatures;
	}
	
	/* return feature string */
	public String featureString() {
		return featureString;
	}
	
	/* add element to state */
	public void add(Integer i, T element) {
		state.put(i, element);
	}

	/* get the ith index element */
	public T get(int i) {
		return state.get(i);
	}

	/* remove the ith index element */
	public void remove(int i) {
		state.remove(i);
	}
	
	/* get state */
	public Map<Integer, T> getState() {
		return state;
	}
	
	/* set the id for the state */
	public void setID(String val) {
		this.id = val;
	}
	
	/* get the id of the state */
	public String getID() {
		return this.id;
	}
	
	public void setActionDescription(String action) {
		actionDescription = action;
	}
	
	public String getActionDescription() {
		return actionDescription;
	}
	
	/* set metric score */
	public void setScore(double[] score) {
		mMetricScore = score;
	}
	
	/* get metric score */
	public double[] getScore() {
		return mMetricScore;
	}
	
	/* set cost score of the state */
	public void setCostScore(double score) {
		mCostScore = score;
	}
	
	/* get cost score of the state */
	public double getCostScore() {
		return mCostScore;
	}
	
	/* set and get predicted centroid for to and from cluster */
	public void setToClusterPredictedCentroid( HashMap<String, ClassicCounter<String>> toClusterPredictedCentroid) {
		mToClusterPredictedCentroid = toClusterPredictedCentroid;
	}
	
	public void setFromClusterPredictedCentroid( HashMap<String, ClassicCounter<String>> fromClusterPredictedCentroid) {
		mFromClusterPredictedCentroid = fromClusterPredictedCentroid;
	}
	
	public HashMap<String, ClassicCounter<String>> getToClusterPredictedCentroid() {
		return mToClusterPredictedCentroid;
	}
	
	public HashMap<String, ClassicCounter<String>> getFromClusterPredictedCentroid() {
		return mFromClusterPredictedCentroid;
	}
	
	// This one needs to be take cared
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			@SuppressWarnings("unchecked")
			State<T> s = (State<T>) obj;
			Map<Integer, T> sList = s.state;
			if (sList.size() == state.size()) {
				Set<T> objValues = new HashSet<T>(s.getState().values());
				Set<T> stateValues = new HashSet<T>(state.values());
				boolean equal = objValues.equals(stateValues);
				return equal;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("state: \n");
		for (Integer key : state.keySet()) {
			sb.append(state.get(key).toString() + " \n");
		}
		
		return sb.toString().trim();
	}
	
}
