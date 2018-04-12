
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
    HashMap<E, ArrayList<E>> dictionary = new HashMap<>();

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public Iterable<E> getAllVertices() {
        return dictionary.keySet();
    }
}
