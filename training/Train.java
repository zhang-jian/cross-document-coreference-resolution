package edu.oregonstate.training;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import edu.stanford.nlp.dcoref.Mention;
import edu.oregonstate.CRC_MAIN;
import edu.oregonstate.classifier.LinearRegression;
import edu.oregonstate.features.Feature;
import edu.oregonstate.io.EECBMentionExtractor;
import edu.oregonstate.io.EmentionExtractor;
import edu.oregonstate.search.IterativeResolution;
import edu.oregonstate.util.EECB_Constants;
import edu.oregonstate.util.GlobalConstantVariables;
import edu.stanford.nlp.dcoref.Constants;
import edu.stanford.nlp.dcoref.CorefCluster;
import edu.stanford.nlp.dcoref.CorefMentionFinder;
import edu.stanford.nlp.dcoref.CorefScorer;
import edu.stanford.nlp.dcoref.Document;
import edu.stanford.nlp.dcoref.ScorerBCubed;
import edu.stanford.nlp.dcoref.SieveCoreferenceSystem;
import edu.stanford.nlp.dcoref.ScorerBCubed.BCubedType;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.DefaultPaths;
import edu.stanford.nlp.stats.Counter;
import Jama.Matrix;

/**
 * train the linear regression Co-reference model
 * 
 * <p>
 * The specification of the whole process is shown in the algorithm 2 of the paper
 * 
 * @author Jun Xie (xie@eecs.oregonstate.edu)
 *
 */
public class Train {

	public static final Logger logger = Logger.getLogger(CRC_MAIN.class.getName());
	
	private static LexicalizedParser makeParser(Properties props) {
	    int maxLen = Integer.parseInt(props.getProperty(Constants.PARSER_MAXLEN_PROP, "100"));
	    String[] options = {"-maxLength", Integer.toString(maxLen)};
	    LexicalizedParser parser = LexicalizedParser.loadModel(props.getProperty(Constants.PARSER_MODEL_PROP, DefaultPaths.DEFAULT_PARSER_MODEL), options);
	    return parser;
	}
	
	// clusters results, right now, I just use the original corpus
	// Later time, I will use clustering result using EM variant where the 
	// the initial points (and the number of clusters) are selected from the clusters
	// generated by a hierarchical agglomerative clustering algorithm using geometric heuristics
	private String[] mTopics;
	private int mEpoch;
	private double mCoefficient;
	private double mLamda;
	private String[] outputFileNames = {"one.csv", "two.csv", "three.csv", "four.csv", "five.csv", "six.csv", "seven.csv", "eight.csv", "nine.csv", "ten.csv", "initial.csv"};
	public static String currentOutputFileName;
	
	public Train(String[] topics, int epoch, double coefficient, double lamda) {
		mTopics = topics;
		mEpoch = epoch;
		mCoefficient = coefficient;
		mLamda = lamda;
		currentOutputFileName = GlobalConstantVariables.RESULT_PATH + outputFileNames[10];
	}
	
	// main method for training the linear regression model, in the current time, we just use 2 roles
	// in the later time, I will change to 4 roles
	public Matrix train(Matrix initialWeight) {
		// configure the high-precision sieves
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.put("dcoref.eecb", GlobalConstantVariables.CORPUS_PATH);
		props.put("dcoref.score", "true");
		props.put("dcoref.sievePasses", "MarkRole, DiscourseMatch, ExactStringMatch, RelaxedExactStringMatch, PreciseConstructs, StrictHeadMatch1, StrictHeadMatch2, StrictHeadMatch3, StrictHeadMatch4, RelaxedHeadMatch");
		
		Matrix model = initialWeight;
		// training data for linear regression
		for (int i = 0; i < mEpoch; i++) {
			logger.info("Start train the initial model:"+ i +"th iteration ============================================================");
			currentOutputFileName = GlobalConstantVariables.RESULT_PATH + outputFileNames[i];
			// all mentions in one doc cluster
			for (String topic : mTopics) {
				try {
				    SieveCoreferenceSystem corefSystem = new SieveCoreferenceSystem(props);
				    LexicalizedParser parser = makeParser(props);
				    EmentionExtractor mentionExtractor = null;
				    mentionExtractor = new EECBMentionExtractor(topic, parser, corefSystem.dictionaries(), props, corefSystem.semantics());
				    assert mentionExtractor != null;
				    if (!EECB_Constants.USE_GOLD_MENTIONS) {
				    	String mentionFinderClass = props.getProperty(Constants.MENTION_FINDER_PROP);
				    	if (mentionFinderClass != null) {
				            String mentionFinderPropFilename = props.getProperty(Constants.MENTION_FINDER_PROPFILE_PROP);
				            CorefMentionFinder mentionFinder;
				            if (mentionFinderPropFilename != null) {
				              Properties mentionFinderProps = new Properties();
				              mentionFinderProps.load(new FileInputStream(mentionFinderPropFilename));
				              mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).getConstructor(Properties.class).newInstance(mentionFinderProps);
				            } else {
				              mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).newInstance();
				            }
				            mentionExtractor.setMentionFinder(mentionFinder);
				    	}
				    	if (mentionExtractor.mentionFinder == null) {
				            logger.warning("No mention finder specified, but not using gold mentions");
				    	}
				    }
				   
				    // Parse one document at a time, and do single-doc coreference resolution in each
				    Document document = mentionExtractor.inistantiate(topic);
				    corefSystem.coref(document);
				    
				    
				    IterativeResolution ir = new IterativeResolution(document, corefSystem.dictionaries(), model);
				    ir.merge(document, corefSystem.dictionaries());
				    if(corefSystem.doScore()){
				        //Identifying possible coreferring mentions in the corpus along with any recall/precision errors with gold corpus
				    	System.out.println("Bcubed score");
				    	CorefScorer score = new ScorerBCubed(BCubedType.Bconll);
				    	score.calculateScore(document);
				    	System.out.println(score.getF1());
				    	System.out.println(score.getPrecision());
				    	System.out.println(score.getRecall());
				    	
				    }
				    
				    LinearRegression lr = new LinearRegression(currentOutputFileName);
					Matrix updateModel = lr.calculateWeight();
					
					updateModel = updateModel.times(1 - mLamda);
					model = model.times(mLamda);
					model = model.plus(updateModel);
				    
				}catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			logger.info("Finish train the model:"+ i +"th iteration ===================================================");
		}
		
		return model;
	}
	
	/**
	 * This procedure runs the high-precision sieves introduced just like the data generation loop in algorithm 2, creates training examples from 
	 * the clusters available after every merge operation. Since these deterministic models address only nominal clusters, at the end we generate 
	 * training data for events by inspecting all the pairs of singleton verbal clusters. Using this data, we train the initial linear regression model.
	 * 
	 * @return the initial weight set
	 */
	public Matrix assignInitialWeights() {
		logger.info("Start train the initial model: ============================================================");
	
		// configure the high-precision sieves
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.put("dcoref.eecb", GlobalConstantVariables.CORPUS_PATH);
		props.put("dcoref.score", "true");
		props.put("dcoref.sievePasses", "MarkRole, DiscourseMatch, ExactStringMatch, RelaxedExactStringMatch, PreciseConstructs, StrictHeadMatch1, StrictHeadMatch2, StrictHeadMatch3, StrictHeadMatch4, RelaxedHeadMatch");
		
		// all mentions in one doc cluster
		for (String topic : mTopics) {
			try {
			    SieveCoreferenceSystem corefSystem = new SieveCoreferenceSystem(props);
			    LexicalizedParser parser = makeParser(props);
			    EmentionExtractor mentionExtractor = null;
			    mentionExtractor = new EECBMentionExtractor(topic, parser, corefSystem.dictionaries(), props, corefSystem.semantics());
			    assert mentionExtractor != null;
			    if (!EECB_Constants.USE_GOLD_MENTIONS) {
			    	String mentionFinderClass = props.getProperty(Constants.MENTION_FINDER_PROP);
			    	if (mentionFinderClass != null) {
			            String mentionFinderPropFilename = props.getProperty(Constants.MENTION_FINDER_PROPFILE_PROP);
			            CorefMentionFinder mentionFinder;
			            if (mentionFinderPropFilename != null) {
			              Properties mentionFinderProps = new Properties();
			              mentionFinderProps.load(new FileInputStream(mentionFinderPropFilename));
			              mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).getConstructor(Properties.class).newInstance(mentionFinderProps);
			            } else {
			              mentionFinder = (CorefMentionFinder) Class.forName(mentionFinderClass).newInstance();
			            }
			            mentionExtractor.setMentionFinder(mentionFinder);
			    	}
			    	if (mentionExtractor.mentionFinder == null) {
			            logger.warning("No mention finder specified, but not using gold mentions");
			    	}
			    }
			   
			    // Parse one document at a time, and do single-doc coreference resolution in each
			    Document document = mentionExtractor.inistantiate(topic);
			    corefSystem.coref(document);
			    
			    // inspect all the pairs of singleton verbal clusters
			    Map<Integer, CorefCluster> corefClusters = document.corefClusters;
			    List<CorefCluster> verbSingletonCluster = new ArrayList<CorefCluster>();  // add the singleton verbal cluster
			    for (Integer clusterid : corefClusters.keySet()) {
			    	CorefCluster cluster = corefClusters.get(clusterid);
			    	Set<Mention> mentions = cluster.getCorefMentions();
			    	if (mentions.size() > 1) {
			    		continue;
			    	} else {
			    		for (Mention mention : mentions) {
			    			boolean isVerb = mention.isVerb;
			    			if (isVerb) {
				    			verbSingletonCluster.add(cluster);
				    			break;
			    			}
			    		}
			    	}
			    }
			    
			    Map<Integer, Mention> goldCorefClusters = document.allGoldMentions; // use the gold coref cluster to calculate the quality for this merge
			    for (int i = 0; i < verbSingletonCluster.size(); i++) {
			    	for (int j = 0; j < i; j++) {
			    		CorefCluster ci = verbSingletonCluster.get(i);
			    		CorefCluster cj = verbSingletonCluster.get(j);
			    		Counter<String> features = Feature.getFeatures(document, ci, cj, false, corefSystem.dictionaries()); // get the feature
			    		Mention ciFirstMention = ci.getFirstMention();
			    		Mention cjFirstMention = cj.getFirstMention();
			    		double correct = 0.0;
			    		double total = 1.0;
			    		
			    		if (goldCorefClusters.containsKey(ciFirstMention.mentionID) && goldCorefClusters.containsKey(cjFirstMention.mentionID)) {
							if (goldCorefClusters.get(ciFirstMention.mentionID).goldCorefClusterID == goldCorefClusters.get(cjFirstMention.mentionID).goldCorefClusterID) {
								correct += 1.0;
							}
			    		}
			    		double quality = correct / total;
			    		String record = buildString(features, quality);
			    		writeTextFile(currentOutputFileName, record);
			    		
			    	}
			    }
			    
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		LinearRegression lr = new LinearRegression(currentOutputFileName);
		Matrix initialModel = lr.calculateWeight();
		
		logger.info("Finish train the initial model: ===================================================");
		return initialModel;
	}
	
	public static String buildString(Counter<String> features, double quality) {
		StringBuilder sb = new StringBuilder();
		for (String key : features.keySet()) {
			double value = features.getCount(key);
			sb.append(value + ",");
		}
		sb.append(quality + "\n");
		return sb.toString();
	}
	
	public static void writeTextFile(String fileName, String s) {
	    try {
	    	BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
	        out.write(s);
	        out.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
	}
	
}
