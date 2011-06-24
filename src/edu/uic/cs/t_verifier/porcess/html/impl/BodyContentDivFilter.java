package edu.uic.cs.t_verifier.porcess.html.impl;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.Div;

class BodyContentDivFilter implements NodeFilter
{
	private static final long serialVersionUID = 1L;

	private static final String HTML_TAG_DIV_ATTRIBUTE_ID = "id";
	private static final String HTML_TAG_DIV_ATTRIBUTE_ID_BODYCONTENT = "bodyContent";

	@Override
	public boolean accept(Node node)
	{
		if (node instanceof Div)
		{
			Div div = (Div) node;
			String id = div.getAttribute(HTML_TAG_DIV_ATTRIBUTE_ID);
			if (HTML_TAG_DIV_ATTRIBUTE_ID_BODYCONTENT.equals(id))
			{
				return true;
			}
		}

		return false;
	}
}
