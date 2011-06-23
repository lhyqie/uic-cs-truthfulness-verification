package edu.uic.cs.t_verifier.porcess.logic;

import java.util.List;

import edu.uic.cs.t_verifier.EnhancedTestCase;
import edu.uic.cs.t_verifier.data.Statement;
import edu.uic.cs.t_verifier.input.AlternativeUnitsReader;

public class TestVerifyProcessor2 extends EnhancedTestCase
{
	private List<Statement> allStatements = AlternativeUnitsReader
			.parseAllStatementsFromInputFiles();
	private VerifyProcessor processor = new VerifyProcessor();

	private List<String> allExpected = getAllExpected("TestVerifyProcessor.expected");

	private int getIndex()
	{
		String methodName = getName();
		int indexBegin = methodName.indexOf('_') + 1;
		return Integer.parseInt(methodName.substring(indexBegin)) - 1;
	}

//	public void testProcessVerificationForEachStatement_1()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_2()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_3()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_4()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_5()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_6()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_7()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_8()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_9()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_10()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}

	public void testProcessVerificationForEachStatement_11()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_12()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_13()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_14()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_15()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_16()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_17()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_18()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_19()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

	public void testProcessVerificationForEachStatement_20()
	{
		int index = getIndex();
		String actual = processor
				.processVerificationForEachStatement(allStatements.get(index));
		assertEquals(allExpected.get(index), actual);
	}

//	public void testProcessVerificationForEachStatement_21()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_22()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_23()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_24()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_25()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_26()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_27()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_28()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_29()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_30()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_31()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_32()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_33()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_34()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_35()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_36()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_37()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_38()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_39()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_40()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_41()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_42()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_43()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_44()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_45()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_46()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_47()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_48()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_49()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}
//
//	public void testProcessVerificationForEachStatement_50()
//	{
//		int index = getIndex();
//		String actual = processor
//				.processVerificationForEachStatement(allStatements.get(index));
//		assertEquals(allExpected.get(index), actual);
//	}

}
