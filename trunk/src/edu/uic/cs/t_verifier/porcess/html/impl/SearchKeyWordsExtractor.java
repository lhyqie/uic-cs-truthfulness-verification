package edu.uic.cs.t_verifier.porcess.html.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import edu.uic.cs.t_verifier.data.SearchKeyWords;
import edu.uic.cs.t_verifier.data.SearchKeyWords.AmbiguityEntry;
import edu.uic.cs.t_verifier.misc.GeneralException;
import edu.uic.cs.t_verifier.misc.LogHelper;

public class SearchKeyWordsExtractor
{
	private static final Logger LOGGER = LogHelper
			.getLogger(SearchKeyWordsExtractor.class);

	private static final String WIKI_SEARCH_URL_PREFIX = "http://en.wikipedia.org/wiki/Special:Search/";
	private static final String WIKI_URL_PREFIX = "/wiki/";
	private static final int WIKI_URL_PREFIX_LENGTH = WIKI_URL_PREFIX.length();

	//	private static final String HTML_TAG_HTML = "HTML";
	//	private static final String HTML_TAG_HEAD = "HEAD";
	//	private static final String HTML_TAG_TITLE = "TITLE";

	//	private static final String HTML_TAG_DIV_ATTRIBUTE_ID = "id";
	//	private static final String HTML_TAG_DIV_ATTRIBUTE_ID_BODYCONTENT = "bodyContent";

	private static final String HTML_TEXT_AMBIGUITY = " may refer to:";

	//	private static class BodyContentDivFilter implements NodeFilter
	//	{
	//		private static final long serialVersionUID = 1L;
	//
	//		@Override
	//		public boolean accept(Node node)
	//		{
	//			if (node instanceof Div)
	//			{
	//				Div div = (Div) node;
	//				String id = div.getAttribute(HTML_TAG_DIV_ATTRIBUTE_ID);
	//				if (HTML_TAG_DIV_ATTRIBUTE_ID_BODYCONTENT.equals(id))
	//				{
	//					return true;
	//				}
	//			}
	//
	//			return false;
	//		}
	//	}

	private static class AmbiguityTagNodeFilter implements NodeFilter
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node)
		{
			if (node instanceof Text
					&& node.toPlainTextString().equals(HTML_TEXT_AMBIGUITY))
			{
				return true;
			}

			return false;
		}
	}

	private static class PossibleKeyWordsFilter implements NodeFilter
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node)
		{
			if (node instanceof Tag
					&& ((Tag) node).getRawTagName().equals("li")
					&& (node.getChildren() != null || node.getChildren().size() > 0))
			{
				return true;
			}

			return false;
		}
	}

	private PoliteParser parser = new PoliteParser();

	public SearchKeyWords getStandardSearchKeyWords(String queryWords)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(LogHelper.LOG_LAYER_ONE_BEGIN + "Trying key word["
					+ queryWords + "]. ");
		}

		SearchKeyWords result = getStandardSearchKeyWordsInternal(queryWords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(LogHelper.LOG_LAYER_ONE_END + "Trying key word["
					+ queryWords + "]. Result[" + result + "]. ");
		}

		return result;
	}

	private SearchKeyWords getStandardSearchKeyWordsInternal(String queryWords)
	{
		String url = WIKI_SEARCH_URL_PREFIX
				+ queryWords.trim().replace(' ', '_');

		try
		{
			System.out.print(url);
			parser.setResource(url);

			// this means the input query words can't exactly match a page 
			if (url.equals(parser.getURL()))
			{
				System.out.println(" ×");
				return null;
			}

			Node htmlNode = parser.parse(
					new TagNameFilter(HtmlConstant.HTML_TAG_HTML)).elementAt(0);

			Node headNode = htmlNode
					.getChildren()
					.extractAllNodesThatMatch(
							new TagNameFilter(HtmlConstant.HTML_TAG_HEAD))
					.elementAt(0);
			Node titleNode = headNode
					.getChildren()
					.extractAllNodesThatMatch(
							new TagNameFilter(HtmlConstant.HTML_TAG_TITLE))
					.elementAt(0);

			// successfully retrieved title looks like this:  
			// Sleepless in Seattle - Wikipedia, the free encyclopedia
			String titleString = titleNode.toPlainTextString();
			titleString = titleString
					.substring(0, titleString.lastIndexOf('-')).trim();

			Node bodyContentDiv = htmlNode.getChildren()
					.extractAllNodesThatMatch(new BodyContentDivFilter(), true)
					.elementAt(0);
			// there may be ambiguous key words
			List<AmbiguityEntry> ambiguityEntries = extractAmbiguityEntries(bodyContentDiv
					.getChildren());

			SearchKeyWords result = new SearchKeyWords(titleString.replaceAll(
					" ", "_"), ambiguityEntries);
			if (result.isCertainly())
			{
				System.out.println(" √");
			}
			else
			{
				System.out.println(" ?");
			}

			return result;

		}
		catch (ParserException e)
		{
			throw new GeneralException(e);
		}
	}

	private List<AmbiguityEntry> extractAmbiguityEntries(
			NodeList nodeListInBodyContentDiv)
	{
		if (!isThereAmbiguousKeyWords(nodeListInBodyContentDiv))
		{
			// there's no ambiguous key words
			return Collections.emptyList();
		}

		NodeList allAmbiguousBulletList = nodeListInBodyContentDiv
				.extractAllNodesThatMatch(new TagNameFilter("ul"));
		NodeList allAmbiguousbullets = allAmbiguousBulletList
				.extractAllNodesThatMatch(new PossibleKeyWordsFilter(), true);

		List<AmbiguityEntry> result = new ArrayList<AmbiguityEntry>();
		for (int index = 0; index < allAmbiguousbullets.size(); index++)
		{
			Bullet bullet = (Bullet) allAmbiguousbullets.elementAt(index);
			String alternativeKeyWord = extractAlternativeKeyWord(bullet);
			String description = extractDescription(bullet);
			//			if (alternativeKeyWord != null)
			//			{
			result.add(new AmbiguityEntry(alternativeKeyWord, description));
			//			}
		}

		return result;
	}

	private String extractDescription(Bullet bullet)
	{
		StringBuilder stringRepresentation = new StringBuilder();
		for (SimpleNodeIterator e = bullet.children(); e.hasMoreNodes();)
		{
			Node node = e.nextNode();
			// Only process one level
			if (node instanceof BulletList)
			{
				continue;
			}
			stringRepresentation.append(node.toPlainTextString());
		}
		return stringRepresentation.toString().trim();
	}

	private String extractAlternativeKeyWord(Bullet bullet)
	{
		LinkTag linkTag = (LinkTag) bullet.getChildren()
				.extractAllNodesThatMatch(new TagNameFilter("a")).elementAt(0);
		if (linkTag == null)
		{
			return null;
		}

		String link = linkTag.getLink().trim();

		int brginIndex = link.indexOf(WIKI_URL_PREFIX);
		if (brginIndex < 0)
		{
			return null;
		}
		else
		{
			return link.substring(brginIndex + WIKI_URL_PREFIX_LENGTH);
		}
	}

	private boolean isThereAmbiguousKeyWords(NodeList nodeListInBodyContentDiv)
	{
		NodeList ambiguityTag = nodeListInBodyContentDiv
				.extractAllNodesThatMatch(new AmbiguityTagNodeFilter(), true);

		return ambiguityTag.size() != 0;
	}
}
