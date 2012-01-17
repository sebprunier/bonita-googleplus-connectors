package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

/**
 * Test class for PeopleListByActivity connector.
 * 
 * @author sebastien.prunier
 */
public class TestPeopleListByActivity extends TestBase {

    @Test
    public void testExecuteAction() throws Exception {
        Plus plus = getPlusClient(getApiKey(), null);

        PeopleListByActivity connector = new PeopleListByActivity();
        connector.setActivityId("z13awn1hmy2lzrv3n23hit5juybght31j");
        connector.setCollection("plusoners");
        List<Person> people = connector.executeAction(plus);
        Assert.assertTrue(people.size() >= 1);

        connector.setCollection("resharers");
        people = connector.executeAction(plus);
        Assert.assertTrue(people.size() >= 1);
    }

    @Test
    public void testValidateActionValues() {
        PeopleListByActivity connector = new PeopleListByActivity();

        // Test with a negative maxValue and a valid collection
        connector.setMaxResults(-1L);
        connector.setCollection("plusoners");
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "maxResults");

        // Test with a null maxResults value and a valid collection
        connector.setMaxResults(null);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults value and a valid collection
        connector.setMaxResults(20L);
        connector.setCollection("resharers");
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults and a wrong collection
        connector.setCollection("xxx");
        errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        error = errors.get(0);
        checkErrorOnField(error, "collection");
    }

}
