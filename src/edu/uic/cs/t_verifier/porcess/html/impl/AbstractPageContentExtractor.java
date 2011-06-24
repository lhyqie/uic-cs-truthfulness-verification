package edu.uic.cs.t_verifier.porcess.html.impl;

import org.htmlparser.Node;
import org.htmlparser.filters.TagNameFilter;

import edu.uic.cs.t_verifier.misc.GeneralException;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;
import edu.uic.cs.t_verifier.porcess.html.PageContentExtractor;

abstract class AbstractPageContentExtractor extends AbstractWordOperations
		implements PageContentExtractor
{
	private PoliteParser parser = new PoliteParser();

	protected String extractRawPageContent(String pageUrl)
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
}
