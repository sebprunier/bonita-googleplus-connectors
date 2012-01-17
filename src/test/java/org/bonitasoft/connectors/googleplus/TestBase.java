package org.bonitasoft.connectors.googleplus;

import java.util.List;

import org.junit.Assert;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * Base class for unit tests.
 * 
 * @author sebastien.prunier
 */
public abstract class TestBase {

    /**
     * Returns the apiKey used for testing. It must be set with a system property : <code>-DapiKey=XXXXXX</code>.
     * 
     * @return the ApiKey used for testing.
     */
    protected final String getApiKey() {
        return System.getProperty("apiKey");
    }

    /**
     * Verifies that the given list of {@link ConnectorError} is empty.
     * 
     * @param errors
     *            list of {@link ConnectorError}
     */
    protected final void checkEmptyErrorList(List<ConnectorError> errors) {
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }

    /**
     * Verifies that the given field is the cause of the given error.
     * 
     * @param error
     *            the error
     * @param fieldName
     *            the field name
     */
    protected final void checkErrorOnField(ConnectorError error, String fieldName) {
        Assert.assertEquals(fieldName, error.getField());
        Assert.assertEquals(IllegalArgumentException.class, error.getError().getClass());
    }

}
