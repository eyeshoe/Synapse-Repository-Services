package org.sagebionetworks.repo.web.service;

import java.io.IOException;

import org.sagebionetworks.reflection.model.PaginatedResults;
import org.sagebionetworks.repo.model.AccessApproval;
import org.sagebionetworks.repo.model.Count;
import org.sagebionetworks.repo.model.DatastoreException;
import org.sagebionetworks.repo.model.IdList;
import org.sagebionetworks.repo.model.InvalidModelException;
import org.sagebionetworks.repo.model.RestrictableObjectDescriptor;
import org.sagebionetworks.repo.model.UnauthorizedException;
import org.sagebionetworks.repo.model.dataaccess.AccessorGroupRequest;
import org.sagebionetworks.repo.model.dataaccess.AccessorGroupResponse;
import org.sagebionetworks.repo.web.NotFoundException;

public interface AccessApprovalService {

	public AccessApproval createAccessApproval(Long userId,
			AccessApproval accessApproval) throws DatastoreException,
			UnauthorizedException, NotFoundException, InvalidModelException,
			IOException;

	public AccessApproval getAccessApproval(
			Long userId, String approvalId)
			throws DatastoreException, UnauthorizedException,
			NotFoundException;

	public PaginatedResults<AccessApproval> getAccessApprovals(Long userId,
			RestrictableObjectDescriptor subjectId, Long limit, Long offset)
			throws DatastoreException, UnauthorizedException, NotFoundException;

	public void deleteAccessApproval(Long userId, String approvalId)
			throws DatastoreException, UnauthorizedException, NotFoundException;

	public void revokeAccessApprovals(Long userId, String accessRequirementId, String accessorId);

	public Count deleteAccessApprovals(Long userId, IdList toDelete);

	public AccessorGroupResponse listAccessorGroup(Long userId, AccessorGroupRequest request);

}