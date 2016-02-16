package com.kenzan.msl.ratings.client.services;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;

import com.kenzan.msl.ratings.client.dto.UserRatingsDto;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;
import com.kenzan.msl.ratings.client.cassandra.query.AverageRatingsQuery;
import com.kenzan.msl.ratings.client.cassandra.query.UserRatingsQuery;

import rx.Observable;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({
    CassandraRatingsService.class,
    AverageRatingsQuery.class,
    UserRatingsQuery.class,
    Session.class,
    Cluster.class,
    MappingManager.class })
public class CassandraRatingsServiceTest {

    private TestConstants tc = TestConstants.getInstance();
    private CassandraRatingsService cassandraRatingsService;
    private ResultSet resultSet;
    private MappingManager manager;

    @Before
    public void init()
        throws Exception {
        resultSet = createMock(ResultSet.class);

        Session session = PowerMock.createMock(Session.class);
        Cluster cluster = PowerMock.createMock(Cluster.class);
        Cluster.Builder builder = PowerMock.createMock(Cluster.Builder.class);

        PowerMock.mockStatic(Cluster.class);
        EasyMock.expect(Cluster.builder()).andReturn(builder);
        EasyMock.expect(builder.addContactPoint(EasyMock.anyString())).andReturn(builder);
        EasyMock.expect(builder.build()).andReturn(cluster);
        EasyMock.expect(cluster.connect(EasyMock.anyString())).andReturn(session);

        manager = PowerMockito.mock(MappingManager.class);
        PowerMockito.whenNew(MappingManager.class).withAnyArguments().thenReturn(manager);

        Mapper<UserRatingsDto> myUserRatingsMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(UserRatingsDto.class)).thenReturn(myUserRatingsMapper);
        PowerMockito.when(myUserRatingsMapper.map(resultSet)).thenReturn(null);

        Mapper<AverageRatingsDto> myAverageRatingsMapper = PowerMockito.mock(Mapper.class);
        PowerMockito.when(manager.mapper(AverageRatingsDto.class)).thenReturn(myAverageRatingsMapper);
        PowerMockito.when(myAverageRatingsMapper.map(resultSet)).thenReturn(null);

        PowerMockito.mockStatic(AverageRatingsQuery.class);
        PowerMockito.mockStatic(UserRatingsQuery.class);
    }

    @Test
    public void testAddOrUpdateAverageRating() {
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Void> results = cassandraRatingsService.addOrUpdateAverageRating(tc.AVERAGE_RATINGS_DTO);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    @Test
    public void testGetAverageRating() {
        PowerMockito.when(AverageRatingsQuery.get(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
                                                  Mockito.anyObject())).thenReturn(Optional.of(tc.AVERAGE_RATINGS_DTO));

        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Optional<AverageRatingsDto>> results = cassandraRatingsService
            .getAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        assertNotNull(results);
        assertEquals(tc.AVERAGE_RATINGS_DTO, results.toBlocking().first().get());
    }

    @Test
    public void testDeleteAverageRating() {
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Void> results = cassandraRatingsService.deleteAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    // ================================================================================================================
    // USER RATINGS
    // ================================================================================================================

    @Test
    public void testAddOrUpdateUserRatings() {
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Void> results = cassandraRatingsService.addOrUpdateUserRatings(tc.USER_RATINGS_DTO);
        assertTrue(results.isEmpty().toBlocking().first());
    }

    @Test
    public void testGetUserRating() {
        PowerMockito.when(UserRatingsQuery.getRating(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
                                                     Mockito.anyObject(), Mockito.anyObject()))
            .thenReturn(Optional.of(tc.USER_RATINGS_DTO));
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Optional<UserRatingsDto>> results = cassandraRatingsService.getUserRating(tc.USER_ID,
                                                                                             tc.ALBUM_CONTENT_TYPE,
                                                                                             tc.ALBUM_ID);
        assertNotNull(results);
        assertEquals(tc.USER_RATINGS_DTO, results.toBlocking().first().get());
    }

    @Test
    public void testGetUserRatings() {
        PowerMockito
            .when(UserRatingsQuery.getRatings(Mockito.anyObject(), eq(tc.USER_ID),
                                              eq(Optional.of(tc.ALBUM_CONTENT_TYPE)), eq(Optional.of(tc.LIMIT))))
            .thenReturn(resultSet);
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<ResultSet> results = cassandraRatingsService.getUserRatings(tc.USER_ID,
                                                                               Optional.of(tc.ALBUM_CONTENT_TYPE),
                                                                               Optional.of(tc.LIMIT));
        assertNotNull(results);
        assertEquals(resultSet, results.toBlocking().first());
    }

    @Test
    public void testMapUserRatings() {
        PowerMock.replayAll();
        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Result<UserRatingsDto>> results = cassandraRatingsService.mapUserRatings(Observable.just(resultSet));
        assertNull(results.toBlocking().first());
    }

    @Test
    public void testDeleteUserRatings() {
        PowerMock.replayAll();

        cassandraRatingsService = CassandraRatingsService.getInstance();
        Observable<Void> results = cassandraRatingsService.deleteUserRatings(tc.USER_ID, tc.ALBUM_CONTENT_TYPE,
                                                                             tc.ALBUM_ID);
        assertTrue(results.isEmpty().toBlocking().first());
    }

}
