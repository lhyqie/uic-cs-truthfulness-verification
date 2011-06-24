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
						"five", "it", "is", "life", "of", /*"s",*/"the",
						"years" }));

		assertEquals(expected, actual);
	}

	public void testAssignWeightToAUs_1()
	{
		Statement statement = new Statement(null, null);
		statement.addAlternativeUnit("a b c d");
		statement.addAlternativeUnit("a b");
		statement.addAlternativeUnit("c d");
		statement.addAlternativeUnit("b c");
		statement.addAlternativeUnit("a");
		statement.addAlternativeUnit("b");
		statement.addAlternativeUnit("c");

		TreeSet<String> actual = new TreeSet<String>();
		for (AlternativeUnit au : statement.getAlternativeUnits())
		{
			System.out.println(au.toString());
			actual.add(au.toString());
		}

		TreeSet<String> expected = new TreeSet<String>(
				Arrays.asList(new String[] { "a[1]", "b[1]", "c[1]", "a b[2]",
						"c d[2]", "b c[2]", "a b c d[3]" }));

		assertEquals(expected, actual);
	}

	public void testAssignWeightToAUs_2()
	{
		Statement statement = new Statement(null, null);
		statement.addAlternativeUnit("a b c d");
		statement.addAlternativeUnit("a b c");
		statement.addAlternativeUnit("b c d");
		statement.addAlternativeUnit("a b");
		statement.addAlternativeUnit("c d");
		statement.addAlternativeUnit("a");
		statement.addAlternativeUnit("b");
		statement.addAlternativeUnit("c");

		TreeSet<String> actual = new TreeSet<String>();
		for (AlternativeUnit au : statement.getAlternativeUnits())
		{
			System.out.println(au.toString());
			actual.add(au.toString());
		}

		TreeSet<String> expected = new TreeSet<String>(
				Arrays.asList(new String[] { "a[1]", "b[1]", "c[1]", "a b[2]",
						"c d[2]", "a b c[3]", "b c d[3]", "a b c d[4]" }));

		assertEquals(expected, actual);
	}

}
