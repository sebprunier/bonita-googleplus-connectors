package org.bonitasoft.connectors.googleplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.Connector;
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
 * Base class for Google+ connectors.
 * 
 * @author sebastien.prunier
 * 
 * @param <T>
 *            the type of the connector's result.
 */
public abstract class GooglePlusConnector<T> extends Connector {

    // Constant value used for pagination : number of results per page.
    protected static final Long PAGINATION_SIZE = 10L;
    // Constant value used for search : max number of results.
    protected static final Long MAX_SEARCH_VALUES = Long.MAX_VALUE;

    // Basic attributes for all connectors. See : https://developers.google.com/+/api/
    private String apiKey; // key
    private String fields; // fields
    private String resultRepresentationType; // alt

    // Connector result.
    private T result;

    /**
     * Executes the connector.
     */
    @Override
    protected void executeConnector() throws Exception {
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
                plusRequest.setAlt(resultRepresentationType);
            }
        };

        Plus.Builder builder = Plus.builder(httpTransport, jsonFactory);
        builder.setJsonHttpRequestInitializer(jsonHttpRequestInitializer);

        Plus plus = builder.build();

        result = executeAction(plus);
    }

    /**
     * Executes the API Call, using the given Google+ client.<br>
     * This method must be implemented by subclasses (Concrete connectors).
     * 
     * @param plus
     *            the Google+ client.
     * @return the connector result.
     * @throws Exception
     *             if an error occurred.
     */
    protected abstract T executeAction(Plus plus) throws Exception;

    /**
     * Validate input values.
     */
    @Override
    protected List<ConnectorError> validateValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        // Check ApiKey
        if (apiKey == null) {
            errors.add(new ConnectorError("apiKey", new IllegalArgumentException("apiKey is not valid !")));
        }

        // Append errors thrown by concrete connectors.
        errors.addAll(validateActionValues());

        return errors;
    }

    /**
     * Validates the specific values of the connector.<br>
     * This method must be implemented by subclasses (Concrete connectors).
     * 
     * @return the validation errors of the specific values.
     */
    protected abstract List<ConnectorError> validateActionValues();

    /*
     * Getters and Setters.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setResultRepresentationType(String resultRepresentationType) {
        this.resultRepresentationType = resultRepresentationType;
    }

    public T getResult() {
        return result;
    }
}
