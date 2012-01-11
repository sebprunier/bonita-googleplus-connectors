package org.bonitasoft.connectors.googleplus;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

/**
 * Test class for the base class GooglePlusConenctor.
 * 
 * @author sebastien.prunier
 */
public class TestGooglePlusConnector extends TestBase {

    @Test
    public void testExecuteConnector() throws Exception {
        MockConnector connector = new MockConnector();
        connector.setApiKey(getApiKey());
        connector.executeConnector();
        Assert.assertNotNull(connector.getResult());
    }

    @Test(expected = GoogleJsonResponseException.class)
    public void testExecuteConnectorWithoutApiKey() throws Exception {
        MockConnector connector = new MockConnector();
        connector.setApiKey(null);
        connector.executeConnector();
    }

    @Test(expected = GoogleJsonResponseException.class)
    public void testExecuteConnectorWithBadApiKey() throws Exception {
        MockConnector connector = new MockConnector();
        connector.setApiKey("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        connector.executeConnector();
    }

    /*
     * Mock connector only used for testing.
     */
    private static final class MockConnector extends GooglePlusConnector<Person> {

        @Override
        protected Person executeAction(Plus plus) throws Exception {
            return plus.people().get("101859091162395481354").execute();
        }

        @Override
        protected List<ConnectorError> validateActionValues() {
            return null;
        }

    }

}
