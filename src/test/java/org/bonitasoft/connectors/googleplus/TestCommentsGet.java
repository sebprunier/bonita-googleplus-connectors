package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Comment;

/**
 * Test class for PeopleGet connector.
 * 
 * @author sebastien.prunier
 */
public class TestCommentsGet extends TestBase {

    @Test
    public void testExecuteConnector() throws Exception {
        CommentsGet connector = new CommentsGet();
        connector.setApiKey(getApiKey());

        connector.setCommentId("fde6Na2Dw9EIfVHeSE44ZlT6LBA-i3U8lkOScVsdOoV6PmYdOHrZQoLw6X052D7OJ4lKWEytX8s");
        connector.executeConnector();
        Comment comm = connector.getResult();

        Assert.assertNotNull(comm);
        Assert.assertEquals("Go Giants", comm.getPlusObject().getContent());
    }

    @Test
    public void testValidateActionValues() {
        CommentsGet connector = new CommentsGet();

        // Test with a null userId
        connector.setCommentId(null);
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "commentId");

        // Test with an empty userId
        connector.setCommentId("");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "commentId");

        // Test with a valid commentId
        connector.setCommentId("fde6Na2Dw9EIfVHeSE44ZlT6LBA-i3U8lkOScVsdOoV6PmYdOHrZQoLw6X052D7OJ4lKWEytX8s");
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);
    }

}
