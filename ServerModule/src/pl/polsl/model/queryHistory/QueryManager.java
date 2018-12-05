package pl.polsl.model.queryHistory;

import pl.polsl.model.CalculationData;
import pl.polsl.model.IntegralData;
import pl.polsl.model.exceptions.NoQueryFoundException;

/**Manages the query history - responsible for creating, storing and searching for integral calculation query
 * data.
 * @author Kozuch Karol
 * @version 1.0*/
public class QueryManager implements CalcResultListener {
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

    @Override
    public void newCalculationPerformed(CalculationData calculationData, IntegralData integralData) {
        addQuery(new SingleQuery(integralData, calculationData));
    }

}
