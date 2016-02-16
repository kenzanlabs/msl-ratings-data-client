/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.services;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Optional;
import com.kenzan.msl.ratings.client.cassandra.QueryAccessor;
import com.kenzan.msl.ratings.client.cassandra.query.AverageRatingsQuery;
import com.kenzan.msl.ratings.client.cassandra.query.UserRatingsQuery;
import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;
import com.kenzan.msl.ratings.client.dto.UserRatingsDto;
import rx.Observable;

import java.util.UUID;

public class CassandraRatingsService
    implements RatingsService {

    private QueryAccessor queryAccessor;
    private MappingManager mappingManager;

    private static final String DEFAULT_CONTACT_POINT = "127.0.0.1";
    private static final String DEFAULT_MSL_KEYSPACE = "msl";

    private static CassandraRatingsService instance = null;

    private CassandraRatingsService() {
        String configUrl = "file://" + System.getProperty("user.dir");
        configUrl += "/../msl-ratings-data-client-config/archaius-config.properties";
        String additionalUrlsProperty = "archaius.configurationSource.additionalUrls";
        System.setProperty(additionalUrlsProperty, configUrl);

        DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();
        DynamicStringProperty contactPoint = propertyFactory.getStringProperty("contact_point", DEFAULT_CONTACT_POINT);
        Cluster cluster = Cluster.builder().addContactPoint(contactPoint.getValue()).build();

        DynamicStringProperty keyspace = propertyFactory.getStringProperty("keyspace", DEFAULT_MSL_KEYSPACE);
        Session session = cluster.connect(keyspace.getValue());

        mappingManager = new MappingManager(session);
        queryAccessor = mappingManager.createAccessor(QueryAccessor.class);
    }

    public static CassandraRatingsService getInstance() {
        if ( instance == null ) {
            instance = new CassandraRatingsService();
        }
        return instance;
    }

    // ================================================================================================================
    // AVERAGE RATINGS
    // ================================================================================================================

    /**
     * Adds or update an average rating to the average_ratings table
     *
     * @param averageRatingDto com.kenzan.msl.ratings.client.dto.AverageRatingsDto
     * @return Observable<Void>
     */
    public Observable<Void> addOrUpdateAverageRating(AverageRatingsDto averageRatingDto) {
        AverageRatingsQuery.add(queryAccessor, mappingManager, averageRatingDto);
        return Observable.empty();
    }

    /**
     * Retrieves an average rating from the average_ratings table
     *
     * @param contentId java.util.UUID
     * @param contentType String
     * @return Observable<AverageRatingsDto>
     */
    public Observable<Optional<AverageRatingsDto>> getAverageRating(UUID contentId, String contentType) {
        return Observable.just(AverageRatingsQuery.get(queryAccessor, mappingManager, contentId, contentType));
    }

    /**
     * Deletes an average rating from the average_ratings table
     *
     * @param contentId java.util.UUID
     * @param contentType String
     * @return Observable<Void>
     */
    public Observable<Void> deleteAverageRating(UUID contentId, String contentType) {
        AverageRatingsQuery.delete(queryAccessor, mappingManager, contentId, contentType);
        return Observable.empty();
    }

    // ================================================================================================================
    // USER RATINGS
    // ================================================================================================================

    /**
     * Adds a user rating to the user_ratings table
     *
     * @param userRatingsDto com.kenzan.msl.ratings.client.dto.UserRatingsDto
     * @return Observable<Void>
     */
    public Observable<Void> addOrUpdateUserRatings(UserRatingsDto userRatingsDto) {
        UserRatingsQuery.add(queryAccessor, mappingManager, userRatingsDto);
        return Observable.empty();
    }

    /**
     * Retrieves a specific user query from the user_ratings table
     *
     * @param userUuid java.util.UUID
     * @param contentType String
     * @param contentUuid java.util.UUID
     * @return Observable<UserRatingsDto>
     */
    public Observable<Optional<UserRatingsDto>> getUserRating(UUID userUuid, String contentType, UUID contentUuid) {
        return Observable.just(UserRatingsQuery.getRating(queryAccessor, mappingManager, userUuid, contentUuid,
                                                          contentType));
    }

    /**
     * Retrieve a set of user ratings from the user_ratings table
     *
     * @param userUuid java.util.UUID
     * @param contentType Optional<String>
     * @param limit Optional<Integer>
     * @return Observable<ResultSet>
     */
    public Observable<ResultSet> getUserRatings(UUID userUuid, Optional<String> contentType, Optional<Integer> limit) {
        return Observable.just(UserRatingsQuery.getRatings(queryAccessor, userUuid, contentType, limit));
    }

    /**
     * Maps a result set object into a userRatingsDto result set
     * 
     * @param object Observable<ResultSet>
     * @return Observable<Result<UserRatingsDto>>
     */
    public Observable<Result<UserRatingsDto>> mapUserRatings(Observable<ResultSet> object) {
        return Observable.just(mappingManager.mapper(UserRatingsDto.class).map(object.toBlocking().first()));
    }

    /**
     * Deletes a specific user rating from the user ratings table
     *
     * @param userUuid java.util.UUID
     * @param contentType String
     * @param contentUuid java.util.UUID
     * @return Observable<Void>
     */
    public Observable<Void> deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid) {
        UserRatingsQuery.remove(queryAccessor, mappingManager, userUuid, contentUuid, contentType);
        return Observable.empty();
    }
}
