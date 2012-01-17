package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

/**
 * Bonita connector for service People:listByActivity.
 * 
 * @author sebastien.prunier
 */
public class PeopleListByActivity extends GooglePlusConnector {

    // The ID of the activity to get the list of people for.
    private String activityId;
    // The collection of people to list.
    private String collection;
    // Max number of results
    private Long maxResults;
    // Connector result
    private List<Person> result;

    /**
     * Executes the connector.
     */
    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();

        // Check maxResults value
        if (maxResults == null) {
            maxResults = MAX_SEARCH_VALUES;
        }

        // Execute People:listByActivity query
        Plus.People.ListByActivity listPeople = plus.people().listByActivity(activityId, collection);
        listPeople.setMaxResults(PAGINATION_SIZE);
        PeopleFeed peopleFeed = listPeople.execute();
        List<Person> people = peopleFeed.getItems();

        // Manage pagination
        result = new ArrayList<Person>();
        Integer finalResultSize = 0;
        while (people != null && finalResultSize < maxResults) {
            // Add page results to final result
            result.addAll(people);
            finalResultSize += people.size();

            // We will know we are on the last page when the next page token is null.
            // If this is the case, break.
            if (peopleFeed.getNextPageToken() == null) {
                break;
            }

            // Prepare the next page of results
            listPeople.setPageToken(peopleFeed.getNextPageToken());

            // Execute and process the next page request
            peopleFeed = listPeople.execute();
            people = peopleFeed.getItems();
        }

        // Keep only the 'maxResults' people.
        if (result.size() > maxResults) {
            result = result.subList(0, maxResults.intValue());
        }
    }

    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        // Check maxResults
        if (maxResults != null && maxResults <= 0) {
            errors.add(new ConnectorError("maxResults", new IllegalArgumentException("maxResults must be greater than 0 !")));
        }

        // For 'collection', acceptable values are:
        // -> "plusoners" - List all people who have +1'd this activity.
        // -> "resharers" - List all people who have reshared this activity.
        if (!"plusoners".equalsIgnoreCase(collection) && !"resharers".equalsIgnoreCase(collection)) {
            errors.add(new ConnectorError("collection", new IllegalArgumentException("Acceptable values are 'plusoners' and 'resharers'")));
        }

        return errors;
    }

    /*
     * Getters and Setters
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public List<Person> getResult() {
        return result;
    }

}
