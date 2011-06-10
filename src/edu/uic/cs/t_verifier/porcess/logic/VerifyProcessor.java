package edu.uic.cs.t_verifier.porcess.logic;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import edu.uic.cs.t_verifier.data.Statement;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;
import edu.uic.cs.t_verifier.porcess.html.PageContentExtractor;

public class VerifyProcessor extends AbstractWordOperations
{
	private static final int POWER_BASE_AU = 2;
	private static final int POWER_BASE_TU = 10;
	private SearchKeyWordsMatcher keyWordsMatcher = new SearchKeyWordsMatcher();
	private PageContentExtractor pageContentExtractor = new PageContentExtractor();

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

		int maxScore = -1;
		String theHighestScoredAu = null;
		for (String au : statement.getAlternativeUnits())
		{
			int score = evaluateEachAlternativeUnit(au,
					topicUnitsWithMatchedPageContentList, statement);

			if (score > maxScore)
			{
				maxScore = score;
				theHighestScoredAu = au;
			}
		}

		System.out.println(">>>>>>>> " + theHighestScoredAu);
		System.out.println("=============================================");

		return theHighestScoredAu;

	}

	private int evaluateEachAlternativeUnit(
			String au,
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
		int scoreInAuPage = evaluateTopicUnitsInAuPage(au,
				statement.getAllWordsInTopicUnits());
		totalScore += scoreInAuPage;

		return totalScore;
	}

	private int evaluateTopicUnitsInAuPage(String au, TreeSet<String> topicUnits)
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
	}

	private int evaluateEachAlternativeUnitInOnePage(String au,
			TreeSet<String> topicUnits, List<List<String>> stemmedParagraphs)
	{
		List<List<String>> powersetOfTopicUnits = powerset(topicUnits, false);
		List<Map<String, List<Integer>>> postingListOfParagraphs = constructPostingList(stemmedParagraphs);

		int score = countScoreForEachAlternativeUnit(au, powersetOfTopicUnits,
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

	private int countScoreForEachAlternativeUnit(String au,
			List<List<String>> powersetOfTopicUnits,
			List<Map<String, List<Integer>>> postingListOfParagraphs)
	{
		// AU may contains more than one word
		String[] stemmedWordsInAu = StringUtils.split(au);
		{
			for (int index = 0; index < stemmedWordsInAu.length; index++)
			{
				stemmedWordsInAu[index] = stem(stemmedWordsInAu[index]);
			}
		}

		int finalScore = scorePage(powersetOfTopicUnits,
				postingListOfParagraphs, stemmedWordsInAu, POWER_BASE_TU);

		return finalScore;
	}

	private int scorePage(List<List<String>> powersetOfTopicUnits,
			List<Map<String, List<Integer>>> postingListOfParagraphs,
			String[] stemmedWordsInAu, int powerBase)
	{
		// trying begin with the longest ones
		Collections.reverse(powersetOfTopicUnits);
		if (powersetOfTopicUnits.isEmpty())
		{
			List<String> emptyList = Collections.emptyList();
			powersetOfTopicUnits.add(emptyList);
		}

		int finalScore = 0;
		for (Map<String, List<Integer>> postingListOfOneParagraph : postingListOfParagraphs)
		{
			//			if (stemmedAu.equals("852")
			//					&& postingListOfOneParagraph
			//							.toString()
			//							.equals("{disarma=[182], osaka=[19], servic=[23], jean-bertrand=[148], airport=[18, 58], open=[21], 150 km=[82], 1977=[42], assault=[93], year=[102], border=[193], pine=[66], heather=[121], estonia=[157], ban=[95, 96], elect=[146], invas=[141], internat=[17, 57], transfer=[24], respons=[195], mexican=[171], jose=[167], wollemi=[65, 74], raul=[175], canyon=[79], board=[53], wollemia=[63], 28=[152, 165], threaten=[185], largest=[84], 3=[1], peopl=[6, 52, 163], soldier=[116], 737=[50], gorg=[73], 5=[28], remot=[71], 4=[14], david=[80], 8=[44], impair=[124], citi=[85], nobl=[81], home=[38], legitim=[145], america=[128, 132], 19=[135], bloodless=[140], john=[35], 17=[119], contest=[125], 16=[104], 13=[87], itami=[25], cabramatta=[34], de=[177], 1995=[133], –=[2, 15, 29, 45, 62, 88, 105, 120, 136, 153, 166, 180], unscom=[188], presid=[89], american=[137], gortari=[178], republ=[7], rape=[112], salina=[176], louis=[109], crisi=[183], mp=[33], previous=[67], park=[76], british=[115], ms=[156], 427=[48], flight=[47], 10=[61, 101], jensen=[110], russia=[5], kansai=[16, 26], september–octob=[179], weapon=[12, 94, 98], intern=[22], deploi=[191, 197], south=[30, 77], tour=[107], aristid=[149], survivor=[59], kill=[161], shot=[37], baltic=[159], boe=[49], hear=[123], cold=[3], three=[114], power=[150], ruiz=[169], massieu=[170], crash=[54], de-target=[10], china=[8], live=[70], whiteston=[122, 130], murder=[113], manufactur=[97], sink=[158], period=[100], rainforest=[72], nation=[75], cooper=[187], usair=[46], sign=[92], entitl=[129], discov=[69], newman=[36], cypru=[117], pittsburgh=[56], australia=[39, 83], wale=[31, 78], restor=[144], car=[154], stop=[186], 132=[51], inspector=[189], bill=[90], state=[32], clinton=[91], stage=[139], war=[4], troop=[138, 192, 198], politician=[172], japan=[20], order=[143, 174], leader=[147], haiti=[142], 852=[162], ferri=[155], septemb=[0, 13, 27, 43, 60, 86, 103, 118, 134, 151, 164], francisco=[168], sea=[160], win=[126], polit=[40], iraq=[181, 184], begin=[190, 196], abduct=[111], guid=[108], nobili=[64], nuclear=[11], agre=[9], assassin=[41, 173], fossil=[68], featur=[99], danish=[106], approach=[55], kuwait=[194, 199], miss=[127, 131]}"))
			//			{
			//				System.out.println(postingListOfOneParagraph);
			//			}

			Integer score = null;
			for (List<String> setOfTopicUnits : powersetOfTopicUnits)
			{
				score = matchWordsInOneParagraph(setOfTopicUnits,
						stemmedWordsInAu, postingListOfOneParagraph, powerBase);
				// if matched one, break, since the longest matched the better
				if (score != null)
				{
					break;
				}
			}

			finalScore += ((score != null) ? score.intValue() : 0);
		}
		return finalScore;
	}

	private Integer matchWordsInOneParagraph(List<String> setOfTopicUnits,
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

		/*int initialValue = -1;
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
		}*/

	}

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

			System.out.println(url);

			TreeSet<String> notMatchedWordsInTopicUnits = (TreeSet<String>) allWordsInTopicUnits
					.clone();
			for (String matchedTopicUnit : StringUtils.split(entry.getKey()))
			{
				notMatchedWordsInTopicUnits.remove(matchedTopicUnit);
			}
			notMatchedWordsInTopicUnits = stemAndRemoveAllNonStopWords(notMatchedWordsInTopicUnits);

			List<List<String>> stemmedNonStopWordsInAllParagraphs = pageContentExtractor
					.extractStemmedNonStopWordsInAllParagraphs(url);

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
