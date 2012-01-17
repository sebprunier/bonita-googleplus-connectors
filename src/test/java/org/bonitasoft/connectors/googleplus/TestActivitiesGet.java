package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Activity;

/**
 * Test class for ActivitiesGet connector.
 * 
 * @author sebastien.prunier
 */
public class TestActivitiesGet extends TestBase {

    @Test
    public void testExecuteConnector() throws Exception {
        ActivitiesGet connector = new ActivitiesGet();
        connector.setApiKey(getApiKey());

        connector.setActivityId("z13awn1hmy2lzrv3n23hit5juybght31j");
        connector.executeConnector();
        Activity act = connector.getResult();
        Assert.assertNotNull(act);
    }

    @Test
    public void testValidateActionValues() {
        ActivitiesGet connector = new ActivitiesGet();

        // Test with a null activityId
        connector.setActivityId(null);
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "activityId");

        // Test with an empty activityId
        connector.setActivityId("");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "activityId");

        // Test with a valid activityId
        connector.setActivityId("101859091162395481354");
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);
    }

}
