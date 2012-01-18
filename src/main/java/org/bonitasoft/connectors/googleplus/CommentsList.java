package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.CommentFeed;

public class CommentsList extends GooglePlusConnector {

    // The ID of the activity to get comments for.
    private String activityId;
    // Max number of results
    private Long maxResults;
    // Connector result
    private List<Comment> result;

    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();

        // Check maxResults value
        if (maxResults == null) {
            maxResults = MAX_SEARCH_VALUES;
        }

        // Execute Comments:list query
        Plus.Comments.List listComments = plus.comments().list(activityId);
        listComments.setMaxResults(PAGINATION_SIZE);
        CommentFeed commentFeed = listComments.execute();
        List<Comment> comments = commentFeed.getItems();

        // Manage pagination
        result = new ArrayList<Comment>();
        Integer finalResultSize = 0;
        while (comments != null && finalResultSize < maxResults) {
            // Add page results to final result
            result.addAll(comments);
            finalResultSize += comments.size();

            // We will know we are on the last page when the next page token is null.
            // If this is the case, break.
            if (commentFeed.getNextPageToken() == null) {
                break;
            }

            // Prepare the next page of results
            listComments.setPageToken(commentFeed.getNextPageToken());

            // Execute and process the next page request
            commentFeed = listComments.execute();
            comments = commentFeed.getItems();
        }

        // Keep only the 'maxResults' comments.
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

        // Check activityId
        if (activityId == null || "".equals(activityId)) {
            errors.add(new ConnectorError("activityId", new IllegalArgumentException("activityId is not valid !")));
        }

        return errors;
    }

    /*
     * Getters and Setters
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public List<Comment> getResult() {
        return result;
    }

}
