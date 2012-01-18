package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class ActivitiesSearch extends GooglePlusConnector {

    // Full-text search query string.
    private String query;
    // Specifies how to order search results.
    private String orderBy;
    // Max number of results
    private Long maxResults;
    // Connector result
    private List<Activity> result;

    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();

        // Check maxResults value
        if (maxResults == null) {
            maxResults = MAX_SEARCH_VALUES;
        }

        // Execute Activities:search query
        Plus.Activities.Search searchActivities = plus.activities().search();
        searchActivities.setQuery(query);
        searchActivities.setOrderBy(orderBy);
        searchActivities.setMaxResults(PAGINATION_SIZE);
        ActivityFeed activityFeed = searchActivities.execute();
        List<Activity> activities = activityFeed.getItems();

        // Manage pagination
        result = new ArrayList<Activity>();
        Integer finalResultSize = 0;
        while (activities != null && finalResultSize < maxResults) {
            // Add page results to final result
            result.addAll(activities);
            finalResultSize += activities.size();

            // We will know we are on the last page when the next page token is null.
            // If this is the case, break.
            if (activityFeed.getNextPageToken() == null) {
                break;
            }

            // Prepare the next page of results
            searchActivities.setPageToken(activityFeed.getNextPageToken());

            // Execute and process the next page request
            activityFeed = searchActivities.execute();
            activities = activityFeed.getItems();
        }

        // Keep only the 'maxResults' activities.
        if (result.size() > maxResults) {
            result = result.subList(0, maxResults.intValue());
        }

    }

    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        // For 'orderBy', acceptable values are:
        // -> "best" - Sort activities by relevance to the user, most relevant first.
        // -> "recent" - Sort activities by published date, most recent first. (default)
        if (!"best".equalsIgnoreCase(orderBy) && !"recent".equalsIgnoreCase(orderBy)) {
            errors.add(new ConnectorError("orderBy", new IllegalArgumentException("Acceptable values are 'plusoners' and 'resharers'")));
        }

        // Check maxResults
        if (maxResults != null && maxResults <= 0) {
            errors.add(new ConnectorError("maxResults", new IllegalArgumentException("maxResults must be greater than 0 !")));
        }

        return errors;
    }

    /*
     * Getters and Setters
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public List<Activity> getResult() {
        return result;
    }
}
