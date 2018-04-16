import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Stream;import com.sun.media.jfxmedia.events.NewFrameEvent;

/**
 * This class adds additional functionality to the graph as a whole.
 * 
 * Contains an instance variable, {@link #graph}, which stores information for all the vertices and edges.
 * @see #populateGraph(String)
 *  - loads a dictionary of words as vertices in the graph.
 *  - finds possible edges between all pairs of vertices and adds these edges in the graph.
 *  - returns number of vertices added as Integer.
 *  - every call to this method will add to the existing graph.
 *  - this method needs to be invoked first for other methods on shortest path computation to work.
 * @see #shortestPathPrecomputation()
 *  - applies a shortest path algorithm to precompute data structures (that store shortest path data)
 *  - the shortest path data structures are used later to 
 *    to quickly find the shortest path and distance between two vertices.
 *  - this method is called after any call to populateGraph.
 *  - It is not called again unless new graph information is added via populateGraph().
 * @see #getShortestPath(String, String)
 *  - returns a list of vertices that constitute the shortest path between two given vertices, 
 *    computed using the precomputed data structures computed as part of {@link #shortestPathPrecomputation()}.
 *  - {@link #shortestPathPrecomputation()} must have been invoked once before invoking this method.
 * @see #getShortestDistance(String, String)
 *  - returns distance (number of edges) as an Integer for the shortest path between two given vertices
 *  - this is computed using the precomputed data structures computed as part of {@link #shortestPathPrecomputation()}.
 *  - {@link #shortestPathPrecomputation()} must have been invoked once before invoking this method.
 *  
 * @author sapan (sapan@cs.wisc.edu)
 * 
 */
public class GraphProcessor {

    /**
     * Graph which stores the dictionary words and their associated connections
     */
    private Graph<String> graph; // graph that contains the vertecies
    private ArrayList<Routes> allPaths; //  an array list contains all the shortest paths

    /**
     * Constructor for this class. Initializes instances variables to set the starting state of the object
     */
    public GraphProcessor() {
        this.graph = new Graph<String>();
        this.allPaths = new ArrayList<Routes>();
    }

    /**
     * Builds a graph from the words in a file. Populate an internal graph, by adding words from the dictionary as vertices
     * and finding and adding the corresponding connections (edges) between 
     * existing words.
     * 
     * Reads a word from the file and adds it as a vertex to a graph.
     * Repeat for all words.
     * 
     * For all possible pairs of vertices, finds if the pair of vertices is adjacent {@link WordProcessor#isAdjacent(String, String)}
     * If a pair is adjacent, adds an undirected and unweighted edge between the pair of vertices in the graph.
     * 
     * @param filepath file path to the dictionary
     * @return Integer the number of vertices (words) added
     * @throws FileNotFoundException 
     */
    public Integer populateGraph(String filepath){
        Integer verCount = 0;
        Scanner scanner;

        try {
            WordProcessor.getWordStream(filepath);
            scanner = new Scanner(new File(filepath));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }

        //  creating vertices 
        graph.addVertex(scanner.next());
        verCount++;
        while (scanner.hasNext()) {
            String newWord = scanner.next();
            graph.addVertex(newWord);
            for (String string1 : graph.getAllVertices()) {
                if(WordProcessor.isAdjacent(string1, newWord)) {
                    graph.addEdge(string1, newWord);
                }
            }
            verCount++;
        }
        shortestPathPrecomputation();
        scanner.close();
        return verCount;
    }


    /**
     * Gets the list of words that create the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  shortest path between cat and wheat is the following list of words:
     *     [cat, hat, heat, wheat]
     * 
     * @param word1 first word
     * @param word2 second word
     * @return List<String> list of the words
     */
    public List<String> getShortestPath(String word1, String word2) {
        String[] ary = {word1, word2};
        for (int i = 0; i < allPaths.size(); i++) {
            if(Arrays.equals(allPaths.get(i).strings, ary)) {
                return allPaths.get(i).list;
            }
        }
        return null;
    }

    /**
     * Gets the distance of the shortest path between word1 and word2
     * 
     * Example: Given a dictionary,
     *             cat
     *             rat
     *             hat
     *             neat
     *             wheat
     *             kit
     *  distance of the shortest path between cat and wheat, [cat, hat, heat, wheat]
     *   = 3 (the number of edges in the shortest path)
     * 
     * @param word1 first word
     * @param word2 second word
     * @return Integer distance
     */
    public Integer getShortestDistance(String word1, String word2) {
        String[] ary = {word1, word2};
        for (int i = 0; i < allPaths.size(); i++) {
            if(Arrays.equals(allPaths.get(i).strings, ary)) {
                if (allPaths.get(i).list == null) { //  if the list is null then return null
                    return null;
                } else {
                    return allPaths.get(i).list.size();
                }
            }
        }
        return null;
    }

    /**
     * Computes shortest paths and distances between all possible pairs of vertices.
     * This method is called after every set of updates in the graph to recompute the path information.
     * Any shortest path algorithm can be used (Djikstra's or Floyd-Warshall recommended).
     */
    public void shortestPathPrecomputation() {
        for (String start : graph.getAllVertices()) {
            for (String end : graph.getAllVertices()) {
                String[] temp = {start, end};
                LinkedList<String> list = helper(start, end);
                this.allPaths.add(new Routes(temp, list));
            }
        }
    }

    //  using BFS
    private LinkedList<String> helper(String start, String end) {
        HashMap<String, String> pathMap = new HashMap<>(); //  contains every node to other node's shortest path
        LinkedList<String> returnList = new LinkedList<>(); //  the list will be returned
        Queue<String> queue = new LinkedList<>(); //  a queue of elements 


        //  initializing variables
        String current = null;
        pathMap.put(start, null);
        queue.add(start);

        //trying to find the path
        if(start.equals(end)) { //  if start vertex and end vertex is the same vertex
            returnList.add(start);
            return returnList; 
        }else {
            while(!queue.isEmpty()) {
                current = queue.poll();//update current
                if (current.equals(end)) {
                    break;
                }else {
                    for (String string : graph.getNeighbors(current)) {
                        if (!pathMap.containsKey(string)) {
                            queue.add(string);
                            pathMap.put(string, current);
                        }
                    }
                }
            }
        }

        if(!current.equals(end)) {return null;} //  means did not find the end

        Stack<String> stack = new Stack<>();
        String key = end;
        while(pathMap.get(key)!=null) {
            stack.push(key);
            key = pathMap.get(key);
        }
        stack.push(key); //  adding the start vertex to the stack
        while(!stack.empty()) { //  reversing the list order to the normal one to the return list
            returnList.add(stack.pop());
        }
        return returnList;
    }
}

class Routes { //  a class that contains the start and end verticies' shortest path
    String[] strings; // index 1 is start vertx's value, index 2 is end vertex's value
    LinkedList<String> list; //  the shortest path

    public Routes(String[] strings, LinkedList<String> list) {
        this.strings = strings;
        this.list = list;
    }

    public String toString() {
        return "!!"+strings[0]+", "+strings[1]+"!!";
    }
}
