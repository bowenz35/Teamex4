import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;



/**
 * This is a test class for the graph processor functionalities, from a given
 * path of a dictionary file
 * 
 * @author Jichen Zhang (jzhang877@wisc.edu)
 * 		   Junge Zhang (jzhang875@wisc.edu)
 *
 */
public class GraphProcessorTest {
	/**
	 * The path of the file which contains the dictionary to be tested
	 */
	private static String filepath;

	/**
	 * This method sets the testing path used in the rest of the class. It is easy
	 * to change
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		filepath = "word_list.txt";
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
		String[] expectedWords = readFromFile(filepath);
		String[] actualWords = WordProcessor.getWordStream(filepath).toArray(String[]::new);
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
		String[] words = readFromFile(filepath);
		for (int i = 0; i < words.length; i ++) {
			for (int j = i; j < words.length; j++) { 
				// every pair is only tested once. i.e. (i,j) = (j,i)
				String prompt = String.format("is adjacent between %1$s and %2$s", words[i], words[j]);
				assertEquals(prompt, testAdjacent(words[i], words[j]), 
						WordProcessor.isAdjacent(words[i], words[j]));
			}
		}
	}

	
	/**
	 * This test tests whether the populateGraph() method returns the true number of
	 * vertices added to the graph. We use WordProcessor.getWordStream method to
	 * construct a String array, and then compare its size with the return of
	 * populateGraph().
	 * 
	 * Precondition: WordProcessor.getWordStream() works fine
	 * 
	 * @throws IOException
	 */
	@Test
	public void populateGraphCorrectNumVertices() throws IOException {
		GraphProcessor p = new GraphProcessor();
		Integer actual = p.populateGraph(filepath);

		String[] words = WordProcessor.getWordStream(filepath).toArray(String[]::new);

		Integer expected = words.length;

		assertEquals("number of vertices", expected, actual);
	}

	/**
	 * This is a test which test the getShortestPath method of GraphProcessor clsss
	 * by loading the file into a String[] and use our own helper method to
	 * determine the shortest path. We randomly choose a word and calculate all the
	 * paths to it, comparing the results between our method and getShortestPath().
	 * It fails if any of the shortest paths are different.
	 * 
	 * Precondition: populateGraph(), shortestPathPrecomputation() works fine
	 * WordProcessor.getWordStream(), isAdjacent() works fine
	 * 
	 * 
	 * @throws IOException
	 */
	@Test
	public void correctShortestDistance() throws IOException {
		GraphProcessor p = new GraphProcessor();
		p.populateGraph(filepath);
		p.shortestPathPrecomputation();

		String[] words = WordProcessor.getWordStream(filepath).toArray(String[]::new);
		Graph<String> g = new Graph<String>();
		for (String s : words) {
			g.addVertex(s);
		}

		for (int i = 0; i < words.length; i++) {
			for (int j = i; j < words.length; j++) {
				// everything is counted once
				if (WordProcessor.isAdjacent(words[i], words[j])) {
					g.addEdge(words[i], words[j]);
				}
			}
		}

		// check for a random word that every pair involving it has the same length
		// result from bfsSearch in this class is the same as getShortestPath()
		int i = (int) (Math.random() * words.length);
		for (int j = 0; i < words.length; i++) {
			assertEquals("path length", shortestPath(i, words)[j], p.getShortestPath(words[i], words[j]));
		}

	}

	/**
	 * This method calculates all the shortest paths from a single word to the rest
	 * of the graph
	 * 
	 * @param index,
	 *            of the word in the array
	 * @param words,
	 *            the array of words
	 * @return the shortest path from one word to the rest in an array for each
	 *         pair. If the two words are unreachable
	 */
	private Integer[] shortestPath(int index, String[] words) {

		// pre-cache edges
		boolean[][] edges = new boolean[words.length][words.length];
		for (int i = 0; i < words.length; i++)
			for (int j = 0; j < words.length; j++)
				edges[i][j] = WordProcessor.isAdjacent(words[i], words[j]);

		Integer[] distance = new Integer[edges.length];
		for (int t = 0; t < distance.length; t++)
			distance[t] = -1;
		distance[index] = 0;

		for (int i = 0; i < edges.length; i++)
			for (int j = 0; j < edges.length; j++)
				for (int k = 0; k < edges.length; k++)
					if (edges[j][k])
						if ((distance[k] == null || distance[j] + 1 < distance[k]) && distance[j] != -1)
							distance[k] = distance[j] + 1;

		// a conversion between -1 and null
		for (int i = 0; i < distance.length; i++) {
			distance[i] = distance[i] == -1 ? null : distance[i];
		}
		return distance;

	}
	
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

	/**
	 * This method remove all the duplicate Strings in an array
	 * @param lines
	 * @return
	 */
/*	private String[] removeDuplicates(String[] lines) {
		int repeat = 0;
		for (int t = 0; t < lines.length; t++) {
			lines[t - repeat] = lines[t];
			for (int t1 = 0; t1 < t - repeat; t1++) {
				if (lines[t1].equals(lines[t]))
					repeat++;
			}
		}
		int size = lines.length - repeat;
		String[] nlines = new String[size];
		for (int t = 0; t < size; t++)
			nlines[t] = lines[t];
		lines = nlines;
		return lines;
	}*/

