package edu.uic.cs.t_verifier;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public abstract class EnhancedTestCase extends TestCase
{
	protected <T> void assertEquals(Set<T> expected, Set<T> actual)
	{
		assertEquals(expected.size(), actual.size());
		assertTrue("Must be TreeSet", (expected instanceof TreeSet)
				&& (actual instanceof TreeSet));

		Iterator<T> expectedIterator = expected.iterator();
		Iterator<T> actualIterator = actual.iterator();
		while (expectedIterator.hasNext())
		{
			assertEquals(expectedIterator.next(), actualIterator.next());
		}
	}

	protected <T> void assertEquals(List<T> expected, List<T> actual)
	{
		assertEquals(expected.size(), actual.size());

		for (int index = 0; index < expected.size(); index++)
		{
			assertEquals(expected.get(index), actual.get(index));
		}
	}

	protected List<String> getAllExpected()
	{
		String expectedFileName = this.getClass().getSimpleName() + ".expected";
		return getAllExpected(expectedFileName);
	}

	protected List<String> getAllExpected(String expectedFileName)
	{
		try
		{

			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(new File(
					getPathOfFile(expectedFileName)));
			return lines;
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}

	}

	private String getPathOfFile(String fileName)
	{
		String path = this.getClass().getResource(fileName).getPath();
		return path;
	}

}
