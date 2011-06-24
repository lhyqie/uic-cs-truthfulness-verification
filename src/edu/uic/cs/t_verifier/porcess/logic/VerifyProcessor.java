package edu.uic.cs.t_verifier.porcess.logic;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import edu.uic.cs.t_verifier.data.AlternativeUnit;
import edu.uic.cs.t_verifier.data.Statement;
import edu.uic.cs.t_verifier.misc.ClassFactory;
import edu.uic.cs.t_verifier.misc.Config;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;
import edu.uic.cs.t_verifier.porcess.html.PageContentExtractor;

public class VerifyProcessor extends AbstractWordOperations
{
	//	private static final int POWER_BASE_AU = 2;
	private static final int POWER_BASE_TU = 10;
	private SearchKeyWordsMatcher keyWordsMatcher = new SearchKeyWordsMatcher();

	private PageContentExtractor pageContentExtractor = ClassFactory
			.getInstance(Config.PageContentExtractor_CLASS_NAME);

	//	public void processVerification()
	//	{
	//		List<Statement> allStatements = AlternativeUnitsReader
	//				.parseAllStatementsFromInputFiles();
	//
	//		for (Statement statement : allStatements)
	//		{
	//			processVerificationForEachStatement(statement);
	//		}
	//	}

	/*public String processVerificationForEachStatement(Statement statement)
	{
		Entry<TreeSet<String>, List<List<String>>> topicUnitsWithMostMatchedPageContent = getMatchedPagesForEachStatement(statement);

		System.out.println(topicUnitsWithMostMatchedPageContent.getKey());

		String theHighestRatedAU = evaluateAlternativeUnits(
				topicUnitsWithMostMatchedPageContent.getKey(),
				topicUnitsWithMostMatchedPageContent.getValue(),
				statement.getAlternativeUnits());

		System.out.println(">>>>>>>> " + theHighestRatedAU);
		System.out.println("=============================================");

		return theHighestRatedAU;
	}*/

	public String processVerificationForEachStatement(Statement statement)
	{
		// http://en.wikipedia.org/wiki/Richard_Nixon
		// http://en.wikipedia.org/wiki/China
		List<Entry<TreeSet<String>, List<List<String>>>> topicUnitsWithMatchedPageContentList = getMatchedPagesForEachStatement(statement);

		int maxScore = 0;
		String theHighestScoredAu = null;
		for (AlternativeUnit au : statement.getAlternativeUnits())
		{
			int score = evaluateEachAlternativeUnit(au,
					topicUnitsWithMatchedPageContentList, statement);

			System.out.println("$ " + au + "=" + score);

			if (score > maxScore)
			{
				maxScore = score;
				theHighestScoredAu = au.getString();
			}
		}

		System.out.println(">>>>>>>> " + theHighestScoredAu);
		System.out.println("=============================================");

		return theHighestScoredAu;

	}

	private int evaluateEachAlternativeUnit(
			AlternativeUnit au,
			List<Entry<TreeSet<String>, List<List<String>>>> topicUnitsWithMatchedPageContentList,
			Statement statement)
	{
		int totalScore = 0;

		// match AU in topic-unit pages
		for (Entry<TreeSet<String>, List<List<String>>> topicUnitsWithMatchedPageContent : topicUnitsWithMatchedPageContentList)
		{
			int score = evaluateEachAlternativeUnitInOnePage(au,
					topicUnitsWithMatchedPageContent.getKey(),
					topicUnitsWithMatchedPageContent.getValue());
			totalScore += score;
		}

		// topic-units in AU page
		//		int scoreInAuPage = evaluateTopicUnitsInAuPage(au,
		//				statement.getAllWordsInTopicUnits());
		//		totalScore += scoreInAuPage;

		return totalScore;
	}

	/*private int evaluateTopicUnitsInAuPage(String au, TreeSet<String> topicUnits)
	{

		String pageUrlForAu = keyWordsMatcher.matchAlternativeUnit(au);
		if (pageUrlForAu == null)
		{
			return 0;
		}

		topicUnits = stemAndRemoveAllNonStopWords(topicUnits);
		List<List<String>> powersetOfTopicUnits = powerset(topicUnits, false);

		List<List<String>> stemmedNonStopWordsInAllParagraphs = pageContentExtractor
				.extractStemmedNonStopWordsInAllParagraphs(pageUrlForAu);
		List<Map<String, List<Integer>>> postingListOfParagraphs = constructPostingList(stemmedNonStopWordsInAllParagraphs);

		int score = scorePage(powersetOfTopicUnits, postingListOfParagraphs,
				new String[] {}, POWER_BASE_AU);

		return score;
	}*/

	private int evaluateEachAlternativeUnitInOnePage(AlternativeUnit au,
			TreeSet<String> topicUnits, List<List<String>> stemmedParagraphs)
	{
		// List<List<String>> powersetOfTopicUnits = powerset(topicUnits, false);
		List<Map<String, List<Integer>>> postingListOfParagraphs = constructPostingList(stemmedParagraphs);

		int score = countScoreForEachAlternativeUnit(au, topicUnits,
				postingListOfParagraphs);

		return score;
	}

	/*public String processVerificationForEachStatement(Statement statement)
	{
		// http://en.wikipedia.org/wiki/Richard_Nixon
		// http://en.wikipedia.org/wiki/China
		List<Entry<TreeSet<String>, List<List<String>>>> topicUnitsWithMatchedPageContentList = getMatchedPagesForEachStatement(statement);

		int max = -1;
		String theHighestRatedAU = null;
		for (Entry<TreeSet<String>, List<List<String>>> topicUnitsWithMatchedPageContent : topicUnitsWithMatchedPageContentList)
		{
			Entry<Integer, String> auByScore = evaluateAlternativeUnits(
					topicUnitsWithMatchedPageContent.getKey(),
					topicUnitsWithMatchedPageContent.getValue(),
					statement.getAlternativeUnits());
			if (auByScore.getKey().intValue() > max)
			{
				max = auByScore.getKey().intValue();
				theHighestRatedAU = auByScore.getValue();
			}
		}

		System.out.println(">>>>>>>> " + theHighestRatedAU);
		System.out.println("=============================================");

		return theHighestRatedAU;
	}

	private Entry<Integer, String> evaluateAlternativeUnits(
			TreeSet<String> topicUnits, List<List<String>> stemmedParagraphs,
			List<String> alternativeUnits)
	{
		List<List<String>> powersetOfTopicUnits = powerset(topicUnits, false);
		List<Map<String, List<Integer>>> postingListOfParagraphs = constructPostingList(stemmedParagraphs);

		// Map<String, Integer> alternativeUnitsByRate = new HashMap<String, Integer>();
		int maxScore = -1;
		String theHighestRatedAU = null;
		for (String au : alternativeUnits)
		{
			int score = countScoreForEachAlternativeUnit(au,
					powersetOfTopicUnits, postingListOfParagraphs);

			if (score > maxScore)
			{
				maxScore = score;
				theHighestRatedAU = au;
			}

		}

		return new SimpleEntry<Integer, String>(maxScore, theHighestRatedAU);
	}*/

	private int countScoreForEachAlternativeUnit(AlternativeUnit au,
			TreeSet<String> topicUnits,
			List<Map<String, List<Integer>>> postingListOfParagraphs)
	{
		// AU may contains more than one word
		String[] stemmedWordsInAu = au.getWords();
		{
			for (int index = 0; index < stemmedWordsInAu.length; index++)
			{
				stemmedWordsInAu[index] = stem(stemmedWordsInAu[index]);
			}
		}

		int finalScore = scorePage(topicUnits, postingListOfParagraphs,
				stemmedWordsInAu, au.getWeight(), POWER_BASE_TU);

		return finalScore;
	}

	private int scorePage(TreeSet<String> topicUnits,
			List<Map<String, List<Integer>>> postingListOfParagraphs,
			String[] stemmedWordsInAu, int auWeight, int powerBase)
	{
		// trying begin with the longest ones
		//		Collections.reverse(powersetOfTopicUnits);
		//		if (powersetOfTopicUnits.isEmpty())
		//		{
		//			List<String> emptyList = Collections.emptyList();
		//			powersetOfTopicUnits.add(emptyList);
		//		}

		int finalScore = 0;
		for (Map<String, List<Integer>> postingListOfOneParagraph : postingListOfParagraphs)
		{
			/*Integer score = null;
			for (List<String> setOfTopicUnits : powersetOfTopicUnits)
			{
				score = matchWordsInOneParagraph(setOfTopicUnits,
						stemmedWordsInAu, postingListOfOneParagraph, powerBase);
				// if matched one, break, since the longest matched the better
				if (score != null)
				{
					break;
				}
			}*/
			int score = matchWordsInOneParagraph(topicUnits, stemmedWordsInAu,
					postingListOfOneParagraph, auWeight, powerBase);
			finalScore += score;
		}
		return finalScore;
	}

	private int matchWordsInOneParagraph(TreeSet<String> topicUnits,
			String[] stemmedWordsInAu,
			Map<String, List<Integer>> postingListOfOneParagraph, int auWeight,
			int powerBase)
	{
		// AU must exist!
		for (String wordInAu : stemmedWordsInAu)
		{
			// all the words in AU must exist in a paragraph at a same time
			if (!postingListOfOneParagraph.containsKey(wordInAu))
			{
				return 0;
			}
		}

		int frequancySumOfTus = 0;
		// no matter how many words in AU, only count once
		int numberOfTuExist = 0;

		for (String tu : topicUnits)
		{
			List<Integer> postingOfTu = postingListOfOneParagraph.get(tu);
			if (postingOfTu != null)
			{
				// there exists such TU in the paragraph
				numberOfTuExist++;
				frequancySumOfTus += postingOfTu.size();
			}
		}

		// the more weighted AU, the more accurate it is. 
		return (int) (auWeight * frequancySumOfTus * Math.pow(powerBase,
				numberOfTuExist));
	}

	/*private Integer matchWordsInOneParagraph(TreeSet<String> topicUnits,
			String[] stemmedWordsInAu,
			Map<String, List<Integer>> postingListOfOneParagraph, int powerBase)
	{
		Map<String, Integer> wordsCurrentPosition = new HashMap<String, Integer>();
		for (String word : setOfTopicUnits)
		{
			wordsCurrentPosition.put(word, null);
		}
		for (String word : stemmedWordsInAu)
		{
			wordsCurrentPosition.put(word, null);
		}

		////////////////////////////////////////////////////////////////////////
		int count = 0;
		// check if all words are existing in the paragraph
		for (String word : wordsCurrentPosition.keySet())
		{
			List<Integer> posting = postingListOfOneParagraph.get(word);
			if (posting == null)
			{
				return null;
			}

			// use setOfTopicUnits.size() as weight
			// use posting.size() as frequency of a word in the paragraph
			count += (posting.size() * Math.pow(powerBase,
					setOfTopicUnits.size()));
		}

		////////////////////////////////////////////////////////////////////////
		// HERE, all the words are in the paragraph
		// TODO right now, if words in a set are all in one paragraph,
		// I consider them to be close, and count them all

		return count;

		int initialValue = -1;
		for (String word : wordsCurrentPosition.keySet())
		{
			wordsCurrentPosition.put(word, initialValue);
		}

		int rightPosition = -1;
		for (String word : wordsCurrentPosition.keySet())
		{
			Integer currentPosition = wordsCurrentPosition.get(word);
			List<Integer> posting = postingListOfOneParagraph.get(word);
			for (Integer position : posting)
			{
				if (position > currentPosition)
				{
					if (position >= rightPosition)
					{
						rightPosition = position;
						//						wordsCurrentPosition.put(word, position);
					}
					break;
				}
			}
		}

		int leftPosition = Integer.MAX_VALUE;
		for (String word : wordsCurrentPosition.keySet())
		{
			List<Integer> posting = postingListOfOneParagraph.get(word);
			for (int index = posting.size() - 1; index <= 0; index--)
			{
				int position = posting.get(index);
				if (position <= rightPosition)
				{
					if (position < leftPosition)
					{
						leftPosition = position;
						//						wordsCurrentPosition.put(word, position);
					}
					break;
				}
			}
		}

	}*/

	@SuppressWarnings("unchecked")
	private List<Entry<TreeSet<String>, List<List<String>>>> getMatchedPagesForEachStatement(
			Statement statement)
	{
		Map<String, String> matchedUrlsBySubTopicUnit = keyWordsMatcher
				.matchTopicUnitsForStatement(statement);
		TreeSet<String> allWordsInTopicUnits = statement
				.getAllWordsInTopicUnits();

		List<Entry<TreeSet<String>, List<List<String>>>> result = new ArrayList<Map.Entry<TreeSet<String>, List<List<String>>>>(
				matchedUrlsBySubTopicUnit.size());

		for (Entry<String, String> entry : matchedUrlsBySubTopicUnit.entrySet())
		{
			String url = entry.getValue();

			TreeSet<String> notMatchedWordsInTopicUnits = (TreeSet<String>) allWordsInTopicUnits
					.clone();
			for (String matchedTopicUnit : StringUtils.split(entry.getKey()))
			{
				notMatchedWordsInTopicUnits.remove(matchedTopicUnit);
			}

			System.out.println("> " + url + notMatchedWordsInTopicUnits);

			notMatchedWordsInTopicUnits = stemAndRemoveAllNonStopWords(notMatchedWordsInTopicUnits);

			List<List<String>> stemmedNonStopWordsInAllParagraphs = pageContentExtractor
					.extractStemmedNonStopWords(url);

			result.add(new SimpleEntry<TreeSet<String>, List<List<String>>>(
					notMatchedWordsInTopicUnits,
					stemmedNonStopWordsInAllParagraphs));
		}

		return result;

	}

	/*@SuppressWarnings("unchecked")
	private Entry<TreeSet<String>, List<List<String>>> getTheMostMatchedPageForEachStatement(
			Statement statement)
	{
		Map<String, String> matchedUrlsBySubTopicUnit = keyWordsMatcher
				.matchStatement(statement);
		TreeSet<String> allWordsInTopicUnits = statement
				.getAllWordsInTopicUnits();

		TreeSet<String> notMatchedWordsInTopicUnitsForMaxRatedPage = null;
		List<List<String>> stemmedNonStopWordsInAllParagraphs = null;

		// if only one URL matched already, there's no need further matching
		if (matchedUrlsBySubTopicUnit.size() == 1)
		{
			//			System.out.println(statement.getTopicUnits());
			//			System.out.println(matchedUrlsBySubTopicUnit);

			String maxRatedMatchedUrl = matchedUrlsBySubTopicUnit.values()
					.toArray()[0].toString();

			notMatchedWordsInTopicUnitsForMaxRatedPage = (TreeSet<String>) allWordsInTopicUnits
					.clone();
			for (String matchedTopicUnit : StringUtils
					.split((String) matchedUrlsBySubTopicUnit.keySet()
							.toArray()[0]))
			{
				notMatchedWordsInTopicUnitsForMaxRatedPage
						.remove(matchedTopicUnit);
			}
			stemmedNonStopWordsInAllParagraphs = pageContentExtractor
					.extractStemmedNonStopWordsInAllParagraphs(maxRatedMatchedUrl);

			return new SimpleEntry<TreeSet<String>, List<List<String>>>(
					notMatchedWordsInTopicUnitsForMaxRatedPage,
					stemmedNonStopWordsInAllParagraphs);
		}
		else
		{
			///////////////////////////////////////////////////////////////////////
			int maxCount = 0;
			String mostMatchedUrl = null;
			for (Entry<String, String> entry : matchedUrlsBySubTopicUnit
					.entrySet())
			{
				TreeSet<String> notMatchedWordsInTopicUnits = (TreeSet<String>) allWordsInTopicUnits
						.clone();
				for (String matchedTopicUnit : StringUtils
						.split(entry.getKey()))
				{
					notMatchedWordsInTopicUnits.remove(matchedTopicUnit);
				}

				String matchedUrl = entry.getValue();

				Entry<Integer, List<List<String>>> countWithPageContent = processEachMatchedUrl(
						matchedUrl, notMatchedWordsInTopicUnits);
				int count = countWithPageContent.getKey();
				if (count >= maxCount)
				{
					maxCount = count;
					mostMatchedUrl = matchedUrl;

					notMatchedWordsInTopicUnitsForMaxRatedPage = notMatchedWordsInTopicUnits;
					stemmedNonStopWordsInAllParagraphs = countWithPageContent
							.getValue();
				}

			}

			System.out.println(maxCount);
			System.out.println(mostMatchedUrl);
		}

		System.out.println(statement.getTopicUnits());
		System.out.println(matchedUrlsBySubTopicUnit);

		notMatchedWordsInTopicUnitsForMaxRatedPage = stemAndRemoveAllNonStopWords(notMatchedWordsInTopicUnitsForMaxRatedPage);

		return new SimpleEntry<TreeSet<String>, List<List<String>>>(
				notMatchedWordsInTopicUnitsForMaxRatedPage,
				stemmedNonStopWordsInAllParagraphs);
	}*/

	private TreeSet<String> stemAndRemoveAllNonStopWords(TreeSet<String> words)
	{
		TreeSet<String> result = new TreeSet<String>();
		for (String word : words)
		{
			if (isNoneStopWord(word))
			{
				result.add(stem(word));
			}

		}

		return result;
	}

	/*private Entry<Integer, List<List<String>>> processEachMatchedUrl(
			String matchedUrl, TreeSet<String> notMatchedWordsInTopicUnits)
	{
		TreeSet<String> notMatchedWordsInTopicUnits_stemmed = new TreeSet<String>();
		for (String word : notMatchedWordsInTopicUnits)
		{
			if (isNoneStopWord(word))
			{
				notMatchedWordsInTopicUnits_stemmed.add(stem(word));
			}
		}

		////////////////////////////////////////////////////////////////////////
		int matchedCount = 1;
		HashSet<String> wordsCounted = new HashSet<String>();

		List<List<String>> stemmedNonStopWordsInAllParagraphs = pageContentExtractor
				.extractStemmedNonStopWordsInAllParagraphs(matchedUrl);
		for (List<String> wordsInEachParagraph : stemmedNonStopWordsInAllParagraphs)
		{
			for (String word : wordsInEachParagraph)
			{
				if (notMatchedWordsInTopicUnits_stemmed.contains(word))
				{
					// consider how many different words matched
					if (wordsCounted.contains(word))
					{
						matchedCount++;
					}
					else
					{
						wordsCounted.add(word);
						matchedCount = matchedCount * 10;
					}
				}
			}
		}

		return new SimpleEntry<Integer, List<List<String>>>(matchedCount,
				stemmedNonStopWordsInAllParagraphs);
	}*/
}
