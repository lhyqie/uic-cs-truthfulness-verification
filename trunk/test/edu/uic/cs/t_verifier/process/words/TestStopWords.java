package edu.uic.cs.t_verifier.process.words;

import edu.uic.cs.t_verifier.process.words.StopWords;
import junit.framework.TestCase;

public class TestStopWords extends TestCase
{
	public void testTrimStopWordsInBothSides_1()
	{
		String orginal = "is the leading actor in";
		String expected = "leading actor";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}

	public void testTrimStopWordsInBothSides_2()
	{
		String orginal = "this is the only way";
		String expected = "";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}

	public void testTrimStopWordsInBothSides_3()
	{
		String orginal = " He is the leading actor in";
		String expected = "leading actor";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}

	public void testTrimStopWordsInBothSides_4()
	{
		String orginal = "he is not the leader";
		String expected = "leader";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}

	public void testTrimStopWordsInBothSides_5()
	{
		String orginal = "father  he is not";
		String expected = "father";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}

	public void testTrimStopWordsInBothSides_6()
	{
		String orginal = "  ";
		String expected = "";
		String actual = StopWords.trimStopWordsInBothSides(orginal);

		assertEquals(expected, actual);
	}
}
