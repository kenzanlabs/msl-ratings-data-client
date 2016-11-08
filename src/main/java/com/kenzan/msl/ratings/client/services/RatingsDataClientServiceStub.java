package com.kenzan.msl.ratings.client.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;
import com.kenzan.msl.ratings.client.dto.UserRatingsDto;
import rx.Observable;

import java.util.UUID;

/**
 * @author Kenzan
 */
public class RatingsDataClientServiceStub implements RatingsDataClientService {

  public QueryAccessor getQueryAccessor () {
    return null;
  }

  public MappingManager getMappingManager () {
    return null;
  }

  // ================================================================================================================
  // AVERAGE RATINGS
  // ================================================================================================================

  public Observable<Void> addOrUpdateAverageRating(AverageRatingsDto averageRatingDto) { return Observable.empty(); }

  public Observable<Optional<AverageRatingsDto>> getAverageRating(UUID contentId, String contentType) { return Observable.empty(); }

  public Observable<Void> deleteAverageRating(UUID contentId, String contentType) { return Observable.empty(); }

  // ================================================================================================================
  // USER RATINGS
  // ================================================================================================================

  public Observable<Void> addOrUpdateUserRatings(UserRatingsDto userRatingsDto) { return Observable.empty(); }

  public Observable<Optional<UserRatingsDto>> getUserRating(UUID userUuid, String contentType,
                                                     UUID contentUuid) { return Observable.empty(); }

  public Observable<ResultSet> getUserRatings(UUID userUuid, Optional<String> contentType,
                                       Optional<Integer> limit) { return Observable.empty(); }

  public Observable<Result<UserRatingsDto>> mapUserRatings(Observable<ResultSet> object) { return Observable.empty(); }

  public Observable<Void> deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid) { return Observable.empty(); }
}
