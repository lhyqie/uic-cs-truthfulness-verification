package edu.uic.cs.t_verifier.porcess.html.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import edu.uic.cs.t_verifier.porcess.html.PageContentExtractor;

public class ParagraphContentExtractor extends AbstractPageContentExtractor
{
	public List<List<String>> extractStemmedNonStopWords(String pageUrl)
	{
		String rawContent = extractRawPageContent(pageUrl);

		//		System.out.println(rawContent);
		//		System.out.println("=================================================");

		String[] paragraphs = rawContent.split("\t*\n\t*\n");

		return tidyWordsInParagraphs(paragraphs);
	}

	private List<List<String>> tidyWordsInParagraphs(String[] paragraphs)
	{
		List<List<String>> result = new ArrayList<List<String>>();

		int realLineIndex = 0;
		for (String paragraph : paragraphs)
		{
			paragraph = StringEscapeUtils.unescapeHtml(paragraph).trim();
			if (paragraph.length() == 0)
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
				List<String> wordsInLine = splitIntoNoneStopStemmedWords(paragraph);
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
		PageContentExtractor extractor = new ParagraphContentExtractor();
		//		String page = extractor
		//				.extractRawPageContent("http://en.wikipedia.org/wiki/Continent");
		//		System.out.println(page);

		// String url = "http://en.wikipedia.org/wiki/Sleepless_(2001_film)";
		// String url = "http://en.wikipedia.org/wiki/Continent";
		String url = "http://en.wikipedia.org/wiki/China_Airlines";
		List<List<String>> result = extractor.extractStemmedNonStopWords(url);
		for (List<String> paragraph : result)
		{
			System.out.println(paragraph);
		}

	}
}
