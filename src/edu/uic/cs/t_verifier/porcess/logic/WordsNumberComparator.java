package edu.uic.cs.t_verifier.porcess.logic;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

class WordsNumberComparator implements Comparator<String>
{
	@Override
	public int compare(String s1, String s2)
	{
		int length1 = StringUtils.split(s1).length;
		int length2 = StringUtils.split(s2).length;
		if (length1 != length2)
		{
			return length2 - length1;
		}
		else
		{
			return s1.compareTo(s2);
		}
	}

}
