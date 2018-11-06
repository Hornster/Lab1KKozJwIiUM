package pl.polsl.utils;

import pl.polsl.data.IntegralData;
import pl.polsl.data.QueryHistory;
import pl.polsl.data.SingleQuery;
import pl.polsl.exceptions.NoQueryFoundException;

/**Manages the query history - responsible for creating, storing and searching for integral calculation query
 * data.
 * @author Kozuch Karol
 * @version 1.0*/
public class QueryManager {
    /**Stores queries' data.*/
    private QueryHistory history = new QueryHistory();

    public void addQuery(SingleQuery newQuery) {
        history.addItem(newQuery);
    }
    /**Returns query saved under given index.
     * @param index Index of requested query.
     * @return Query saved under passed index.*/
    public SingleQuery getQuery(int index)throws NoQueryFoundException {
        return history.getQuery(index);
    }
    /**Returns last performed query data.
     * @return Last saved query.*/
    public SingleQuery getLastQuery()throws NoQueryFoundException
    {
        return history.getQuery();
    }
    /**Gets the query storing object, for ease of iteration through.
     * @return Object that stores all queries.*/
    public QueryHistory getQueryHistory()
    {
        return history;
    }
}
