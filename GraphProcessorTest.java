import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
		filepath = "test1.txt";
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
}
