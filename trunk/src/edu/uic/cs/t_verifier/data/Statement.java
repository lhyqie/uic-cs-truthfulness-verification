package edu.uic.cs.t_verifier.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import edu.uic.cs.t_verifier.misc.Assert;
import edu.uic.cs.t_verifier.porcess.common.AbstractWordOperations;

public class Statement
{
	private static final int DEFAULT_ALTERNATIVE_UNIT_NUMBER = 5;

	//	private String wholeStatement = null;
	private List<String> alternativeUnits = null;
	private String topicUnitLeft = "";
	private String topicUnitRight = "";

	private List<String> allAlternativeStatements = null;
	private TreeSet<String> allWordsInTopicUnits = null;

	public Statement(String topicUnitLeft, String topicUnitRight)
	{
		this.alternativeUnits = new ArrayList<String>(
				DEFAULT_ALTERNATIVE_UNIT_NUMBER);

		if (topicUnitLeft != null)
		{
			this.topicUnitLeft = topicUnitLeft.toLowerCase(Locale.US);
		}
		if (topicUnitRight != null)
		{
			this.topicUnitRight = topicUnitRight.toLowerCase(Locale.US);
		}
	}

	public void addAlternativeUnit(String alternativeUnit)
	{
		alternativeUnits.add(alternativeUnit.toLowerCase(Locale.US));
	}

	public List<String> getAllAlternativeStatements()
	{
		if (allAlternativeStatements != null)
		{
			return allAlternativeStatements;
		}

		allAlternativeStatements = new ArrayList<String>(
				alternativeUnits.size());
		for (String au : alternativeUnits)
		{
			String alternativeStatement = topicUnitLeft + " " + au + " "
					+ topicUnitRight;
			allAlternativeStatements.add(alternativeStatement.trim());
		}

		return allAlternativeStatements;
	}

	/**
	 * Since the DU may appear in the middle of a sentence, like: 
	 * 'the life expectancy of an elephant is 60 years [60]'
	 * There may be at most two TUs,
	 * 'the life expectancy of an elephant is' and 'years'
	 * 
	 * @return all TUs of the statement
	 */
	public List<String> getTopicUnits()
	{
		ArrayList<String> result = new ArrayList<String>(2);
		if (topicUnitLeft != null && topicUnitLeft.trim().length() != 0)
		{
			result.add(topicUnitLeft.trim());
		}

		if (topicUnitRight != null && topicUnitRight.trim().length() != 0)
		{
			result.add(topicUnitRight.trim());
		}

		Assert.isTrue(result.size() > 0);

		return result/*.toArray(new String[result.size()])*/;
	}

	public TreeSet<String> getAllWordsInTopicUnits()
	{
		if (allWordsInTopicUnits != null)
		{
			return allWordsInTopicUnits;
		}

		allWordsInTopicUnits = new TreeSet<String>();
		for (String topicUnit : getTopicUnits())
		{
			String[] words = StringUtils.split(topicUnit,
					AbstractWordOperations.WORDS_DELIM);
			allWordsInTopicUnits.addAll(Arrays.asList(words));
		}

		return allWordsInTopicUnits;
	}

	public List<AlternativeUnit> getAlternativeUnits()
	{
		List<AlternativeUnit> result = new ArrayList<AlternativeUnit>(
				alternativeUnits.size());
		for (String auString : alternativeUnits)
		{
			result.add(new AlternativeUnit(auString));
		}

		assignWeightToAUs(result);

		return result;
	}

	private void assignWeightToAUs(List<AlternativeUnit> aus)
	{
		Collections.sort(aus, new Comparator<AlternativeUnit>()
		{
			@Override
			public int compare(AlternativeUnit au1, AlternativeUnit au2)
			{
				return au1.getWords().length - au2.getWords().length;
			}
		});

		////////////////////////////////////////////////////////////////////////
		aus.get(0).setWeight(1);
		for (int index = 1; index < aus.size(); index++)
		{
			AlternativeUnit au_current = aus.get(index);

			AlternativeUnit au_previous = null;
			for (int j = 0; j < index; j++)
			{
				au_previous = aus.get(j);

				if (Arrays.asList(au_current.getWords()).containsAll(
						Arrays.asList(au_previous.getWords())))
				{
					au_current.setWeight(au_previous.getWeight() + 1);
				}
				else
				{
					if (au_current.getWeight() < au_previous.getWeight())
					{
						au_current.setWeight(au_previous.getWeight());
					}
				}
			}
		}
	}
}
