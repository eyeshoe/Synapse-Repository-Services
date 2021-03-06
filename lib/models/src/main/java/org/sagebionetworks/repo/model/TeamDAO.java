package org.sagebionetworks.repo.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.repo.web.NotFoundException;

public interface TeamDAO {
	/**
	 * @param dto object to be created
	 * @return the newly created object
	 * @throws DatastoreException
	 * @throws InvalidModelException
	 */
	public Team create(Team dto) throws DatastoreException, InvalidModelException;

	/**
	 * Retrieves the object given its id
	 * 
	 * @param id
	 * @return
	 * @throws DatastoreException
	 * @throws NotFoundException
	 */
	public Team get(String id) throws DatastoreException, NotFoundException;
	
	/**
	 * 
	 * @param ids
	 * @return
	 * @throws DatastoreException
	 * @throws NotFoundException
	 */
	public ListWrapper<Team> list(List<Long> ids) throws DatastoreException, NotFoundException;
	
	/**
	 * Get the Teams in the system
	 * 
	 * @param offset
	 * @param limit
	 * 
	 */
	public List<Team> getInRange(long limit, long offset) throws DatastoreException;
	
	/**
	 * 
	 * @return the number of teams in the system
	 * @throws DatastoreException
	 */
	public long getCount() throws DatastoreException;
	
	/**
	 * 
	 * @param teamId
	 * @param principalId
	 * @return
	 * @throws NotFoundException
	 * @throws DatastoreException
	 */
	public TeamMember getMember(String teamId, String principalId) throws NotFoundException, DatastoreException;
	
	/**
	 * 
	 * @param teamIds
	 * @param principalIds
	 * @return
	 * @throws NotFoundException
	 * @throws DatastoreException
	 */
	public ListWrapper<TeamMember> listMembers(List<Long> teamIds, List<Long> principalIds) throws NotFoundException, DatastoreException;
	/**
	 * 
	 * @param teamId
	 * @param limit
	 * @param offset
	 * @return
	 * @throws DatastoreException
	 */
	public List<TeamMember> getMembersInRange(String teamId, long limit, long offset) throws DatastoreException;

	/**
	 * 
	 * @param teamId
	 * @return
	 * @throws DatastoreException
	 */
	public long getMembersCount(String teamId) throws DatastoreException;
	
	/**
	 * Get the ids of all the admins in the team
	 * @param teamId
	 * @return
	 * @throws NotFoundException
	 */
	public List<String> getAdminTeamMembers(String teamId) throws NotFoundException;

	/**
	 * This is used to build up the team and member prefix caches
	 * @return
	 * @throws DatastoreException
	 */
	public Map<Team, Collection<TeamMember>> getAllTeamsAndMembers() throws DatastoreException;
	
	/**
	 * Get the Teams a member belongs to
	 * @param principalId the team member
	 * @param offset
	 * @param limit
	 * @return the Teams this principal belongs to
	 * @throws DatastoreException 
	 */
	public List<Team> getForMemberInRange(String principalId, long limit, long offset) throws DatastoreException;

	/**
	 * Get the IDs of the Teams a member belongs to
	 * @param teamMemberId
	 * @param limit
	 * @param offset
	 * @param sortBy
	 * @param ascending
	 * @return
	 */
	List<String> getIdsForMember(String teamMemberId, long limit, long offset, TeamSortOrder sortBy, Boolean ascending);

	/**
	 * 
	 * @param principalId
	 * @return the number of teams the given member belongs to
	 * @throws DatastoreException
	 */
	public long getCountForMember(String principalId) throws DatastoreException;
	
	/**
	 * Updates the 'shallow' properties of an object.
	 * Note:  leaving createdBy and createdOn null in the dto tells the DAO to use the currently stored values
	 *
	 * @param team
	 * @throws DatastoreException
	 */
	public Team update(Team team) throws InvalidModelException,
			NotFoundException, ConflictingUpdateException, DatastoreException;

	/**
	 * delete the object given by the given ID
	 * 
	 * @param id
	 *            the id of the object to be deleted
	 * @throws DatastoreException
	 * @throws NotFoundException
	 */
	public void delete(String id) throws DatastoreException, NotFoundException;

	/**
	 * returns the number of admin members in a Team
	 * @param teamId
	 * @return
	 * @throws DatastoreException
	 */
	long getAdminMemberCount(String teamId) throws DatastoreException;

	/**
	 * retrieve all teams that a user is an admin
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> getAllTeamsUserIsAdmin(String userId);
}
