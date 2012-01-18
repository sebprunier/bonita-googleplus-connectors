package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Comment;

public class CommentsGet extends GooglePlusConnector {

    // The ID of the comment to get.
    private String commentId;
    // Connector result
    private Comment result;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();
        result = plus.comments().get(commentId).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();
        if (commentId == null || "".equals(commentId)) {
            errors.add(new ConnectorError("commentId", new IllegalArgumentException("commentId is not valid !")));
        }
        return errors;
    }

    /*
     * Getters and Setters
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Comment getResult() {
        return result;
    }
}
