package org.sagebionetworks.projectstats.worker;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sagebionetworks.asynchronous.workers.sqs.MessageUtils;
import org.sagebionetworks.asynchronous.workers.sqs.Worker;
import org.sagebionetworks.asynchronous.workers.sqs.WorkerProgress;
import org.sagebionetworks.repo.model.DatastoreException;
import org.sagebionetworks.repo.model.EntityHeader;
import org.sagebionetworks.repo.model.EntityType;
import org.sagebionetworks.repo.model.NodeDAO;
import org.sagebionetworks.repo.model.ObjectType;
import org.sagebionetworks.repo.model.Project;
import org.sagebionetworks.repo.model.ProjectStat;
import org.sagebionetworks.repo.model.ProjectStatsDAO;
import org.sagebionetworks.repo.model.jdo.KeyFactory;
import org.sagebionetworks.repo.model.message.ChangeMessage;
import org.sagebionetworks.repo.web.NotFoundException;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.util.ValidateArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessException;

import com.amazonaws.services.sqs.model.Message;

/**
 * This worker will stream the results of a table SQL query to a local CSV file and upload the file to S3 as a
 * FileHandle.
 * 
 */
public class ProjectStatsWorker implements Worker {

	static private Logger log = LogManager.getLogger(ProjectStatsWorker.class);
	private List<Message> messages;
	private WorkerProgress workerProgress;
	private EntityType projectEntityType = EntityType.getNodeTypeForClass(Project.class);

	@Autowired
	private ProjectStatsDAO projectStatsDao;

	@Autowired
	NodeDAO nodeDao;

	@Override
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public void setWorkerProgress(WorkerProgress workerProgress) {
		this.workerProgress = workerProgress;
	}

	@Override
	public List<Message> call() throws Exception {
		List<Message> toDelete = new LinkedList<Message>();
		for (Message message : messages) {
			try {
				Message returned = processMessage(message);
				if (returned != null) {
					toDelete.add(returned);
				}
			} catch (Throwable e) {
				// Treat unknown errors as unrecoverable and return them
				toDelete.add(message);
				log.error("Worker Failed", e);
			}
		}
		return toDelete;
	}

	private Message processMessage(Message message) throws Throwable {
		try {
			ChangeMessage changeMessage = extractStatus(message);

			Long projectId = null;
			if (changeMessage.getObjectType() == ObjectType.ENTITY) {
				projectId = getProjectIdFromEntityId(changeMessage.getObjectId());
			} else {
				throw new IllegalArgumentException("cannot handle tyep " + changeMessage.getObjectType());
			}

			if (projectId != null) {
				ProjectStat projectStat = new ProjectStat(projectId, changeMessage.getModificationInfo().getUserId(),
						changeMessage.getTimestamp());
				projectStatsDao.update(projectStat);
			}
			return message;
		} catch (TransientDataAccessException e) {
			return null;
		}
	}

	private Long getProjectIdFromEntityId(String entityId) throws NotFoundException {
		List<EntityHeader> nodePath = nodeDao.getEntityPath(entityId);
		// the root of the node path should be the project
		if (nodePath.isEmpty()) {
			throw new DatastoreException("No path for entityId " + entityId + " could be found");
		}
		// walk the path from the top (the top is probably root and the next one should be project)
		for (EntityHeader node : nodePath) {
			if (node.getType().equals(projectEntityType.getEntityType())) {
				return KeyFactory.stringToKey(node.getId());
			}
		}
		throw new IllegalArgumentException("entityId " + entityId + " is not contained in a project");
	}

	/**
	 * Extract the AsynchUploadRequestBody from the message.
	 * 
	 * @param message
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	ChangeMessage extractStatus(Message message) throws JSONObjectAdapterException {
		ValidateArgument.required(message, "message");

		ChangeMessage changeMessage = MessageUtils.extractMessageBody(message, ChangeMessage.class);

		ValidateArgument.required(changeMessage.getModificationInfo(), "changeMessage.modificationInfo");
		ValidateArgument.required(changeMessage.getModificationInfo().getUserId(), "changeMessage.modificationInfo.userId");
		ValidateArgument.required(changeMessage.getTimestamp(), "changeMessage.timestamp");

		if (!BooleanUtils.isTrue(changeMessage.getIsModification())) {
			throw new IllegalArgumentException("ChangeMessage is not a modification: " + changeMessage);
		}

		return changeMessage;
	}
}