package org.bonitasoft.connectors.googleplus;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;

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
     * Returns a GooglePlus client, build with the given parameters.
     * 
     * @param apiKey
     *            the apiKey
     * @param fields
     *            the fields (for a partial response)
     * @return the client
     */
    protected final Plus getPlusClient(final String apiKey, final String fields) {
        JsonFactory jsonFactory = new GsonFactory();
        HttpTransport httpTransport = new NetHttpTransport();

        JsonHttpRequestInitializer jsonHttpRequestInitializer = new JsonHttpRequestInitializer() {
            @Override
            public void initialize(JsonHttpRequest request) throws IOException {
                PlusRequest plusRequest = (PlusRequest) request;
                plusRequest.setKey(apiKey);
                if (fields != null && !"".equals(fields)) {
                    plusRequest.setFields(fields);
                }
            }
        };

        Plus.Builder builder = Plus.builder(httpTransport, jsonFactory);
        builder.setJsonHttpRequestInitializer(jsonHttpRequestInitializer);

        return builder.build();
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
