package edu.uic.cs.t_verifier.porcess.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class TestAbstractWordOperations extends TestCase
{

	AbstractWordOperations operations = new AbstractWordOperations()
	{
	};

	public void testConstructPostingList()
	{
		List<List<String>> stemmedParagraphs = new ArrayList<List<String>>();

		stemmedParagraphs.add(Arrays.asList(new String[] { "this", "is", "a",
				"test", "a", "real", "test" }));
		stemmedParagraphs.add(Arrays.asList(new String[] { "an", "other",
				"test", "other", "other", "other", "test" }));

		List<Map<String, List<Integer>>> actual = operations
				.constructPostingList(stemmedParagraphs);

		assertEquals(2, actual.size());

		Map<String, List<Integer>> paragraph_1 = actual.get(0);
		assertEquals(5, paragraph_1.size());
		assertEquals(Arrays.asList(new Integer[] { 0 }),
				paragraph_1.get("this"));
		assertEquals(Arrays.asList(new Integer[] { 1 }), paragraph_1.get("is"));
		assertEquals(Arrays.asList(new Integer[] { 2, 4 }),
				paragraph_1.get("a"));
		assertEquals(Arrays.asList(new Integer[] { 3, 6 }),
				paragraph_1.get("test"));
		assertEquals(Arrays.asList(new Integer[] { 5 }),
				paragraph_1.get("real"));

		Map<String, List<Integer>> paragraph_2 = actual.get(1);
		assertEquals(3, paragraph_2.size());
		assertEquals(Arrays.asList(new Integer[] { 0 }), paragraph_2.get("an"));
		assertEquals(Arrays.asList(new Integer[] { 1, 3, 4, 5 }),
				paragraph_2.get("other"));
		assertEquals(Arrays.asList(new Integer[] { 2, 6 }),
				paragraph_2.get("test"));

	}

//	public void testPowerset_1()
//	{
//		TreeSet<String> topicUnits = new TreeSet<String>(
//				Arrays.asList(new String[] { "a", "b", "c" }));
//
//		List<List<String>> actual = operations.powerset(topicUnits, true);
//
//		System.out.println(actual);
//		assertEquals(8, actual.size());
//		assertEquals(Arrays.asList(new String[] {}), actual.get(0));
//		assertEquals(Arrays.asList(new String[] { "a" }), actual.get(1));
//		assertEquals(Arrays.asList(new String[] { "b" }), actual.get(2));
//		assertEquals(Arrays.asList(new String[] { "c" }), actual.get(3));
//		assertEquals(Arrays.asList(new String[] { "a", "b" }), actual.get(4));
//		assertEquals(Arrays.asList(new String[] { "a", "c" }), actual.get(5));
//		assertEquals(Arrays.asList(new String[] { "b", "c" }), actual.get(6));
//		assertEquals(Arrays.asList(new String[] { "a", "b", "c" }),
//				actual.get(7));
//	}
//
//	public void testPowerset_2()
//	{
//		TreeSet<String> topicUnits = new TreeSet<String>(
//				Arrays.asList(new String[] { "a", "b", "c" }));
//
//		List<List<String>> actual = operations.powerset(topicUnits, false);
//
//		System.out.println(actual);
//		assertEquals(7, actual.size());
//		assertEquals(Arrays.asList(new String[] { "a" }), actual.get(0));
//		assertEquals(Arrays.asList(new String[] { "b" }), actual.get(1));
//		assertEquals(Arrays.asList(new String[] { "c" }), actual.get(2));
//		assertEquals(Arrays.asList(new String[] { "a", "b" }), actual.get(3));
//		assertEquals(Arrays.asList(new String[] { "a", "c" }), actual.get(4));
//		assertEquals(Arrays.asList(new String[] { "b", "c" }), actual.get(5));
//		assertEquals(Arrays.asList(new String[] { "a", "b", "c" }),
//				actual.get(6));
//
//		Collections.reverse(actual);
//		System.out.println(actual);
//	}
}
