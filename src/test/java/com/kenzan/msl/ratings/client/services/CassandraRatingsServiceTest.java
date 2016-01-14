package com.kenzan.msl.ratings.client.services;

import static org.junit.Assert.*;

import com.google.common.base.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.dao.AverageRatingsDao;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.cassandra.query.AverageRatingsQuery;
import com.kenzan.msl.ratings.client.cassandra.query.UserRatingsQuery;

import rx.Observable;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CassandraRatingsServiceTest {
    private TestConstants tc = TestConstants.getInstance();

    @Mock
    private QueryAccessor queryAccessor;
    @Mock
    private MappingManager mappingManager;

    @InjectMocks
    private CassandraRatingsService service;

    @Test
    public void testGetInstance() {
        assertNotNull(service);
    }

    @PrepareForTest(AverageRatingsQuery.class)
    @Test
    public void testAddOrUpdateAverageRating() {
        PowerMockito.mockStatic(AverageRatingsQuery.class);
        AverageRatingsDao dao = tc.AVERAGE_RATINGS_DAO;

        Observable<Void> returnedValue = service.addOrUpdateAverageRating(dao);
        assertEquals(Observable.empty().getClass(), returnedValue.getClass());

        PowerMockito.verifyStatic();
        AverageRatingsQuery.add(queryAccessor, mappingManager, dao);
    }

    @PrepareForTest(AverageRatingsQuery.class)
    @Test
    public void testGetAverageRating() {
        PowerMockito.mockStatic(AverageRatingsQuery.class);
        service.getAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);

        PowerMockito.verifyStatic();
        AverageRatingsQuery.get(queryAccessor, mappingManager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @PrepareForTest(AverageRatingsQuery.class)
    @Test
    public void testDeleteAverageRating() {
        PowerMockito.mockStatic(AverageRatingsQuery.class);
        service.deleteAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);

        PowerMockito.verifyStatic();
        AverageRatingsQuery.delete(queryAccessor, mappingManager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @PrepareForTest(UserRatingsQuery.class)
    @Test
    public void testAddOrUpdateUserRatings() {
        PowerMockito.mockStatic(UserRatingsQuery.class);
        service.addOrUpdateUserRatings(tc.USER_RATINGS_DAO);

        PowerMockito.verifyStatic();
        UserRatingsQuery.add(queryAccessor, mappingManager, tc.USER_RATINGS_DAO);
    }

    @PrepareForTest(UserRatingsQuery.class)
    @Test
    public void testGetUserRating() {
        PowerMockito.mockStatic(UserRatingsQuery.class);
        service.getUserRating(tc.USER_ID, tc.ALBUM_CONTENT_TYPE, tc.ALBUM_ID);

        PowerMockito.verifyStatic();
        UserRatingsQuery.getRating(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @PrepareForTest(UserRatingsQuery.class)
    @Test
    public void testGetUserRatings() {
        PowerMockito.mockStatic(UserRatingsQuery.class);
        service.getUserRatings(tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE), Optional.of(tc.LIMIT));

        PowerMockito.verifyStatic();
        UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE),
                                    Optional.of(tc.LIMIT));
    }

    @PrepareForTest(UserRatingsQuery.class)
    @Test
    public void testDeleteUserRatings() {
        PowerMockito.mockStatic(UserRatingsQuery.class);
        service.deleteUserRatings(tc.USER_ID, tc.ALBUM_CONTENT_TYPE, tc.ALBUM_ID);

        PowerMockito.verifyStatic();
        UserRatingsQuery.remove(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

}
