package org.bonitasoft.connectors.googleplus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

public class PeopleGet extends ProcessConnector {

	// Google+ API URLs
	private static final String GOOGLEPLUS_API_URL = "https://www.googleapis.com/plus/v1/people/";
	private static final String GOOGLEPLUS_API_KEY_PARAM = "?key=";
	private static final String GOOGLEPLUS_API_FIELDS_PARAM = "&fields=";

	// JSON response body
	private java.lang.String jsonResponse;
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String userId;
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String apiKey;
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String fields;

	@Override
	protected void executeConnector() throws Exception {
		// Create the HTTP query
		String query = GOOGLEPLUS_API_URL + userId + GOOGLEPLUS_API_KEY_PARAM
				+ apiKey;
		if (fields != null && !"".equals(fields)) {
			query += GOOGLEPLUS_API_FIELDS_PARAM + fields;
		}

		// Execute GET http request
		URL url = new URL(query);
		URLConnection conn = url.openConnection();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		jsonResponse = sb.toString();
	}

	@Override
	protected List<ConnectorError> validateValues() {
		List<ConnectorError> errors = new ArrayList<ConnectorError>();
		if (userId == null || !userId.matches("me|[0-9]+")) {
			errors.add(new ConnectorError("userId",
					new IllegalArgumentException(
							"userId is not valid ! It must match : me|[0-9]+")));
		}
		if (apiKey == null) {
			errors.add(new ConnectorError("apiKey",
					new IllegalArgumentException("apiKey is not valid !")));
		}
		return errors;
	}

	/**
	 * Getter for output argument 'response' DO NOT REMOVE NOR RENAME THIS
	 * GETTER, unless you also change the related entry in the XML descriptor
	 * file
	 */
	public String getResponse() {
		return jsonResponse;
	}

	/**
	 * Setter for input argument 'userId' DO NOT REMOVE NOR RENAME THIS SETTER,
	 * unless you also change the related entry in the XML descriptor file
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	/**
	 * Setter for input argument 'apiKey' DO NOT REMOVE NOR RENAME THIS SETTER,
	 * unless you also change the related entry in the XML descriptor file
	 */
	public void setApiKey(java.lang.String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Setter for input argument 'fields' DO NOT REMOVE NOR RENAME THIS SETTER,
	 * unless you also change the related entry in the XML descriptor file
	 */
	public void setFields(java.lang.String fields) {
		this.fields = fields;
	}

}
