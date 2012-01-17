package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Activity;

/**
 * Test class for ActivitiesSearch connector.
 * 
 * @author sebastien.prunier
 */
public class TestActivitiesSearch extends TestBase {

    @Test
    public void testExecuteAction() throws Exception {
        ActivitiesSearch connector = new ActivitiesSearch();
        connector.setApiKey(getApiKey());

        connector.setQuery("bonita open solution");
        connector.setOrderBy("recent");
        connector.setMaxResults(10L);
        connector.executeConnector();
        List<Activity> activities = connector.getResult();
        Assert.assertTrue(activities.size() >= 0);
    }

    @Test
    public void testValidateActionValues() {
        ActivitiesSearch connector = new ActivitiesSearch();

        // Test with a negative maxValue and a valid orderBy
        connector.setMaxResults(-1L);
        connector.setOrderBy("recent");
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "maxResults");

        // Test with a null maxResults and a valid orderBy
        connector.setMaxResults(null);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults and a valid orderBy
        connector.setMaxResults(20L);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults and a wrong orderBy
        connector.setOrderBy("xxx");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "orderBy");
    }

}
