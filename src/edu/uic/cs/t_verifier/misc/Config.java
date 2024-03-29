package edu.uic.cs.t_verifier.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
	private static final Properties CONFIG = new Properties();
	static
	{
		InputStream inStream = ClassLoader
				.getSystemResourceAsStream("config.properties");

		try
		{
			CONFIG.load(inStream);
		}
		catch (IOException e)
		{
			throw new GeneralException(e);
		}
	}

	public static int MATCH_WINDOW_SIZE = Integer.parseInt(CONFIG
			.getProperty("match_window_size"));

	public static long POLITENESS_INTERVAL = Long.parseLong(CONFIG
			.getProperty("politeness_interval"));

	public static String PageContentExtractor_CLASS_NAME = CONFIG
			.getProperty("PageContentExtractor_class_name");

}
