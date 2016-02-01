/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.cassandra.query;

import com.datastax.driver.mapping.MappingManager;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;

import java.util.UUID;

public class AverageRatingsQuery extends RatingsHelper {

    /**
     * Adds an average rating on the average_ratings table;
     * 
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param averageRatingsDto com.kenzan.msl.ratings.client.dto.AverageRatingsDto
     */
    public static void add(final QueryAccessor queryAccessor, final MappingManager manager,
                           final AverageRatingsDto averageRatingsDto) {

        String contentType = averageRatingsDto.getContentType();
        if ( isValidContentType(contentType) ) {
            queryAccessor.setAverageRating(averageRatingsDto.getContentId(), averageRatingsDto.getContentType(),
                                           averageRatingsDto.getNumRating(), averageRatingsDto.getSumRating());
            // Verifies that average rating was in fact added
            if ( get(queryAccessor, manager, averageRatingsDto.getContentId(), averageRatingsDto.getContentType()) == null ) {
                throw new RuntimeException(String.format("Unable to add to average_ratings, contentId: %s",
                                                         averageRatingsDto.getContentId()));
            }
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }
    }

    /**
     * Retrieves an average rating given a content UUID and a content type (Song, Album, Artist)
     * 
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param contentId java.util.UUID
     * @param contentType String
     * @return com.kenzan.msl.ratings.client.dto.AverageRatingsDto
     */
    public static AverageRatingsDto get(final QueryAccessor queryAccessor, final MappingManager manager,
                                        final UUID contentId, final String contentType) {

        if ( isValidContentType(contentType) ) {
            return manager.mapper(AverageRatingsDto.class).map(queryAccessor.getAverageRating(contentId, contentType))
                .one();
        }

        throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
    }

    /**
     * Deletes an average rating given a content UUID and a content type (Song, Album, Artist)
     * 
     * @param queryAccessor com.kenzan.msl.ratings.client.cassandra.QueryAccessor
     * @param manager com.datastax.driver.mapping.MappingManager
     * @param contentId java.util.UUID
     * @param contentType String
     */
    public static void delete(final QueryAccessor queryAccessor, final MappingManager manager, final UUID contentId,
                              final String contentType) {
        if ( isValidContentType(contentType) ) {
            queryAccessor.deleteAverageRating(contentId, contentType);
            // Verifies that average rating was in fact deleted
            if ( get(queryAccessor, manager, contentId, contentType) != null ) {
                throw new RuntimeException(String.format("Unable to remove from average_ratings, contentId: %s",
                                                         contentId));
            }
        }
        else {
            throw new RuntimeException(String.format("Invalid contentType: %s", contentType));
        }

    }
}
