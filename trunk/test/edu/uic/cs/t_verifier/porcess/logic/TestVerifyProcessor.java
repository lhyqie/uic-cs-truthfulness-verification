package edu.uic.cs.t_verifier.porcess.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestVerifyProcessor
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite(TestVerifyProcessor.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(TestVerifyProcessor1.class);
		suite.addTestSuite(TestVerifyProcessor2.class);
		suite.addTestSuite(TestVerifyProcessor3.class);
		suite.addTestSuite(TestVerifyProcessor4.class);
		suite.addTestSuite(TestVerifyProcessor5.class);
		//$JUnit-END$
		return suite;
	}

}
