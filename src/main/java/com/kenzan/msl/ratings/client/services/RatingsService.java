/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.services;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.dao.AverageRatingsDao;
import com.kenzan.msl.ratings.client.dao.UserRatingsDao;
import rx.Observable;

import java.util.UUID;

public interface RatingsService {

    // ================================================================================================================
    // AVERAGE RATINGS
    // ================================================================================================================

    Observable<Void> addOrUpdateAverageRating(AverageRatingsDao averageRatingDao);

    Observable<AverageRatingsDao> getAverageRating(UUID contentId, String contentType);

    Observable<Void> deleteAverageRating(UUID contentId, String contentType);

    // ================================================================================================================
    // USER RATINGS
    // ================================================================================================================

    Observable<Void> addOrUpdateUserRatings(UserRatingsDao userRatingsDao);

    Observable<UserRatingsDao> getUserRating(UUID userUuid, String contentType, UUID contentUuid);

    Observable<ResultSet> getUserRatings(UUID userUuid, Optional<String> contentType, Optional<Integer> limit);

    Observable<Result<UserRatingsDao>> mapUserRatings(Observable<ResultSet> object);

    Observable<Void> deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid);
}
