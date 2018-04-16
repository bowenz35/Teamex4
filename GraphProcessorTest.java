import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import org.junit.*;
/**
 * This is a test class for the graph processor functionalities
 * @author jzhang877 (jzhang877@wisc.edu)
 *
 */
public class GraphProcessorTest {
	/**
	 * The path of the file which contains the dictionary to be tested
	 */
	private static String filepath;
	
	/**
	 * 
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		filepath = "test1.txt";
	}
	

	
	@Test
	public void populateGraphCorrectNumVertices() throws IOException {
		GraphProcessor p = new GraphProcessor();
		Integer actual = p.populateGraph(filepath);
		
		String[] words = WordProcessor.getWordStream(filepath).toArray(String[]::new);
		
		Integer expected = words.length;

		assertEquals("number of adjacencies", expected, actual);
	}
	
	@Test
	public void correctShortestDistance() throws IOException {
		GraphProcessor p = new GraphProcessor();
		p.populateGraph(filepath);
		p.shortestPathPrecomputation();
		
		String[] words = WordProcessor.getWordStream(filepath).toArray(String[]::new);
		Graph<String> g = new Graph<String>();
		for(String s : words) {
			g.addVertex(s);
		}
		
		for (int i = 0; i < words.length; i ++) {
			for (int j = i; j < words.length; j++) {
				//everything is counted once
				if (WordProcessor.isAdjacent(words[i], words[j])) {
					g.addEdge(words[i], words[j]);
				}
			}
		}
		
		//check for a random word that every pair involving it has the same length result from bfsSearch in this class
		//is the same as GraphProcessor.getShortestPath()
		int j = (int) (Math.random() * words.length);
		for (int i = 0; i < words.length; i++) {
			assertEquals("path length", bfsShortestPath(g, words[i], words[j]), p.getShortestPath(words[0], words[i]));
		}

	}
	
	//a shortest path implementation using a little Dijkstra and breath-first search
	private Integer bfsShortestPath(Graph<String> g, String source, String target) {
	    Map<String, Integer> dist = new TreeMap<String, Integer>();
	    Map<String, String> prev = new TreeMap<String, String>();

	    for(String v: g.getNeighbors(source)){
	        dist.put(v, null);
	        prev.put(v, null);
	    }
	    dist.put(source, 0);

	    Queue<String> q = new LinkedList<String>();
	    q.offer(source);
	    while(!q.isEmpty()){
	        String u = q.poll();
	        if(u == target)
	            break;
	        q.remove(u);
	        for(String neighbor: g.getNeighbors(u)){
	            int a = dist.get(u);
	            if(dist.get(neighbor) != null)
	                continue;
	            //BFS distance
	            dist.put(neighbor, a + 1);
	            prev.put(neighbor, u);
	            q.offer(neighbor);
	        }
	    }

	    List<String> s = new LinkedList<String>();
	    String u = target;
	    while(prev.get(u) != null){
	        s.add(u);
	        u = prev.get(u);
	    }
	    Integer result = null;
	    
	    if (s != null && s.size() == 0) {
	    	result = null;
	    }
	    return result;
	}
	
/*	private Integer testLength(int a,int b,String[] s)
	 {
	  int[] dis=new int[s.length];
	  for(int t=0;t<dis.length;t++)
	   dis[t]=-1;
	  dis[a]=0;
	  
	  for(int t0=0;t0<s.length;t0++)
	   for(int t1=0;t1<s.length;t1++)
	    for(int t2=0;t2<s.length;t2++)
	     if(WordProcessor.isAdjacent(s[t1],s[t2]))
	      if((dis[t2]==-1  || dis[t1]+1<dis[t2])&& dis[t1]!=-1)
	       dis[t2]=dis[t1]+1;
	  
	  Integer result = dis[b] == -1 ? null : dis[b];
	  
	  return result;

	 }*/
}

