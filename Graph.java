///////////////////////////////////////////////////////////////////////////////
// assignment name: p4
// Author: Bowen Zhang
// Partner:GRIFF ZHANG JICHEN ZHANG JUNGE ZHANG Tianyuan(Rainer) Yuan
// Email : bzhang296@wisc.edu
// due date: April 15th 2018
// CS Login: bowenz
// Credits: none
// known bugs: none
//////////////////////////////////////////////////////////////////////////////
import java.util.*;


/**
 * Undirected and unweighted graph implementation
 *
 * @param <E> type of a vertex
 *
 * @author sapan (sapan@cs.wisc.edu)
 *
 */
public class Graph<E> implements GraphADT<E> {

    /**
     * Instance variables and constructors
     */
	
  
	private HashMap<E,ArrayList<E>>dictionary;
	
	/**
	  * constructor for graph class
	  */
	public Graph() {
		dictionary=new HashMap<>();
	}
   
   
    /**
     * add vertex 
     * parameter  E vertex 
     * return null if duplicate or add null
     * otherwise add to arraylist 
     */
    @Override
    public E addVertex(E vertex) {
        if (vertex == null || dictionary.containsKey(vertex)) {
            return null;
        } else {
            dictionary.put(vertex, new ArrayList<>());
            return vertex;
        }
    }

    /**
     * remove vertex
     * parameter E vertex
     * return null if vertex is null or arraylist does not have this vertex
     * else remove vertex from array list
     */
    @Override
    public E removeVertex(E vertex) {
        if (vertex == null || !dictionary.containsKey(vertex)) {
            return null;
        } else {
            for (E opposite : getNeighbors(vertex)) {
                dictionary.get(opposite).remove(vertex);
            }
            dictionary.remove(vertex);
            return vertex;
        }
    }

    /**
     * add Edge
     * parameter E vertex1,  E vertex2
     * return true if both vertices exist and are in array list 
     * else return false
     */
    @Override
    public boolean addEdge(E vertex1, E vertex2) {
        if (vertex1 != null
                && vertex2 != null
                && vertex1 != vertex2
                && dictionary.containsKey(vertex1)
                && dictionary.containsKey(vertex2)) {
            dictionary.get(vertex1).add(vertex2);
            dictionary.get(vertex2).add(vertex1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * remove edge
     * parameter E vertex1  E vertex2
     * return true if both vertices exist and are in the array list
     * and there is an edge between two vertices 
     * else return false
     */
    @Override
    public boolean removeEdge(E vertex1, E vertex2) {
        if (vertex1 != null
                && vertex2 != null
                && vertex1 != vertex2
                && dictionary.containsKey(vertex1)
                && dictionary.containsKey(vertex2)
                && dictionary.get(vertex1).contains(vertex2)
                && dictionary.get(vertex2).contains(vertex1)) {
            dictionary.get(vertex1).remove(vertex2);
            dictionary.get(vertex2).remove(vertex1);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Check whether the two vertices are adjacent
     * parameter vertex1  vertex2 
     * return true if both the vertices have an edge with each other, else return false 
     * if vertex1 and vertex2 are not connected (also if valid conditions are violated)
     */
    @Override
    public boolean isAdjacent(E vertex1, E vertex2) {
        if (vertex1 != null
                && vertex2 != null
                && vertex1 != vertex2
                && dictionary.containsKey(vertex1)
                && dictionary.containsKey(vertex2)) {
            return dictionary.get(vertex1).contains(vertex2) && dictionary.get(vertex2).contains(vertex1);
        } else {
            return false;
        }
    }

    /**
     * Get all the neighbor vertices of a vertex
     * parameter vertex
     * return  an iterable for all the immediate connected neighbor vertices
     */
    @Override
    public Iterable<E> getNeighbors(E vertex) {
        if (vertex != null && dictionary.containsKey(vertex)) {
            return dictionary.get(vertex);
        } else {
            return null;
        }
    }

    /**
     * Get all the vertices in the graph
     * @return an iterable for all the vertices
     */
    @Override
    public Iterable<E> getAllVertices() {
        return dictionary.keySet();
    }
}
