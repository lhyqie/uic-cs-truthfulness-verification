package edu.uic.cs.t_verifier.porcess.html.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import edu.uic.cs.t_verifier.porcess.html.PageContentExtractor;

public class SegmentContentExtractor extends AbstractPageContentExtractor
{
	public List<List<String>> extractStemmedNonStopWords(String pageUrl)
	{
		String rawContent = extractRawPageContent(pageUrl);

		System.out.println(rawContent);
		System.out.println("=================================================");

		String[] segments = rawContent.split("\t*\n\t*\n");

		return tidyWordsInSegments(segments);
	}

	private List<List<String>> tidyWordsInSegments(String[] segments)
	{
		List<List<String>> result = new ArrayList<List<String>>();

		int realLineIndex = 0;
		for (String segment : segments)
		{
			segment = StringEscapeUtils.unescapeHtml(segment).trim();
			if (segment.length() == 0)
			{
				continue;
			}

			if (realLineIndex < 2)
			{
				/**
				 * escape first 2 lines:
				 * 
				 * L1: 'From Wikipedia, the free encyclopedia'
				 * L2: 'Jump to: navigation, search'
				 */
				realLineIndex++;
			}
			else
			{
				List<String> wordsInLine = splitIntoNoneStopStemmedWords(segment);
				if (!wordsInLine.isEmpty())
				{
					result.add(wordsInLine);
				}
				//				System.out.println(wordsInLine);
				//				System.out.println("***********");
			}
		}

		return result;
	}

	public static void main(String[] args)
	{
		PageContentExtractor extractor = new SegmentContentExtractor();
		//		String page = extractor
		//				.extractRawPageContent("http://en.wikipedia.org/wiki/Continent");
		//		System.out.println(page);

		// String url = "http://en.wikipedia.org/wiki/Sleepless_(2001_film)";
		// String url = "http://en.wikipedia.org/wiki/Continent";
		// String url = "http://en.wikipedia.org/wiki/China_Airlines";
		String url = "http://en.wikipedia.org/wiki/Docklands_Light_Railway";
		List<List<String>> result = extractor.extractStemmedNonStopWords(url);
		for (List<String> paragraph : result)
		{
			System.out.println(paragraph);
		}

	}
}
