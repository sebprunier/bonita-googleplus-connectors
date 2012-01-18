package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class ActivitiesList extends GooglePlusConnector {

    // The ID of the user to get activities for. The special value "me" can be used to indicate the authenticated user.
    private String userId;
    // The collection of activities to list.
    private String collection;
    // Max number of results
    private Long maxResults;
    // Connector result
    private List<Activity> result;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();

        // Check maxResults value
        if (maxResults == null) {
            maxResults = MAX_SEARCH_VALUES;
        }

        // Execute Activities:list query
        Plus.Activities.List listActivities = plus.activities().list(userId, collection);
        listActivities.setMaxResults(PAGINATION_SIZE);
        ActivityFeed activityFeed = listActivities.execute();
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
            listActivities.setPageToken(activityFeed.getNextPageToken());

            // Execute and process the next page request
            activityFeed = listActivities.execute();
            activities = activityFeed.getItems();
        }

        // Keep only the 'maxResults' activities.
        if (result.size() > maxResults) {
            result = result.subList(0, maxResults.intValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        // Check maxResults
        if (maxResults != null && maxResults <= 0) {
            errors.add(new ConnectorError("maxResults", new IllegalArgumentException("maxResults must be greater than 0 !")));
        }

        // For 'collection', acceptable values are:
        // -> "public" - All public activities created by the specified user.
        if (!"public".equalsIgnoreCase(collection)) {
            errors.add(new ConnectorError("collection", new IllegalArgumentException("Acceptable values are 'public'")));
        }

        // Check userId
        if (userId == null || !userId.matches("me|[0-9]+")) {
            errors.add(new ConnectorError("userId", new IllegalArgumentException("userId is not valid ! It must match : me|[0-9]+")));
        }

        return errors;
    }

    /*
     * Getters and setters
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public List<Activity> getResult() {
        return result;
    }

}
