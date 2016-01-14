package com.kenzan.msl.ratings.client.cassandra.query;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class AverageRatingsQueryTest {

    private TestConstants tc = TestConstants.getInstance();

    @Mock
    private QueryAccessor queryAccessor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MappingManager mappingManager;

    @Before
    public void init() {

        // TODO mock mappingManager.manager chained methods
        // mappingManager = Mockito.mock(MappingManager.class);
        // when(mappingManager.mapper(AverageRatingsDao.class).map(Mockito.anyObject()).one()).thenReturn(tc.AVERAGE_RATINGS_DAO);
    }

    @Test
    @Ignore
    public void testAdd() {
        AverageRatingsQuery.add(queryAccessor, mappingManager, tc.AVERAGE_RATINGS_DAO);
        verify(queryAccessor, atLeastOnce()).setAverageRating(tc.AVERAGE_RATINGS_DAO.getContentId(),
                                                              tc.AVERAGE_RATINGS_DAO.getContentType(),
                                                              tc.AVERAGE_RATINGS_DAO.getNumRating(),
                                                              tc.AVERAGE_RATINGS_DAO.getSumRating());
    }

    @Test
    @Ignore
    public void testGet() {
        AverageRatingsQuery.get(queryAccessor, mappingManager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        verify(queryAccessor, atLeastOnce()).getAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @Test(expected = RuntimeException.class)
    public void testGetThrowException() {
        AverageRatingsQuery.get(queryAccessor, mappingManager, null, null);
    }

    @Test
    @Ignore
    public void testDelete() {
        AverageRatingsQuery.delete(queryAccessor, mappingManager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
        verify(queryAccessor, atLeastOnce()).deleteAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteThrowsException() {
        AverageRatingsQuery.delete(queryAccessor, mappingManager, null, null);
    }
}
