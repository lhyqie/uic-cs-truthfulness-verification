package edu.uic.cs.t_verifier.porcess.common;

import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import edu.uic.cs.t_verifier.process.words.PorterStemmer;
import edu.uic.cs.t_verifier.process.words.StopWords;

public abstract class AbstractWordOperations
{
	public static final String WORDS_DELIM = " \t\n\r\f.,;:'\"()?!•·†";

	private PorterStemmer stemmer = new PorterStemmer();

	protected String removeSaxonGenitive(String phrase)
	{
		return phrase.replace("'s ", " ").replace("s' ", "s ");
	}

	protected List<String> splitIntoNoneStopStemmedWords(String paragraph)
	{
		// remove all commas in the paragraph
		paragraph = paragraph.replace(",", "");

		List<String> wordsInLine = new ArrayList<String>();

		StringTokenizer tokenizer = new StringTokenizer(paragraph, WORDS_DELIM);
		while (tokenizer.hasMoreElements())
		{
			String word = (String) tokenizer.nextElement();
			word = removeReferenceSymbol(word);
			word = trimNoneCharactersInBothEnds(word);

			if (isNoneStopWord(word))
			{
				word = stem(word);
				wordsInLine.add(word);
			}
		}

		return wordsInLine;
	}

	//	public static void main(String[] args)
	//	{
	//		String paragraph = "hello 134,123,456, sdfs";
	//		paragraph = paragraph.replace(",", "");
	//		System.out.println(paragraph);
	//	}

	//	private static String removeCommasInNumber(String paragraph)
	//	{
	//		Pattern pattern = Pattern.compile("\\s\\d{1,3},\\d{3}");
	//		Matcher matcher = pattern.matcher(paragraph);
	//		while (matcher.find())
	//		{
	//			System.out.println(matcher.group());
	//			matcher.replaceFirst(replacement);
	//		}
	//		//		matcher.
	//		return null;
	//	}

	private String trimNoneCharactersInBothEnds(String word)
	{
		int charBegin = 0;
		int charEnd = word.length();
		char[] val = word.toCharArray();

		while ((charBegin < charEnd)
				&& (!Character.isLetterOrDigit(val[charBegin])))
		{
			charBegin++;
		}

		while ((charBegin < charEnd)
				&& (!Character.isLetterOrDigit(val[charEnd - 1])))
		{
			charEnd--;
		}

		return ((charBegin > 0) || (charEnd < word.length())) ? word.substring(
				charBegin, charEnd) : word;
	}

	protected String stem(String word)
	{
		return stemmer.stem(word);
	}

	protected boolean isNoneStopWord(String word)
	{
		return !StopWords.isStopWord(word) && word.trim().length() != 0;
	}

	private String removeReferenceSymbol(String word)
	{
		word = word.trim();

		int begin = word.indexOf('[');
		int end = word.lastIndexOf(']');
		if (begin >= 0 && end == word.length() - 1)
		{
			return word.substring(0, begin).trim();
		}

		return word;
	}

	/*private static class PowersetComparator implements Comparator<List<String>>
	{
		@Override
		public int compare(List<String> one, List<String> another)
		{
			if (one.size() != another.size())
			{
				return one.size() - another.size();
			}

			for (int index = 0; index < one.size(); index++)
			{
				if (!one.get(index).equals(another.get(index)))
				{
					return one.get(index).compareTo(another.get(index));
				}
			}

			return 0;
		}
	}

	protected List<List<String>> powerset(Collection<String> list,
			boolean needEmptySet)
	{
		List<List<String>> ps = new ArrayList<List<String>>();
		ps.add(new ArrayList<String>()); // add the empty set

		// for every item in the original list
		for (String item : list)
		{
			List<List<String>> newPs = new ArrayList<List<String>>();

			for (List<String> subset : ps)
			{
				// copy all of the current powerset's subsets
				newPs.add(subset);

				// plus the subsets appended with the current item
				List<String> newSubset = new ArrayList<String>(subset);
				newSubset.add(item);
				newPs.add(newSubset);
			}

			// powerset is now powerset of list.subList(0, list.indexOf(item)+1)
			ps = newPs;
		}

		Collections.sort(ps, new PowersetComparator());

		if (!needEmptySet)
		{
			ps.remove(0);
		}

		return ps;
	}*/

	protected List<Map<String, List<Integer>>> constructPostingList(
			List<List<String>> stemmedParagraphs)
	{
		List<Map<String, List<Integer>>> result = new ArrayList<Map<String, List<Integer>>>(
				stemmedParagraphs.size());

		for (List<String> paragraph : stemmedParagraphs)
		{
			Map<String, List<Integer>> postingListOfOneParagraph = new HashMap<String, List<Integer>>();
			result.add(postingListOfOneParagraph);

			for (int index = 0; index < paragraph.size(); index++)
			{
				String word = paragraph.get(index).toLowerCase(Locale.US);
				List<Integer> positionList = postingListOfOneParagraph
						.get(word);
				if (positionList == null)
				{
					positionList = new ArrayList<Integer>();
					postingListOfOneParagraph.put(word, positionList);
				}

				positionList.add(index);
			}
		}

		return result;
	}

}
