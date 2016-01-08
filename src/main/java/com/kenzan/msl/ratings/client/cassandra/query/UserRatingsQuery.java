/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.dao.UserRatingsDao;

import java.util.UUID;

public class UserRatingsQuery extends RatingsHelper {

    /**
     * Adds a user rating to the user_ratings
     *
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param userRatingsDao com.kenzan.msl.ratings.client.dao.UserRatingsDao
     */
    public static void add(final QueryAccessor queryAccessor, final MappingManager manager,
                           final UserRatingsDao userRatingsDao) {
        String contentType = userRatingsDao.getContentType();
        if ( isValidContentType(contentType) ) {
            queryAccessor.setUserRating(userRatingsDao.getUserId(), userRatingsDao.getContentUuid(), contentType,
                                        userRatingsDao.getRating());
            // Verifies that user rating was in fact added
            if ( getRating(queryAccessor, manager, userRatingsDao.getUserId(), userRatingsDao.getContentUuid(),
                           contentType) == null ) {
                throw new RuntimeException(String.format("Unable to add to user_ratings, contentId: %s, userId: %s",
                                                         userRatingsDao.getContentUuid(), userRatingsDao.getUserId()));
            }
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }
    }

    /**
     * Retrieves a specific user rating from the user_ratings table by its contentId and userId
     *
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param userId java.util.UUID
     * @param contentId java.util.UUID
     * @param contentType String
     * @return com.kenzan.msl.ratings.client.dao.UserRatingsDao
     */
    public static UserRatingsDao getRating(final QueryAccessor queryAccessor, final MappingManager manager,
                                           final UUID userId, final UUID contentId, final String contentType) {
        if ( isValidContentType(contentType) ) {
            return manager.mapper(UserRatingsDao.class)
                .map(queryAccessor.getUserRating(userId, contentId, contentType)).one();
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }
    }

    /**
     * Retrieves a list of user ratings to the user_ratings table
     *
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param userId java.util.UUID
     * @param contentType com.google.common.base.Optional > String
     * @param limit com.google.common.base.Optional > Integer
     * @return com.datastax.driver.core.ResultSet
     */
    public static ResultSet getRatings(final QueryAccessor queryAccessor, final UUID userId,
                                       final Optional<String> contentType, final Optional<Integer> limit) {

        if ( contentType.isPresent() && limit.isPresent() && isValidContentType(contentType.get()) ) {
            return queryAccessor.getUserRatingsWithTypeAndLimit(userId, contentType.get(), limit.get());
        }
        else if ( contentType.isPresent() && isValidContentType(contentType.get()) ) {
            return queryAccessor.getUserRatingsWithType(userId, contentType.get());
        }
        else if ( limit.isPresent() ) {
            return queryAccessor.getUserRatingsWithLimit(userId, limit.get());
        }
        else {
            return queryAccessor.getUserRatings(userId);
        }
    }

    /**
     * Removes a user rating from the user_ratings table
     *
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param userId java.util.UUID
     * @param contentId java.util.UUID
     * @param contentType String
     */
    public static void remove(final QueryAccessor queryAccessor, final MappingManager manager, final UUID userId,
                              final UUID contentId, final String contentType) {

        if ( isValidContentType(contentType) ) {
            queryAccessor.deleteUserRating(userId, contentId, contentType);
            // Verifies that user rating was in fact removed
            if ( getRating(queryAccessor, manager, userId, contentId, contentType) != null ) {
                throw new RuntimeException(String.format("Unable to remove contentId: %s from user_ratings table",
                                                         contentId));
            }
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }
    }
}
