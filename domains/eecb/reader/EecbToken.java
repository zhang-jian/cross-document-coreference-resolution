package edu.oregonstate.domains.eecb.reader;

import edu.stanford.nlp.ie.machinereading.common.StringDictionary;
import edu.stanford.nlp.trees.Span;

/**
 * Every Token in the EECB corpus
 * 
 * @author Jun Xie (xie@eecs.oregonstate.edu)
 *
 */
public class EecbToken {

	/** the actual token bytes
	 * Normally we work with mWord (see below), but mLiteral is needed when
	 * we need to check if a sequence of tokens exists in a gazetteer 
	 */
	private String mLiteral;
	
	/** The index of the literal in the WORDS hash */
	private int mWord;
	
	/** suffixes of mWord */
	private int mCase;
	
	private int mLemma;
	
	private int mPos;
	
	private int mChunk;
	
	private int mNerc;
	
	private Span mByteOffset;
	
	/** Raw byte offset in the SGM doc */
	private Span mRawByteOffset;
	
	private int mSentence;
	
	/**
	 * The three fields are needed to consider.
	 */
	/** Entity class from Massi*/
	private String mMassiClass;
	/** Entity label from the BBN corpus */
	private String mMassiBbn;
	/** WordNet super-senses detected by Massi */
	private String mMassiWnss;
	
	/** Dictionary for all words in the corpus */
	public static StringDictionary WORDS;

	/** Dictionary for all lemmas in the corpus */
	public static StringDictionary LEMMAS;

	/** Dictionary for all other strings in the corpus */
	public static StringDictionary OTHERS;
	
	public String getLiteral() {
	    return mLiteral;
	}
	
	public int getByteStart() {
	    return mByteOffset.start();
	}

	public int getByteEnd() {
	    return mByteOffset.end();
	}
	
}
