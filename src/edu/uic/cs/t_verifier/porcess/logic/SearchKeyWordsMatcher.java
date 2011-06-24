package edu.uic.cs.t_verifier.porcess.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.uic.cs.t_verifier.data.AlternativeUnit;
import edu.uic.cs.t_verifier.data.SearchKeyWords;
import edu.uic.cs.t_verifier.data.SearchKeyWords.AmbiguityEntry;
import edu.uic.cs.t_verifier.data.Statement;
import edu.uic.cs.t_verifier.misc.LogHelper;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;
import edu.uic.cs.t_verifier.porcess.html.SearchKeyWordsExtractor;
import edu.uic.cs.t_verifier.process.words.StopWords;

public class SearchKeyWordsMatcher extends AbstractWordOperations
{
	private static final Logger LOGGER = LogHelper
			.getLogger(SearchKeyWordsMatcher.class);

	private SearchKeyWordsExtractor searchKeyWordsExtractor = new SearchKeyWordsExtractor();

	private static class NoSubStringTreeSet extends TreeSet<String>
	{
		private static final long serialVersionUID = 1L;
		private static final WordsNumberComparator WORDS_NUMBER_COMPARATOR = new WordsNumberComparator();

		public NoSubStringTreeSet()
		{
			super(WORDS_NUMBER_COMPARATOR);
		}

		@Override
		public boolean add(String str)
		{
			Iterator<String> iterator = iterator();
			while (iterator.hasNext())
			{
				String containingStr = iterator.next();
				if (containingStr.contains(str))
				{
					return false;
				}
				else if (str.contains(containingStr))
				{
					iterator.remove();
				}
			}

			return super.add(str);
		}
	}

	/*
	 * public static void main(String[] args) { NoSubStringTreeSet set = new
	 * NoSubStringTreeSet(); set.add(" b c d"); set.add("a b c d");
	 * set.add("a b c d");
	 * 
	 * set.add(" c d"); set.add(" c d e");
	 * 
	 * TreeSet<String> other = new TreeSet<String>(); other.add(" b c d");
	 * other.add(" c d"); other.add(" c d e");
	 * 
	 * set.addAll(other);
	 * 
	 * System.out.println(set); }
	 */

	// public String matchAlternativeUnit(String au)
	// {
	// au = au.trim();
	// SearchKeyWords matchedKeyWords = searchKeyWordsExtractor
	// .getStandardSearchKeyWords(au);
	// if (matchedKeyWords != null && matchedKeyWords.isCertainly())
	// {
	// return matchedKeyWords.getCertainPageUrl();
	// }
	//
	// // remove stop-words and try again
	// au = StopWords.trimStopWordsInBothSides(au);
	// matchedKeyWords = searchKeyWordsExtractor.getStandardSearchKeyWords(au);
	// if (matchedKeyWords != null && matchedKeyWords.isCertainly())
	// {
	// return matchedKeyWords.getCertainPageUrl();
	// }
	//
	// return null;
	// }
	public Map<String, String> matchTopicUnitsForStatement(Statement statement)
	{
		Collection<String> topicUnits = statement.getTopicUnits();
		List<AlternativeUnit> aus = statement.getAlternativeUnits();

		return processAllTopicUnitsInStatement(topicUnits, aus);
	}

	Map<String, String> processAllTopicUnitsInStatement(
			Collection<String> topicUnits, List<AlternativeUnit> aus)
	{
		// topic units waiting for mathcing
		NoSubStringTreeSet topicUnitsRemovedStopWords = new NoSubStringTreeSet();
		for (String topicUnit : topicUnits)
		{
			topicUnit = removeSaxonGenitive(topicUnit);
			topicUnitsRemovedStopWords.add(StopWords
					.trimStopWordsInBothSides(topicUnit));
		}

		// //////////////////////////////////////////////////////////////////////
		Map<String, String> result = new HashMap<String, String>();

		HashSet<String> allMatchedTopicUnits = new HashSet<String>();
		while (topicUnitsRemovedStopWords.size() != 0)
		{
			HashSet<String> notMatchedTopicUnits = new HashSet<String>();

			for (String topicUnit : topicUnitsRemovedStopWords)
			{
				if (topicUnit.length() == 0)
				{
					continue;
				}

				SearchKeyWords matchedKeyWords = searchKeyWordsExtractor
						.getStandardSearchKeyWords(topicUnit);
				// matched
				if (matchedKeyWords != null)
				{
					// certainly matched
					if (matchedKeyWords.isCertainly())
					{
						allMatchedTopicUnits.add(topicUnit);

						String matchedUrl = matchedTopicUnit(topicUnit,
								matchedKeyWords);
						result.put(topicUnit, matchedUrl);
					}
					// ambiguous
					else
					{
						List<AmbiguityEntry> ambiguousEntries = matchedKeyWords
								.getAmbiguousEntries();
						Set<String> otherTopicUnits = new HashSet<String>(
								topicUnitsRemovedStopWords);
						otherTopicUnits.remove(topicUnit);
						List<String> urls = findTheMostMatchedAmbiguousEntry(
								ambiguousEntries, otherTopicUnits, aus);

						for (String url : urls)
						{
							System.out.println("? " + url);
							result.put(topicUnit, url);
						}
					}
				}
				else
				{
					// not matched
					notMatchedTopicUnits.add(topicUnit);
				}
			}

			if (notMatchedTopicUnits.isEmpty())
			{
				break;
			}

			topicUnitsRemovedStopWords = getNextLevelTopicUnits(
					allMatchedTopicUnits, notMatchedTopicUnits);
		}

		return result;
	}

	private List<String> findTheMostMatchedAmbiguousEntry(
			List<AmbiguityEntry> ambiguousEntries,
			Collection<String> otherTopicUnits, List<AlternativeUnit> aus)
	{
		int maxScore = 0;
		List<String> maxScoreUrls = new ArrayList<String>();
		for (AmbiguityEntry ambiguityEntry : ambiguousEntries)
		{
			int count = 0;
			String description = ambiguityEntry.getDescription();
			List<String> nonstopStemmedWordsInDesc = splitIntoNoneStopStemmedWords(description);
			if (nonstopStemmedWordsInDesc.isEmpty())
			{
				continue;
			}

			if (otherTopicUnits.isEmpty())
			{
				// if there's no TUs, use AUs do the matching
				otherTopicUnits = new ArrayList<String>();
				for (AlternativeUnit au : aus)
				{
					otherTopicUnits.add(au.toString());
				}
			}

			for (String topicUnit : otherTopicUnits)
			{
				if (isNoneStopWord(topicUnit)
						&& nonstopStemmedWordsInDesc.contains(stem(topicUnit)))
				{
					count++;
				}
			}

			if (count > maxScore)
			{
				maxScore = count;
				maxScoreUrls.clear();
				maxScoreUrls.add(SearchKeyWords.WIKI_ADDRESS_PREFIX
						+ ambiguityEntry.getKeyWord());
			}
			else if (count != 0 && count == maxScore)
			{
				maxScoreUrls.add(SearchKeyWords.WIKI_ADDRESS_PREFIX
						+ ambiguityEntry.getKeyWord());
			}
		}

		return maxScoreUrls;
	}

	private String matchedTopicUnit(String topicUnit, SearchKeyWords keyWords)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(LogHelper.LOG_LAYER_ONE + "Matched a key word["
					+ topicUnit + "] in the URL["
					+ keyWords.getCertainPageUrl() + "]");
		}

		// TODO
		// System.out.println(keyWords.getCertainPageUrl());

		return keyWords.getCertainPageUrl();
	}

	private NoSubStringTreeSet getNextLevelTopicUnits(
			HashSet<String> allMatchedTopicUnits,
			HashSet<String> notMatchedTopicUnits)
	{
		NoSubStringTreeSet result = new NoSubStringTreeSet();

		for (String notMatched : notMatchedTopicUnits)
		{
			List<String> nextLevelTopicUnits = shrinkPharse(notMatched);
			middle: for (String nextLevelNotMatched : nextLevelTopicUnits)
			{
				for (String matched : allMatchedTopicUnits)
				{
					// if any matched already contains this TU
					if (matched.contains(nextLevelNotMatched))
					{
						// ignore this substring
						continue middle;
					}
				}

				// no already match TU contains this sub-TU
				result.add(nextLevelNotMatched);
			}
		}

		return result;
	}

	private List<String> shrinkPharse(String topicUnit)
	{
		// topicUnit = topicUnit.trim();
		int first = topicUnit.indexOf(' ');
		int last = topicUnit.lastIndexOf(' ');

		if (first < 0 || last < 0)
		{
			return Collections.emptyList();
		}

		List<String> result = new ArrayList<String>(2);
		result.add(StopWords.trimStopWordsInBothSides(topicUnit.substring(0,
				last)));
		result.add(StopWords.trimStopWordsInBothSides(topicUnit
				.substring(first + 1)));

		return result;
	}

}
