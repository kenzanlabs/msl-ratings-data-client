package com.kenzan.msl.ratings.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;

import com.kenzan.msl.ratings.client.dao.UserRatingsDao;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MappingManager.class })
public class UserRatingsQueryTest {
    @Mock
    private QueryAccessor queryAccessor;

    private TestConstants tc = TestConstants.getInstance();

    private MappingManager manager;
    private Mapper<UserRatingsDao> mapper;
    private Result<UserRatingsDao> result;
    private ResultSet resultSet;

    @Before
    public void init()
        throws Exception {
        MockitoAnnotations.initMocks(this);

        resultSet = EasyMock.createMock(ResultSet.class);
        manager = PowerMockito.mock(MappingManager.class);
        mapper = PowerMockito.mock(Mapper.class);
        result = PowerMockito.mock(Result.class);
    }

    @Test
    public void testAdd() {
        mockGetMethod();
        tc.USER_RATINGS_DAO.setContentType("Album");
        UserRatingsQuery.add(queryAccessor, manager, tc.USER_RATINGS_DAO);
        verify(queryAccessor, atLeastOnce()).setUserRating(tc.USER_RATINGS_DAO.getUserId(),
                                                           tc.USER_RATINGS_DAO.getContentUuid(),
                                                           tc.USER_RATINGS_DAO.getContentType(),
                                                           tc.USER_RATINGS_DAO.getRating());
    }

    @Test(expected = RuntimeException.class)
    public void testUnableToAdd() {
        mockNullGetMethod();
        UserRatingsQuery.add(queryAccessor, manager, tc.USER_RATINGS_DAO);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidContentTypeExceptionAdd() {
        tc.USER_RATINGS_DAO.setContentType("INVALID_CONTENT_TYPE");
        UserRatingsQuery.add(queryAccessor, manager, tc.USER_RATINGS_DAO);
    }

    @Test(expected = RuntimeException.class)
    public void testUnableToAddException() {
        mockNullGetMethod();
        UserRatingsQuery.add(queryAccessor, manager, tc.USER_RATINGS_DAO);
    }

    @Test
    public void testGetRating() {
        mockGetMethod();
        UserRatingsDao results = UserRatingsQuery.getRating(queryAccessor, manager, tc.USER_ID, tc.ALBUM_ID,
                                                            tc.ALBUM_CONTENT_TYPE);
        assertNotNull(results);
        assertEquals(results, tc.USER_RATINGS_DAO);
    }

    @Test(expected = RuntimeException.class)
    public void testGetInvalidContentType() {
        UserRatingsQuery.getRating(queryAccessor, manager, tc.USER_ID, tc.ALBUM_ID, "INVALID_CONTENT_TYPE");
    }

    @Test
    public void testDelete() {
        mockNullGetMethod();
        UserRatingsQuery.remove(queryAccessor, manager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        verify(queryAccessor, atLeastOnce()).deleteUserRating(tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @Test(expected = RuntimeException.class)
    public void testUnableToDelete() {
        mockGetMethod();
        UserRatingsQuery.remove(queryAccessor, manager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        verify(queryAccessor, atLeastOnce()).deleteUserRating(tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteInvalidContentException() {
        UserRatingsQuery.remove(queryAccessor, manager, tc.USER_ID, tc.ALBUM_ID, "INVALID_CONTENT_TYPE");
    }

    @Test
    public void testGetRatingsWithLimitAndContentType() {
        Mockito.when(queryAccessor.getUserRatingsWithTypeAndLimit(tc.USER_ID, tc.ALBUM_CONTENT_TYPE, tc.LIMIT))
            .thenReturn(resultSet);
        ResultSet results = UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE),
                                                        Optional.of(tc.LIMIT));
        assertEquals(results, resultSet);
    }

    @Test
    public void testGetRatingsWithContentType() {
        Mockito.when(queryAccessor.getUserRatingsWithType(tc.USER_ID, tc.ALBUM_CONTENT_TYPE)).thenReturn(resultSet);
        ResultSet results = UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE),
                                                        Optional.absent());
        assertEquals(results, resultSet);
    }

    @Test
    public void testGetUserRatingsWithLimit() {
        Mockito.when(queryAccessor.getUserRatingsWithLimit(tc.USER_ID, tc.LIMIT)).thenReturn(resultSet);
        ResultSet results = UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.absent(),
                                                        Optional.of(tc.LIMIT));
        assertEquals(results, resultSet);
    }

    @Test
    public void testGetUserRatings() {
        Mockito.when(queryAccessor.getUserRatings(tc.USER_ID)).thenReturn(resultSet);
        ResultSet results = UserRatingsQuery
            .getRatings(queryAccessor, tc.USER_ID, Optional.absent(), Optional.absent());
        assertEquals(results, resultSet);
    }

    /* ******************************************************* */

    private void mockGetMethod() {
        Mockito.when(queryAccessor.getUserRating(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject()))
            .thenReturn(resultSet);
        PowerMockito.when(manager.mapper(UserRatingsDao.class)).thenReturn(mapper);
        PowerMockito.when(mapper.map(resultSet)).thenReturn(result);
        PowerMockito.when(result.one()).thenReturn(tc.USER_RATINGS_DAO);
    }

    private void mockNullGetMethod() {
        Mockito.when(queryAccessor.getUserRating(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject()))
            .thenReturn(resultSet);
        PowerMockito.when(manager.mapper(UserRatingsDao.class)).thenReturn(mapper);
        PowerMockito.when(mapper.map(resultSet)).thenReturn(result);
        PowerMockito.when(result.one()).thenReturn(null);
    }
}