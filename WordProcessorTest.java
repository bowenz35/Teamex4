import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is a test class for the WordProcessor's functionalities, from a given
 * path of a dictionary file
 * @author Junge Zhang (jzhang875@wisc.edu)
 * 		   Jichen Zhang (jzhang877@wisc.edu)
 *
 */
public class WordProcessorTest {
	
	/**
	 * This is the path of the dictionary
	 */
	public static String path;

	/**
	 * This method sets the path of dictionary that is used in the rest of the class
	 */
	@BeforeClass
	public static void setupBeforeClass() {
		path = "word_list.txt";
	}

	/**
	 * This test tests the getWordStream method. We construct an array of String from the path
	 * in a helper method in this class, and another array of String from 
	 * WordProcessor.getWordStream(path). This fails whenever the size, or any element of these
	 * arrays are not equal
	 * @throws IOException
	 */
	@Test
	public void getWordStreamTest() throws IOException {
		String[] expectedWords = readFromFile(path);
		String[] actualWords = WordProcessor.getWordStream(path).toArray(String[]::new);
		assertEquals("words length", expectedWords.length, actualWords.length);
		
		for (int i = 0; i < expectedWords.length; i++) {
			assertEquals("word", expectedWords[i], actualWords[i]);
		}
	}
	
	/**
	 * This method tests the isAdjacent() method. We construct an array of String from the path
	 * in a helper method in this class. We use another helper method to determine the truth 
	 * value of adjacencies between any 2 words. This fails whenever the truth value is
	 * different for any of the pairs of 2 words in the dictionary
	 * @throws IOException
	 */
	@Test
	public void isAdjacentTest() throws IOException{
		String[] words = readFromFile(path);
		for (int i = 0; i < words.length; i ++) {
			for (int j = i; j < words.length; j++) { 
				// every pair is only tested once. i.e. (i,j) = (j,i)
				String prompt = String.format("is adjacent between %1$s and %2$s", words[i], words[j]);
				assertEquals(prompt, testAdjacent(words[i], words[j]), 
						WordProcessor.isAdjacent(words[i], words[j]));
			}
		}
	}

	// Helper methods

	/**
	 * The helper method that determine whether 2 words are adjacent
	 * @param str0
	 * @param str1
	 * @return true if str0 and str1 are adjacent
	 */
	private boolean testAdjacent(String str0, String str1) {
		if (str0.equals(str1)) return false;
		int dif = str0.length() - str1.length();
		if (dif > 1 || dif < -1)
			return false;
		int shorter = Math.min(str0.length(), str1.length());
		int t0 = 0;
		int t1 = 0;
		for (; t0 < shorter && str0.charAt(t0) == str1.charAt(t0); t0++)
			;
		for (; t1 < shorter - t0 && str0.charAt(str0.length() - t1 - 1) == str1.charAt(str1.length() - t1 - 1); t1++)
			;

		int d0 = str0.length() - t0 - t1;
		int d1 = str1.length() - t0 - t1;

		return (d0 == 0 || d0 == 1) && (d1 == 0 || d1 == 1);

	}

	@SuppressWarnings("resource")
	/**
	 * The method convert the dictionary file into an array of Strings
	 * @param path
	 * @return an array, in which each entry represents a word
	 */
	private String[] readFromFile(String path) {
		// Read file
		String rawWords = null;
		try {
			rawWords = new Scanner(new File(path)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			fail("File not found!");
		}

		// Split into an array 
		String words[] = rawWords.toUpperCase().trim().split("\n");
		
		return words;
	}
}