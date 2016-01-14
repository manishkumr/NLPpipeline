package data.text.mining.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.text.mining.annotator.Annotator;
import data.text.mining.model.Annotation;
import data.text.mining.model.FileAnnotation;
import data.text.mining.model.SentenceAnnotation;
import data.text.mining.model.TokenAnnotation;

public class PosAnnotator implements Annotator{
	private static final Logger logger = LogManager.getLogger(PosAnnotator.class.getName());
	private final InputStream posModelInput = PosAnnotator.class.getResourceAsStream("/en-pos-maxent.bin");
	private POSTaggerME postagger = null;
	@Override
	public Annotation annotate() {
		return null;
	}

	@Override
	public FileAnnotation annotate(FileAnnotation annFile) {
		logger.info("geting pos tags");
		POSModel posModel = null;
		try {
			posModel = new POSModel(posModelInput);
			postagger = new POSTaggerME(posModel);
		} catch (IOException e) {
			logger.error(e.getMessage());

		}
		logger.info("loade pos model");
		if(postagger!=null){
			List<SentenceAnnotation> sentenceList = annFile.getSentenceAnnList();
			for (SentenceAnnotation sentenceAnnotation : sentenceList) {
				List<TokenAnnotation> tokens = sentenceAnnotation.getTokenList();
				String [] tokenArray = getTokens(tokens);
				getPosTags(tokenArray,tokens);
				
				/*List<TokenAnnotation> normTokens = sentenceAnnotation.getNormalizedTokenList();
				String [] normTokenArray = getTokens(normTokens);
				getPosTags(normTokenArray, normTokens);*/
			}
		}
		return annFile;
	}

	private void getPosTags(String[] tokenArray, List<TokenAnnotation> tokens) {
		String [] posTags = postagger.tag(tokenArray);
		for (int i = 0; i < tokens.size(); i++) {
			TokenAnnotation token = tokens.get(i);
			if(token!=null){
				//logger.debug(token.getCoveredText()+"/"+posTags[i]);
				token.setPos(posTags[i]);
			}
		} 
		
	}

	/**
	 * @param tokens
	 * @return tokensArray String Array of tokens
	 */
	private String[] getTokens(List<TokenAnnotation> tokens) {
		String [] tokensArray = new String[tokens.size()] ;
		int i = 0;
		for (TokenAnnotation tokenAnnotation : tokens) {
			tokensArray[i]= tokenAnnotation.getCoveredText();
			i++;
		}
		return tokensArray;
	}

}
