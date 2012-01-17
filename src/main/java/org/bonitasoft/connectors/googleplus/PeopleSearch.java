package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

/**
 * Bonita connector for service People:search.
 * 
 * @author sebastien.prunier
 */
public class PeopleSearch extends GooglePlusConnector<List<Person>> {

    // Full-text search query string.
    private String query;
    // Max number of results
    private Long maxResults;

    @Override
    protected List<Person> executeAction(Plus plus) throws Exception {
        // Check maxResults value
        if (maxResults == null) {
            maxResults = MAX_SEARCH_VALUES;
        }

        // Execute People:search query
        Plus.People.Search searchPeople = plus.people().search();
        searchPeople.setQuery(query);
        searchPeople.setMaxResults(PAGINATION_SIZE);
        PeopleFeed peopleFeed = searchPeople.execute();
        List<Person> people = peopleFeed.getItems();

        // Manage pagination
        List<Person> finalResult = new ArrayList<Person>();
        Integer finalResultSize = 0;
        while (people != null && finalResultSize < maxResults) {
            // Add page results to final result
            finalResult.addAll(people);
            finalResultSize += people.size();

            // We will know we are on the last page when the next page token is null.
            // If this is the case, break.
            if (peopleFeed.getNextPageToken() == null) {
                break;
            }

            // Prepare the next page of results
            searchPeople.setPageToken(peopleFeed.getNextPageToken());

            // Execute and process the next page request
            peopleFeed = searchPeople.execute();
            people = peopleFeed.getItems();
        }

        // Keep only the 'maxResults' people.
        if (finalResult.size() > maxResults) {
            finalResult = finalResult.subList(0, maxResults.intValue());
        }

        return finalResult;
    }

    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        // Check maxResults
        if (maxResults != null && maxResults <= 0) {
            errors.add(new ConnectorError("maxResults", new IllegalArgumentException("maxResults must be greater than 0 !")));
        }

        return errors;
    }

    /*
     * Getters and Setters.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }
}
