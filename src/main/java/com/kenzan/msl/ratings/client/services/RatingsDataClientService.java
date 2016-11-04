/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
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

public interface RatingsDataClientService {

  // ================================================================================================================
  // AVERAGE RATINGS
  // ================================================================================================================

  Observable<Void> addOrUpdateAverageRating(AverageRatingsDto averageRatingDto);

  Observable<Optional<AverageRatingsDto>> getAverageRating(UUID contentId, String contentType);

  Observable<Void> deleteAverageRating(UUID contentId, String contentType);

  // ================================================================================================================
  // USER RATINGS
  // ================================================================================================================

  Observable<Void> addOrUpdateUserRatings(UserRatingsDto userRatingsDto);

  Observable<Optional<UserRatingsDto>> getUserRating(UUID userUuid, String contentType,
      UUID contentUuid);

  Observable<ResultSet> getUserRatings(UUID userUuid, Optional<String> contentType,
      Optional<Integer> limit);

  Observable<Result<UserRatingsDto>> mapUserRatings(Observable<ResultSet> object);

  Observable<Void> deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid);

  // ================================================================================================================
  // MISC
  // ================================================================================================================

  QueryAccessor getQueryAccessor ();

  MappingManager getMappingManager ();
}
