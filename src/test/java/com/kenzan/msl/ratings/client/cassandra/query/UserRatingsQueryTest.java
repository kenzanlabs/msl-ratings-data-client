package com.kenzan.msl.ratings.client.cassandra.query;

import com.google.common.base.Optional;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class UserRatingsQueryTest {
    private TestConstants tc = TestConstants.getInstance();

    @Mock
    private QueryAccessor queryAccessor = Mockito.mock(QueryAccessor.class);
    @Mock
    private MappingManager mappingManager;

    @Test
    public void testAdd() {
        // TODO: remove try/catch & mock method throwing exception
        try {
            UserRatingsQuery.add(queryAccessor, mappingManager, tc.USER_RATINGS_DAO);
        }
        catch ( Exception expected ) {
            verify(queryAccessor, atLeastOnce()).setUserRating(tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE,
                                                               tc.RATING);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAddThrowsException() {
        UserRatingsQuery.add(queryAccessor, mappingManager, null);
    }

    @Test
    public void testGetRating() {
        // TODO: remove try/catch & mock method throwing exception
        try {
            UserRatingsQuery.getRating(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        }
        catch ( Exception expected ) {
            verify(queryAccessor, atLeastOnce()).getUserRating(tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        }
    }

    @Test
    public void testGetRatings() {
        UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.absent(), Optional.absent());
        verify(queryAccessor, atLeastOnce()).getUserRatings(tc.USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testGetRatingThrowsException() {
        UserRatingsQuery.getRating(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, null);
    }

    @Test
    public void testGetRatingsWithTypeAndLimit() {
        UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE),
                                    Optional.of(tc.LIMIT));
        verify(queryAccessor, atLeastOnce())
            .getUserRatingsWithTypeAndLimit(tc.USER_ID, tc.ALBUM_CONTENT_TYPE, tc.LIMIT);
    }

    @Test
    public void testGetRatingsWithType() {
        UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.of(tc.ALBUM_CONTENT_TYPE), Optional.absent());
        verify(queryAccessor, atLeastOnce()).getUserRatingsWithType(tc.USER_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @Test
    public void testGetRatingsWithLimit() {
        UserRatingsQuery.getRatings(queryAccessor, tc.USER_ID, Optional.absent(), Optional.of(tc.LIMIT));
        verify(queryAccessor, atLeastOnce()).getUserRatingsWithLimit(tc.USER_ID, tc.LIMIT);
    }

    @Test
    public void testRemove() {
        // TODO: remove try/catch & mock method throwing exception
        try {
            UserRatingsQuery.remove(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        }
        catch ( Exception expected ) {
            verify(queryAccessor, atLeastOnce()).deleteUserRating(tc.USER_ID, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveThrowsException() {
        UserRatingsQuery.remove(queryAccessor, mappingManager, tc.USER_ID, tc.ALBUM_ID, null);
    }
}