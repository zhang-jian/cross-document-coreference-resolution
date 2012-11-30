package edu.oregonstate.experiment.dataset;

import java.util.logging.Logger;

import edu.oregonstate.experiment.ExperimentConstructor;
import edu.oregonstate.experiment.IDataSet;
import edu.oregonstate.featureExtractor.SrlResultIncorporation;
import edu.oregonstate.io.ResultOutput;
import edu.oregonstate.util.DocumentMerge;
import edu.oregonstate.util.EecbConstants;
import edu.stanford.nlp.dcoref.CorefCluster;
import edu.stanford.nlp.dcoref.CorefScorer;
import edu.stanford.nlp.dcoref.Document;
import edu.stanford.nlp.dcoref.ScorerPairwise;

/**
 * Based on the topic Document object, incorporate the SRL predicate and their arguments 
 * with the predicted mentions generated by Rule based mention detection component.
 * 
 * @author Jun Xie (xie@eecs.oregonstate.edu)
 *
 */

public class CrossTopic implements IDataSet {

	/** corpus path */
	private String corpusPath;
	
	/** used for scoring */
	private static final Logger logger = Logger.getLogger(WithinCross.class.getName());
	
	/** srl path */
	private String srlPath;
	
	public CrossTopic() {
	}
	
	@Override
	public Document getData(String[] topics) {
		corpusPath = (String) ExperimentConstructor.mParameters.get(EecbConstants.DATASET).get("corpusPath");
		srlPath = (String) ExperimentConstructor.mParameters.get(EecbConstants.DATASET).get("srlpath");
		
		Document corpus = new Document();
		IDocument documentExtraction = new CrossDocument();
	
		for (String topic : topics) {
			String statisPath = ExperimentConstructor.currentExperimentFolder + "/" + "statistics";
			try {
				String path = corpusPath + topic + "/";
				ResultOutput.writeTextFile(ExperimentConstructor.logFile, topic + " : " + path);
				corpus = documentExtraction.getDocument(topic);
				ResultOutput.writeTextFile(statisPath, topic + " " + corpus.allGoldMentions.size() + " " + corpus.allPredictedMentions.size() + " " +
						corpus.corefClusters.size() + " " + corpus.goldCorefClusters.size());
				ResultOutput.writeTextFile(ExperimentConstructor.logFile, "\n");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			corpus.fill();                                // incorporate SYNONYM
			
			// generate feature for each mention
		    for (Integer id : corpus.corefClusters.keySet()) {
		    	CorefCluster cluster = corpus.corefClusters.get(id);
		    	cluster.regenerateFeature();
		    }
			
			CorefScorer pairscore = new ScorerPairwise();
    		pairscore.calculateScore(corpus);
    		pairscore.printF1(logger, true);
		}

		return corpus;
	}
	
}
