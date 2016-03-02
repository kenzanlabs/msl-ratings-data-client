package com.kenzan.msl.ratings.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.TestConstants;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MappingManager.class})
public class AverageRatingsQueryTest {

  @Mock
  private QueryAccessor queryAccessor;

  private TestConstants tc = TestConstants.getInstance();

  private MappingManager manager;
  private Mapper<AverageRatingsDto> mapper;
  private Result<AverageRatingsDto> result;
  private ResultSet resultSet;

  @Before
  public void init() throws Exception {
    MockitoAnnotations.initMocks(this);

    resultSet = EasyMock.createMock(ResultSet.class);
    manager = PowerMockito.mock(MappingManager.class);
    mapper = PowerMockito.mock(Mapper.class);
    result = PowerMockito.mock(Result.class);
  }

  @Test
  public void testAdd() {
    mockGetMethod();
    AverageRatingsQuery.add(queryAccessor, manager, tc.AVERAGE_RATINGS_DTO);
    verify(queryAccessor, atLeastOnce()).setAverageRating(tc.AVERAGE_RATINGS_DTO.getContentId(),
        tc.AVERAGE_RATINGS_DTO.getContentType(), tc.AVERAGE_RATINGS_DTO.getNumRating(),
        tc.AVERAGE_RATINGS_DTO.getSumRating());
  }

  @Test(expected = RuntimeException.class)
  public void testUnableToAdd() {
    mockNullGetMethod();
    AverageRatingsQuery.add(queryAccessor, manager, tc.AVERAGE_RATINGS_DTO);
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidContentTypeExceptionAdd() {
    AverageRatingsQuery.get(queryAccessor, manager, tc.ALBUM_ID, "INVALID_CONTENT_TYPE");
  }

  @Test(expected = RuntimeException.class)
  public void testUnableToAddException() {
    mockNullGetMethod();
    AverageRatingsQuery.add(queryAccessor, manager, tc.AVERAGE_RATINGS_DTO);
  }

  @Test
  public void testGet() {
    mockGetMethod();
    Optional<AverageRatingsDto> results =
        AverageRatingsQuery.get(queryAccessor, manager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    assertTrue(results.isPresent());
    assertEquals(results.get(), tc.AVERAGE_RATINGS_DTO);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInvalidContentType() {
    AverageRatingsQuery.get(queryAccessor, manager, tc.ALBUM_ID, "INVALID_CONTENT_TYPE");
  }

  @Test
  public void testDelete() {
    mockNullGetMethod();
    AverageRatingsQuery.delete(queryAccessor, manager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    verify(queryAccessor, atLeastOnce()).deleteAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
  }

  @Test(expected = RuntimeException.class)
  public void testUnableToDelete() {
    mockGetMethod();
    AverageRatingsQuery.delete(queryAccessor, manager, tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
    verify(queryAccessor, atLeastOnce()).deleteAverageRating(tc.ALBUM_ID, tc.ALBUM_CONTENT_TYPE);
  }

  @Test(expected = RuntimeException.class)
  public void testDeleteInvalidContentException() {
    AverageRatingsQuery.delete(queryAccessor, manager, null, null);
  }

  /* ******************************************************* */

  private void mockGetMethod() {
    Mockito.when(queryAccessor.getAverageRating(Mockito.anyObject(), Mockito.anyObject()))
        .thenReturn(resultSet);
    PowerMockito.when(manager.mapper(AverageRatingsDto.class)).thenReturn(mapper);
    PowerMockito.when(mapper.map(resultSet)).thenReturn(result);
    PowerMockito.when(result.one()).thenReturn(tc.AVERAGE_RATINGS_DTO);
  }

  private void mockNullGetMethod() {
    Mockito.when(queryAccessor.getAverageRating(Mockito.anyObject(), Mockito.anyObject()))
        .thenReturn(resultSet);
    PowerMockito.when(manager.mapper(AverageRatingsDto.class)).thenReturn(mapper);
    PowerMockito.when(mapper.map(resultSet)).thenReturn(result);
    PowerMockito.when(result.one()).thenReturn(null);
  }
}
