package edu.uic.cs.t_verifier.data;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import edu.uic.cs.t_verifier.EnhancedTestCase;

public class TestStatement extends EnhancedTestCase
{

	public void testGetAllWordsInTopicUnits_1()
	{
		Statement statement = new Statement(
				"the life expectancy of an elephant is", "the years");
		Set<String> actual = statement.getAllWordsInTopicUnits();
		TreeSet<String> expected = new TreeSet<String>(
				Arrays.asList(new String[] { "an", "elephant", "expectancy",
						"is", "life", "of", "the", "years" }));

		assertEquals(expected, actual);
	}

	public void testGetAllWordsInTopicUnits_2()
	{
		Statement statement = new Statement(
				"the life expectancy of an elephant is", "it's five years");
		Set<String> actual = statement.getAllWordsInTopicUnits();
		TreeSet<String> expected = new TreeSet<String>(
				Arrays.asList(new String[] { "an", "elephant", "expectancy",
						"five", "it", "is", "life", "of", "s", "the", "years" }));

		assertEquals(expected, actual);
	}

}