package data.text.mining.annotator;

import data.text.mining.model.Annotation;
import data.text.mining.model.FileAnnotation;

public interface Annotator {
	
	
	public Annotation annotate();
	public FileAnnotation annotate(FileAnnotation annFile);

}
