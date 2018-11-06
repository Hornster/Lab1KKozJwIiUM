package pl.polsl.data;

import pl.polsl.exceptions.NoQueryFoundException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**Manages collection  that stores all queries that have been made
 * by the user during runtime.
 *
 * @author Karol Kozuch
 * @version 1.0*/
public class QueryHistory implements Iterable{
    /**Amount of mementos in this collection.*/
    private static int itemsCount=0;
    /**All queries made by the user during single runtime.*/
    private Map<Integer, SingleQuery> queries = new HashMap<>();
    /**Adds single query to the history.
     * @param singleQuery Object to add.*/
    public void addItem(SingleQuery singleQuery)
    {
        queries.put(itemsCount, singleQuery);
        itemsCount++;
    }
    /**Returns a copy of single query. If none available - returns null.
     * @param index Index of requested query.
     * @return Requested query or null, if none found under given index.*/
    public SingleQuery getQuery(int index) throws NoQueryFoundException
    {
        if(queries.size() > index && index >= 0) {
            return new SingleQuery(queries.get(index));
        }
        else
        {
            throw new NoQueryFoundException("Tried to access non-existing query under " + index + " index! ");
        }
    }
    /**Returns a copy of last query inserted into collection.
     * @return Last query or null, if no queries available.*/
    public SingleQuery getQuery() throws NoQueryFoundException
    {
        if(queries.size() > 0) {
            return new SingleQuery(queries.get(queries.size() - 1));
        }
        else {
            throw new NoQueryFoundException("Tried to access non-existing query - the collection is empty!");
        }
    }
    /**An iterator used in foreach loop.
     * @return Iterator for foreach loop - allows iterating through SingleQueries in the collection.*/
    @Override
    public Iterator iterator() {
        return new QueryHistoryIterator();
    }
    /**Iterator for QueryHistory. Allows iteration through the queries collection.
     * @author Karol Kozuch
     * @version 1.0*/
    public class QueryHistoryIterator implements Iterator<SingleQuery>
    {
        /**The index of current element.*/
        private int i = -1;
        /**Current element which is pointed at by the iterator.*/
        private SingleQuery current = null;
        /**Checks if the collection has next item.
         * @return Returns true if there's next item in the collection. False otherwise.*/
        @Override
        public boolean hasNext() {
            if(i < queries.size())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        /**Moves iterator to next element in the collection.
         * @return Next element in the collection.*/
        @Override
        public SingleQuery next() {
            i++;
            if(hasNext())
            {
                current = queries.get(i);
            }

            return current;
        }
    }

}
