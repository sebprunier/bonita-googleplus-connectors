package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Comment;

/**
 * Test class for PeopleListByActivity connector.
 * 
 * @author sebastien.prunier
 */
public class TestCommentsList extends TestBase {

    @Test
    public void testExecuteAction() throws Exception {
        CommentsList connector = new CommentsList();
        connector.setApiKey(getApiKey());

        connector.setActivityId("z12dyth5utqittaju04chpv4pzfsxlr4g5w");
        connector.executeConnector();
        List<Comment> comments = connector.getResult();
        Assert.assertTrue(comments.size() >= 1);
    }

    @Test
    public void testValidateActionValues() {
        CommentsList connector = new CommentsList();

        // Test with a negative maxValue and a valid activityId
        connector.setMaxResults(-1L);
        connector.setActivityId("z12dyth5utqittaju04chpv4pzfsxlr4g5w");
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "maxResults");

        // Test with a null maxResults value and a valid activityId
        connector.setMaxResults(null);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults value and a valid activityId
        connector.setMaxResults(20L);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults and a wrong activityId
        connector.setActivityId("");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "activityId");
    }

}
