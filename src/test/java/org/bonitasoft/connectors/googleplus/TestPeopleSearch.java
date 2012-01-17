package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.model.Person;

/**
 * Test class for PeopleSearch connector.
 * 
 * @author sebastien.prunier
 */
public class TestPeopleSearch extends TestBase {

    @Test
    public void testExecuteAction() throws Exception {
        PeopleSearch connector = new PeopleSearch();
        connector.setApiKey(getApiKey());
        
        connector.setQuery("Sébastien PRUNIER");
        connector.executeConnector();
        List<Person> people = connector.getResult();
        Assert.assertTrue(people.size() >= 1);
    }

    @Test
    public void testValidateActionValues() {
        PeopleSearch connector = new PeopleSearch();

        // Test with a negative maxValue
        connector.setMaxResults(-1L);
        List<ConnectorError> errors = connector.validateActionValues();
        Assert.assertEquals(1, errors.size());
        ConnectorError error = errors.get(0);
        checkErrorOnField(error, "maxResults");

        // Test with a null maxResults value
        connector.setMaxResults(null);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);

        // Test with a valid maxResults value
        connector.setMaxResults(20L);
        errors = connector.validateActionValues();
        checkEmptyErrorList(errors);
    }

}
