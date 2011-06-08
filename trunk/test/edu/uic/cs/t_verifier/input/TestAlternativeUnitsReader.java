package edu.uic.cs.t_verifier.input;

import java.util.ArrayList;
import java.util.List;

import edu.uic.cs.t_verifier.EnhancedTestCase;
import edu.uic.cs.t_verifier.data.Statement;

public class TestAlternativeUnitsReader extends EnhancedTestCase
{
	public void testParseAllStatementsFromInputFiles()
	{
		List<Statement> actual = AlternativeUnitsReader
				.parseAllStatementsFromInputFiles();

		List<String> actualAll = new ArrayList<String>();
		for (Statement statement : actual)
		{
			for (String each : statement.getAllAlternativeStatements())
			{
				System.out.println(each);
			}

			actualAll.addAll(statement.getAllAlternativeStatements());
		}

		List<String> expected = getAllExpected("TestAlternativeUnitsReader.expected");
		assertEquals(expected, actualAll);
	}

}
