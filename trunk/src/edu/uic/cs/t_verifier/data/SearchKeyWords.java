package edu.uic.cs.t_verifier.data;

import java.util.List;

public class SearchKeyWords
{
	private static final long serialVersionUID = 1L;

	public static final String WIKI_ADDRESS_PREFIX = "http://en.wikipedia.org/wiki/";

	private String keyWord = null;
	private List<AmbiguityEntry> ambiguousEntries = null;

	public static class AmbiguityEntry
	{
		private String keyWord = null;
		private String description = null;

		public AmbiguityEntry(String keyWord, String description)
		{
			this.keyWord = keyWord;
			this.description = description;
		}

		public String getKeyWord()
		{
			return keyWord;
		}

		public String getDescription()
		{
			return description;
		}

		@Override
		public String toString()
		{
			return "?" + keyWord/* + "=[" + description + "]"*/;
		}

	}

	public SearchKeyWords(String keyWord, List<AmbiguityEntry> ambiguousEntries)
	{
		this.keyWord = keyWord;
		this.ambiguousEntries = ambiguousEntries;
	}

	public String getKeyWord()
	{
		return keyWord;
	}

	public List<AmbiguityEntry> getAmbiguousEntries()
	{
		return ambiguousEntries;
	}

	public boolean isCertainly()
	{
		return (ambiguousEntries == null || ambiguousEntries.isEmpty());
	}

	public String getCertainPageUrl()
	{
		if (isCertainly())
		{
			return WIKI_ADDRESS_PREFIX + keyWord;
		}

		return null;
	}

	@Override
	public String toString()
	{
		return getCertainPageUrl();
	}
}
