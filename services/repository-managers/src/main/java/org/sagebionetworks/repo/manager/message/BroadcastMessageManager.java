package org.sagebionetworks.repo.manager.message;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.sagebionetworks.common.util.progress.ProgressCallback;
import org.sagebionetworks.repo.model.UserInfo;
import org.sagebionetworks.repo.model.message.ChangeMessage;
import org.sagebionetworks.utils.HttpClientHelperException;

/**
 * The Broadcast manager is responsible for sending an email to users subscribed to certain events.
 * This manager is generic and used to send message to for any type of change message.
 *
 */
public interface BroadcastMessageManager {

	/**
	 * Main entry point to broadcast a given change message.
	 * Only Synapse Admin can use this method.
	 * 
	 * @param user - admin user
	 * @param changeMessage
	 * @throws HttpClientHelperException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ClientProtocolException 
	 */
	public void broadcastMessage(UserInfo user,
			ProgressCallback<Void> progressCallback,
			ChangeMessage changeMessage) throws ClientProtocolException, JSONException, IOException, HttpClientHelperException;

}