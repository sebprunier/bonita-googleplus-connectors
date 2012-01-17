package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

/**
 * Bonita connector for service People:get.
 * 
 * @author sebastien.prunier
 */
public class PeopleGet extends GooglePlusConnector {

    // The ID of the person to get the profile for. The special value "me" can be used to indicate the authenticated user.
    private String userId;
    // Connector result
    private Person result;

    /**
     * Executes the connector.
     */
    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();
        result = plus.people().get(userId).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();
        if (userId == null || !userId.matches("me|[0-9]+")) {
            errors.add(new ConnectorError("userId", new IllegalArgumentException("userId is not valid ! It must match : me|[0-9]+")));
        }
        return errors;
    }

    /*
     * Getters and Setters.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    public Person getResult() {
        return result;
    }

}
