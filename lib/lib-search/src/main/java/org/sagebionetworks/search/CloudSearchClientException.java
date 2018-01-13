package org.sagebionetworks.search;

public class CloudSearchClientException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5582738234605784919L;
	private int statusCode;

	public CloudSearchClientException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
