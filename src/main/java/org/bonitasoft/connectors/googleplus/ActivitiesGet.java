package org.bonitasoft.connectors.googleplus;

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;

/**
 * Bonita connector for service Activities:get.
 * 
 * @author sebastien.prunier
 */
public class ActivitiesGet extends GooglePlusConnector {

    // The ID of the activity to get.
    private String activityId;
    // Connector result
    private Activity result;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeConnector() throws Exception {
        Plus plus = getPlusClient();
        result = plus.activities().get(activityId).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ConnectorError> validateActionValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();

        if (activityId == null || "".equals(activityId)) {
            errors.add(new ConnectorError("activityId", new IllegalArgumentException("activityId is not valid !")));
        }

        return errors;
    }

    /*
     * Getters and Setters
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Activity getResult() {
        return result;
    }
}
