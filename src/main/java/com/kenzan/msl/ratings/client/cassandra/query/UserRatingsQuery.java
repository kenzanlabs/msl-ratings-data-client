/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.cassandra.query;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.dto.UserRatingsDto;

import java.util.UUID;

public class UserRatingsQuery extends RatingsHelper {

    /**
     * Adds a user rating to the user_ratings
     *
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param userRatingsDto com.kenzan.msl.ratings.client.dto.UserRatingsDto
     */
    public static void add(final QueryAccessor queryAccessor, final MappingManager manager,
                           final UserRatingsDto userRatingsDto) {
        String contentType = userRatingsDto.getContentType();
        if ( isValidContentType(contentType) ) {
            queryAccessor.setUserRating(userRatingsDto.getUserId(), userRatingsDto.getContentUuid(), contentType,
                                        userRatingsDto.getRating());
            // Verifies that user rating was in fact added
            if ( !getRating(queryAccessor, manager, userRatingsDto.getUserId(), userRatingsDto.getContentUuid(),
                            contentType).isPresent() ) {
                throw new RuntimeException(String.format("Unable to add to user_ratings, contentId: %s, userId: %s",
                                                         userRatingsDto.getContentUuid(), userRatingsDto.getUserId()));
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
     * @return com.kenzan.msl.ratings.client.dto.UserRatingsDto
     */
    public static Optional<UserRatingsDto> getRating(final QueryAccessor queryAccessor, final MappingManager manager,
                                                     final UUID userId, final UUID contentId, final String contentType) {
        if ( isValidContentType(contentType) ) {
            ResultSet userRatingResult = queryAccessor.getUserRating(userId, contentId, contentType);

            if ( userRatingResult == null ) {
                return Optional.absent();
            }

            UserRatingsDto results = manager.mapper(UserRatingsDto.class).map(userRatingResult).one();

            if ( results == null ) {
                return Optional.absent();
            }
            else {
                return Optional.of(results);
            }
        }

        throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
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
            if ( getRating(queryAccessor, manager, userId, contentId, contentType).isPresent() ) {
                throw new RuntimeException(String.format("Unable to remove contentId: %s from user_ratings table",
                                                         contentId));
            }
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }
    }
}
