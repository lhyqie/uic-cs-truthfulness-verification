package edu.uic.cs.t_verifier.porcess.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.htmlparser.Node;
import org.htmlparser.filters.TagNameFilter;

import edu.uic.cs.t_verifier.misc.GeneralException;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;

public class PageContentExtractor extends AbstractWordOperations
{
	private PoliteParser parser = new PoliteParser();

	private String extractRawPageContent(String pageUrl)
	{
		try
		{
			parser.setResource(pageUrl);
			Node htmlNode = parser.parse(
					new TagNameFilter(HtmlConstant.HTML_TAG_HTML)).elementAt(0);
			Node bodyContentDiv = htmlNode.getChildren()
					.extractAllNodesThatMatch(new BodyContentDivFilter(), true)
					.elementAt(0);
			String rawBodyContent = bodyContentDiv.toPlainTextString();

			return rawBodyContent;

		}
		catch (Exception e)
		{
			throw new GeneralException(e);
		}
	}

	public List<List<String>> extractStemmedNonStopWordsInAllParagraphs(
			String pageUrl)
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
		PageContentExtractor extractor = new PageContentExtractor();
		//		String page = extractor
		//				.extractRawPageContent("http://en.wikipedia.org/wiki/Continent");
		//		System.out.println(page);

		// String url = "http://en.wikipedia.org/wiki/Sleepless_(2001_film)";
		// String url = "http://en.wikipedia.org/wiki/Continent";
		String url = "http://en.wikipedia.org/wiki/China_Airlines";
		List<List<String>> result = extractor
				.extractStemmedNonStopWordsInAllParagraphs(url);
		for (List<String> paragraph : result)
		{
			System.out.println(paragraph);
		}

	}
}
