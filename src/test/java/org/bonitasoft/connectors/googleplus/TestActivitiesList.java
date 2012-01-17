package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Activity;

/**
 * Test class for ActivitiesList connector.
 * 
 * @author sebastien.prunier
 */
public class TestActivitiesList extends TestBase {

    @Test
    public void testExecuteAction() throws Exception {
        ActivitiesList connector = new ActivitiesList();
        connector.setApiKey(getApiKey());

        connector.setUserId("110487324451850883514");
        connector.setCollection("public");
        connector.setMaxResults(5L);
        connector.executeConnector();
        List<Activity> activities = connector.getResult();
        Assert.assertTrue(activities.size() == 5);
    }

    @Test
    public void testValidateActionValues() {
        ActivitiesList connector = new ActivitiesList();

        // Test with a negative maxValue, a valid collection and a valid userId
        connector.setMaxResults(-1L);
        connector.setCollection("public");
        connector.setUserId("101859091162395481354");
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "maxResults");

        // Test with a null maxResults, a valid collection and a valid userId
        connector.setMaxResults(null);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults, a valid collection and a valid userId
        connector.setMaxResults(20L);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults, a wrong collection and a valid userId
        connector.setCollection("xxx");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "collection");

        // Test with a valid maxResults, a valid collection and a wrong userId
        connector.setCollection("public");
        connector.setUserId("xxx");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "userId");
    }

}
