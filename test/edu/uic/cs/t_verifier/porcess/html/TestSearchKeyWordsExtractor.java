package edu.uic.cs.t_verifier.porcess.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uic.cs.t_verifier.EnhancedTestCase;
import edu.uic.cs.t_verifier.data.SearchKeyWords;
import edu.uic.cs.t_verifier.data.SearchKeyWords.AmbiguityEntry;
import edu.uic.cs.t_verifier.porcess.html.impl.SearchKeyWordsExtractor;

public class TestSearchKeyWordsExtractor extends EnhancedTestCase
{
	private SearchKeyWordsExtractor extractor = new SearchKeyWordsExtractor();

	public void testGetStandardSearchKeyWords_successful()
	{
		String queryWords = " sleepless in SEATTLE ";
		SearchKeyWords standardQueeryWord = extractor
				.getStandardSearchKeyWords(queryWords);

		assertTrue(standardQueeryWord.isCertainly());
		assertEquals("Sleepless_in_Seattle", standardQueeryWord.getKeyWord());
	}

	public void testGetStandardSearchKeyWords_failed()
	{
		String queryWords = "hong wang";
		SearchKeyWords standardQueeryWord = extractor
				.getStandardSearchKeyWords(queryWords);

		assertNull(standardQueeryWord);
	}

	public void testGetStandardSearchKeyWords_ambiguous1()
	{
		String queryWords = "the electric";
		SearchKeyWords standardQueeryWord = extractor
				.getStandardSearchKeyWords(queryWords);

		assertEquals("The_Electric", standardQueeryWord.getKeyWord());

		assertFalse(standardQueeryWord.isCertainly());

		List<AmbiguityEntry> ambiguousEntries = standardQueeryWord
				.getAmbiguousEntries();

		List<String> actual = new ArrayList<String>();
		for (AmbiguityEntry entry : ambiguousEntries)
		{
			actual.add(entry.toString());
		}

		String[] expectedKeyWords = new String[] {
				"The_Electric_Blues_Company=[The Electric Blues Company, A backing band for former Animals keyboardist Alan Price]",
				"Electric_Slide=[Electric Slide, a four wall line dance]",
				"Electric_Cinema,_Birmingham=[Electric Cinema, Birmingham, the oldest running cinema in the UK]" };

		assertEquals(Arrays.asList(expectedKeyWords), actual);

	}

	public void testGetStandardSearchKeyWords_ambiguous2()
	{
		String queryWords = "Leo";
		SearchKeyWords standardQueeryWord = extractor
				.getStandardSearchKeyWords(queryWords);

		assertEquals("Leo", standardQueeryWord.getKeyWord());

		assertFalse(standardQueeryWord.isCertainly());

		List<AmbiguityEntry> ambiguousEntries = standardQueeryWord
				.getAmbiguousEntries();
		List<String> actual = new ArrayList<String>();
		for (AmbiguityEntry entry : ambiguousEntries)
		{
			actual.add(entry.toString());
		}

		String[] expectedKeyWords = new String[] {
				"Leo_(constellation)=[Leo (constellation), a section of the night sky]",
				"Leo_(astrology)=[Leo (astrology), a sign of the zodiac]",
				"Leo_(horse)=[Leo (horse), an American Quarter Horse]",
				"Leo_(text_editor)=[Leo (text editor), a computer program]",
				"The_Leo=[The Leo, a Filipino basketball award]",
				"Low_Earth_orbit=[Low Earth orbit]",
				"BC_Lions=[Leos, nickname for Canadian Football League's BC Lions]",
				"Leo_(given_name)=[Leo (given name)]",
				"Leo_(surname)=[Leo (surname)]",
				"Leonardo=[Leonardo]",
				"Leonid_(disambiguation)=[Leonid (disambiguation)]",
				"Leonidas_(disambiguation)=[Leonidas (disambiguation)]",
				"Leopold=[Leopold]",
				"King_Leo_(disambiguation)=[King Leo (disambiguation)]",
				"Pope_Leo_(disambiguation)=[Pope Leo (disambiguation)]",
				"Arakel_Babakhanian=[Arakel Babakhanian, an Armenian historian]",
				"Emperor_Leo_(disambiguation)=[Emperor Leo (disambiguation), several Byzantine emperors]",
				"House_of_Leo=[House of Leo, Byzantine dynasty]",
				"L%C3%A9o,_Burkina_Faso=[Léo, Burkina Faso, a town in Burkina Faso]",
				"Leo_Islands=[Leo Islands, in Nunavut, Canada]",
				"null=[Leo, Texas, community in Cooke County, Texas, United States]",
				"Leo_(band)=[Leo (band), a Missouri-based rock band, originally founded in Cleveland, Ohio]",
				"Leo_the_Lion_(MGM)=[Leo the Lion (MGM), mascot of Metro-Goldwyn-Mayer]",
				"Leo_Awards=[Leo Awards, a British Columbian television award]",
				"Leo_(film)=[Leo (film), a 2000 Spanish film by José Luis Borau]",
				"null=[Leo (2002 film), a 2002 drama by Mehdi Norowzian]",
				"null=[Leo (2007 film), a 2007 Swedish film by Josef Fares]",
				"List_of_Being_Erica_episodes#Season_1=[An episode in the television series Being Erica]",
				"Leo_(That_70%27s_Show)=[Leo (That 70's Show), a character played by Tommy Chong]",
				"Leo_(comics)=[Leo (comics), a character in the Marvel universe]",
				"Leo_(Yu-Gi-Oh!_5D%27s)=[Leo (Yu-Gi-Oh! 5D's), a character in the manga series Yu-Gi-Oh!]",
				"List_of_Tekken_characters#Introduced_in_Tekken_6_and_Tekken_6:_Bloodline_Rebellion=[A character in the video game series Tekken]",
				"Leo,_the_Royal_Cadet=[A character in the opera Leo, the Royal Cadet]",
				"Characters_of_Final_Fantasy_VI#Leo=[A character in the video game Final Fantasy VI]",
				"Red_Earth_(video_game)#Player_characters=[A character in Red Earth (video game) (aka Warzard)]",
				"LEO_(disambiguation)=[LEO (disambiguation)]" }; // TODO notie here, this link is for more disambiguation

		assertEquals(Arrays.asList(expectedKeyWords), actual);
	}

	public void testGetStandardSearchKeyWords_ambiguous3()
	{
		String queryWords = "Sleepless";
		SearchKeyWords standardQueeryWord = extractor
				.getStandardSearchKeyWords(queryWords);

		assertEquals("Sleepless", standardQueeryWord.getKeyWord());

		assertFalse(standardQueeryWord.isCertainly());

		List<AmbiguityEntry> ambiguousEntries = standardQueeryWord
				.getAmbiguousEntries();
		List<String> actual = new ArrayList<String>();
		for (AmbiguityEntry entry : ambiguousEntries)
		{
			actual.add(entry.toString());
		}

		String[] expectedKeyWords = new String[] {
				"Sleepless_(2001_film)=[Sleepless, a horror film of 2001 by Dario Argento]",
				"La_Anam=[La Anam (Sleepless), an Egyptian film]",
				"La_Anam_(novel)=[La Anam, an Arabic novel by Ihsan Abdel Quddous, basis for the film]",
				"Sleepless_(The_X-Files)=[\"Sleepless\", an episode of the television show The X-Files]",
				"null=[Sleepless (Charlie Huston 2010)]",

				/**
				 * TODO HERE we only extract the first link!
				 * 
				 * The Sleepless Trilogy (Beggars in Spain, Beggars and Choosers, 
				 * Beggars Ride by Nancy Kress, about how the world changes with 
				 * those genetically modified to never need sleep and the normal 
				 * humans who still do.
				 */
				"Beggars_in_Spain=[The Sleepless Trilogy (Beggars in Spain, Beggars and Choosers, Beggars Ride by Nancy Kress, about how the world changes with those genetically modified to never need sleep and the normal humans who still do.]",

				"Sleepless_(Peter_Wolf_album)=[Sleepless (Peter Wolf album)]",
				"Sleepless_(Kate_Rusby_album)=[Sleepless (Kate Rusby album)]",
				"Sleepless_(Jacksoul_album)=[Sleepless (Jacksoul album)]",
				"Sleepless:_The_Concise_King_Crimson=[Sleepless: The Concise King Crimson, an album by King Crimson]",
				"Sleepless_(song)=[\"Sleepless\" (song), a song by King Crimson from the album Three of a Perfect Pair]",
				"Sleepless_(Eric_Saade_song)=[\"Sleepless\" (Eric Saade song)]",
				"Day_of_Mourning_(album)=[\"Sleepless\", a song by Despised Icon that is featured on their fourth album, Day of Mourning]",
				"Insomnia=[Insomnia, a sleeping disorder characterized by persistent difficulty falling or staying asleep]" };

		assertEquals(Arrays.asList(expectedKeyWords), actual);
	}
}
