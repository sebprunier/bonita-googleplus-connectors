package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Person;

/**
 * Test class for PeopleGet connector.
 * 
 * @author sebastien.prunier
 */
public class TestPeopleGet extends TestBase {

    @Test
    public void testExecuteConnector() throws Exception {

        PeopleGet connector = new PeopleGet();
        connector.setApiKey(getApiKey());
        connector.setUserId("101859091162395481354");

        connector.executeConnector();
        Person me = connector.getResult();

        Assert.assertNotNull(me);
        Assert.assertEquals("Sébastien PRUNIER", me.getDisplayName());
    }

    @Test
    public void testValidateActionValues() {
        PeopleGet connector = new PeopleGet();

        // Test with a null userId
        connector.setUserId(null);
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "userId");

        // Test with an empty userId
        connector.setUserId("");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "userId");

        // Test with a userId that do not respect the pattern me|[0-9]+
        connector.setUserId("XXX");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "userId");

        // Test with 'me'
        connector.setUserId("me");
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with my Google+ userId :-)
        connector.setUserId("101859091162395481354");
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);
    }

}
