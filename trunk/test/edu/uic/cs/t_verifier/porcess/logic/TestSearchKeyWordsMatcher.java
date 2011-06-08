package edu.uic.cs.t_verifier.porcess.logic;

import java.util.ArrayList;
import java.util.List;

import edu.uic.cs.t_verifier.porcess.logic.SearchKeyWordsMatcher;

import junit.framework.TestCase;

public class TestSearchKeyWordsMatcher extends TestCase
{
	SearchKeyWordsMatcher matcher = new SearchKeyWordsMatcher();

	//	public void testProcessEachTopicUnit_1()
	//	{
	//		List<String> topicUints = new ArrayList<String>();
	//		topicUints
	//				.add("is the leading actor in the movie Sleepless in seattle");
	//
	//		matcher.processAllTopicUnitsInStatement(topicUints);
	//	}
	//
	//	public void testProcessEachTopicUnit_2()
	//	{
	//		List<String> topicUints = new ArrayList<String>();
	//		topicUints.add("String go String go String");
	//
	//		matcher.processAllTopicUnitsInStatement(topicUints);
	//	}
	//	
	//	public void testProcessEachTopicUnit_3()
	//	{
	//		List<String> topicUints = new ArrayList<String>();
	//		topicUints.add("nixon visited china in");
	//
	//		matcher.processAllTopicUnitsInStatement(topicUints);
	//	}

	public void testProcessEachTopicUnit_4()
	{
		List<String> topicUints = new ArrayList<String>();
		topicUints.add("won the nobel peace prize in 1991");

		System.out.println(matcher.processAllTopicUnitsInStatement(topicUints));
	}
}
