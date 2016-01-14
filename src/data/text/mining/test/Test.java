package data.text.mining.test;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import data.text.mining.core.ChunkAdjuster;
import data.text.mining.core.PosAnnotator;
import data.text.mining.core.SentencerAnnotator;
import data.text.mining.core.ShallowParser;
import data.text.mining.core.TokenAnnotator;
import data.text.mining.documents.DocumentsReader;
import data.text.mining.documents.TextPreprocessor;
import data.text.mining.exception.SystemException;
import data.text.mining.model.FileAnnotation;


public class Test {
	static final Logger logger = LogManager.getLogger(Test.class.getName());
	public static void main(String [] args){
		DocumentsReader docReader = new DocumentsReader();
		docReader.setFilePath("D:\\ResumeParsing\\text");
		File[] inputFiles;
		try {
			inputFiles = docReader.getFiles();
			System.out.println(inputFiles.length);
			for (File file : inputFiles) {
				//get annotations
				logger.info("processing "+file.getName());
				FileAnnotation annFile = new FileAnnotation();
				String fileName = file.getName();
				annFile.setFileName(fileName);
				TextPreprocessor preprocessor = new TextPreprocessor();
				String fileText = preprocessor.processTextFile(file);
				annFile.setCoveredText(fileText);
				//get sentence
				SentencerAnnotator sentAnnotator = new SentencerAnnotator();
				sentAnnotator.annotate(annFile);
				//get tokens
				TokenAnnotator tokenAnnotator = new TokenAnnotator();
				tokenAnnotator.annotate(annFile);
				//pos
				PosAnnotator posAnnotator = new PosAnnotator();
				posAnnotator.annotate(annFile);
				//System.out.println(annFile.getClass());
				//get chunks
				ShallowParser sp = new ShallowParser();
				sp.annotate(annFile);
				//Chunke merger
				ChunkAdjuster chunkadjuster = new ChunkAdjuster();
				chunkadjuster.annotate(annFile);
				Gson gson = new Gson();
				String json = gson.toJson(annFile);
				System.out.println(json);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
}