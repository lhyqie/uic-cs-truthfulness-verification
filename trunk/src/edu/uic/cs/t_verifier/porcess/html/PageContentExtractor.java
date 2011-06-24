package edu.uic.cs.t_verifier.porcess.html;

import java.util.List;

public interface PageContentExtractor
{
	List<List<String>> extractStemmedNonStopWords(String pageUrl);
}
