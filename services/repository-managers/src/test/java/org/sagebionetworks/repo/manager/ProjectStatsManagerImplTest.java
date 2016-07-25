package org.sagebionetworks.repo.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sagebionetworks.repo.model.NodeDAO;
import org.sagebionetworks.repo.model.ObjectType;
import org.sagebionetworks.repo.model.ProjectStat;
import org.sagebionetworks.repo.model.ProjectStatsDAO;
import org.sagebionetworks.repo.model.dao.WikiPageKeyHelper;
import org.sagebionetworks.repo.model.jdo.KeyFactory;
import org.sagebionetworks.repo.model.v2.dao.V2WikiPageDao;
import org.sagebionetworks.repo.web.NotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

public class ProjectStatsManagerImplTest {

	@Mock
	ProjectStatsDAO mockProjectStatDao;
	
	@Mock
	NodeDAO mockNodeDao;
	
	@Mock
	V2WikiPageDao mockV2wikiPageDao;
	
	ProjectStatsManagerImpl manager;
	
	Long projectId;
	String projectIdString;
	String entityId;
	String wikiId;
	
	@Before
	public void before(){
		MockitoAnnotations.initMocks(this);
		manager = new ProjectStatsManagerImpl();
		ReflectionTestUtils.setField(manager, "projectStatDao", mockProjectStatDao);
		ReflectionTestUtils.setField(manager, "nodeDao", mockNodeDao);
		ReflectionTestUtils.setField(manager, "v2wikiPageDao", mockV2wikiPageDao);
		
		projectId = 456L;
		projectIdString = KeyFactory.keyToString(projectId);
		entityId = "syn123";
		when(mockNodeDao.getProjectId(entityId)).thenReturn(projectIdString);
		
		wikiId = "999";
		when(mockV2wikiPageDao.lookupWikiKey(wikiId)).thenReturn(WikiPageKeyHelper.createWikiPageKey(entityId,ObjectType.ENTITY , wikiId));
	}
	
	@Test
	public void testGetProjectForObjectEntity(){
		ObjectType type = ObjectType.ENTITY;
		// call under test
		String projectIdLookup = manager.getProjectForObject(entityId, type);
		assertEquals(projectIdString, projectIdLookup);
	}
	
	@Test
	public void testGetProjectForObjectTable(){
		ObjectType type = ObjectType.TABLE;
		// call under test
		String projectIdLookup = manager.getProjectForObject(entityId, type);
		assertEquals(projectIdString, projectIdLookup);
	}
	
	@Test
	public void testGetProjectForObjectWiki(){
		ObjectType type = ObjectType.WIKI;
		// call under test
		String projectIdLookup = manager.getProjectForObject(wikiId, type);
		assertEquals(projectIdString, projectIdLookup);
	}
	
	@Test
	public void testGetProjectForObjectUnknown(){
		// a favorite does not have a project
		ObjectType type = ObjectType.FAVORITE;
		// call under test
		String projectIdLookup = manager.getProjectForObject(wikiId, type);
		assertEquals("Favorites do not have projects so null should be returned.",null, projectIdLookup);
	}
	
	@Test
	public void testGetProjectForObjectNotFound(){
		// setup a not found case
		when(mockNodeDao.getProjectId(entityId)).thenThrow(new NotFoundException("Does not exist"));
		ObjectType type = ObjectType.ENTITY;
		// call under test
		String projectIdLookup = manager.getProjectForObject(entityId, type);
		assertEquals("Null should be returned when the object cannot be found.",null, projectIdLookup);
	}
	
	@Test
	public void testUpdateProjectStats(){
		Long userId = 707L;
		ObjectType type = ObjectType.ENTITY;
		Date activityDate = new Date(1);
		// call under test
		manager.updateProjectStats(userId, entityId, type, activityDate);
		
		verify(mockProjectStatDao).update(new ProjectStat(projectId, userId, activityDate));
	}
	
	@Test
	public void testUpdateProjectStatsNotFound(){
		// setup a not found case
		when(mockNodeDao.getProjectId(entityId)).thenThrow(new NotFoundException("Does not exist"));
		Long userId = 707L;
		ObjectType type = ObjectType.ENTITY;
		Date activityDate = new Date(1);
		// call under test
		manager.updateProjectStats(userId, entityId, type, activityDate);
		
		verify(mockProjectStatDao, never()).update(any(ProjectStat.class));
	}
}