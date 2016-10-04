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
import org.codehaus.plexus.util.StringUtils;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CassandraRatingsService implements RatingsService {

  private QueryAccessor queryAccessor;
  private MappingManager mappingManager;

  private static final String DEFAULT_MSL_KEYSPACE = "msl";
  private static final String DEFAULT_MSL_REGION = "us-west-2";
  private static final String DEFAULT_CLUSTER = "127.0.0.1";

  private static DynamicStringProperty domain;
  private static DynamicStringProperty keyspace;
  private static DynamicStringProperty region;

  private static CassandraRatingsService instance = null;

  private CassandraRatingsService() {
    Cluster.Builder builder = Cluster.builder();
    String domainValue = domain.getValue();
    if (StringUtils.isNotEmpty(domainValue)) {
      String[] clusterNodes = StringUtils.split(domainValue, ",");
      for (String node : clusterNodes) {
        builder.addContactPoint(node);
      }
    }

    Cluster cluster = builder.build();
    Session session = cluster.connect(keyspace.getValue());

    mappingManager = new MappingManager(session);
    queryAccessor = mappingManager.createAccessor(QueryAccessor.class);
  }

  public static CassandraRatingsService getInstance(Optional<HashMap<String, Optional<String>>> archaiusProperties) {
    if (instance == null) {
      initializeDynamicProperties(archaiusProperties);
      instance = new CassandraRatingsService();
    }
    return instance;
  }

  public static CassandraRatingsService getInstance() {
    return getInstance(Optional.absent());
  }

  private static void initializeDynamicProperties(Optional<HashMap<String, Optional<String>>> archaiusProperties) {
    DynamicPropertyFactory propertyFactory = DynamicPropertyFactory.getInstance();

    keyspace = propertyFactory.getStringProperty("keyspace", DEFAULT_MSL_KEYSPACE);
    region = propertyFactory.getStringProperty("region", DEFAULT_MSL_REGION);

    String regionValue = "", domainName = "";
    if (archaiusProperties.isPresent()) {
      for (Map.Entry<String, Optional<String>> entry : archaiusProperties.get().entrySet()) {
        if (entry.getValue().isPresent()) {
          switch (entry.getKey()) {
            case "region":
              regionValue = entry.getValue().get();
              break;
            case "domainName":
              domainName = entry.getValue().get();
              break;
          }
        }
      }
    }

    domain = propertyFactory.getStringProperty(StringUtils.isNotEmpty(domainName) ? domainName : "local", DEFAULT_CLUSTER);
  }

  // ================================================================================================================
  // AVERAGE RATINGS
  // ================================================================================================================

  /**
   * Adds or update an average rating to the average_ratings table
   *
   * @param averageRatingDto com.kenzan.msl.ratings.client.dto.AverageRatingsDto
   * @return Observable&lt;Void&gt;
   */
  public Observable<Void> addOrUpdateAverageRating(AverageRatingsDto averageRatingDto) {
    AverageRatingsQuery.add(queryAccessor, mappingManager, averageRatingDto);
    return Observable.empty();
  }

  /**
   * Retrieves an average rating from the average_ratings table
   *
   * @param contentId   java.util.UUID
   * @param contentType String
   * @return Observable&lt;AverageRatingsDto&gt;
   */
  public Observable<Optional<AverageRatingsDto>> getAverageRating(UUID contentId, String contentType) {
    return Observable.just(AverageRatingsQuery.get(queryAccessor, mappingManager, contentId,
      contentType));
  }

  /**
   * Deletes an average rating from the average_ratings table
   *
   * @param contentId   java.util.UUID
   * @param contentType String
   * @return Observable&lt;Void&gt;
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
   * @return Observable&lt;Void&gt;
   */
  public Observable<Void> addOrUpdateUserRatings(UserRatingsDto userRatingsDto) {
    UserRatingsQuery.add(queryAccessor, mappingManager, userRatingsDto);
    return Observable.empty();
  }

  /**
   * Retrieves a specific user query from the user_ratings table
   *
   * @param userUuid    java.util.UUID
   * @param contentType String
   * @param contentUuid java.util.UUID
   * @return Observable&lt;UserRatingsDto&gt;
   */
  public Observable<Optional<UserRatingsDto>> getUserRating(UUID userUuid, String contentType,
                                                            UUID contentUuid) {
    return Observable.just(UserRatingsQuery.getRating(queryAccessor, mappingManager, userUuid,
      contentUuid, contentType));
  }

  /**
   * Retrieve a set of user ratings from the user_ratings table
   *
   * @param userUuid    java.util.UUID
   * @param contentType Optional&lt;String&gt;
   * @param limit       Optional&lt;Integer&gt;
   * @return Observable&lt;ResultSet&gt;
   */
  public Observable<ResultSet> getUserRatings(UUID userUuid, Optional<String> contentType,
                                              Optional<Integer> limit) {
    return Observable
      .just(UserRatingsQuery.getRatings(queryAccessor, userUuid, contentType, limit));
  }

  /**
   * Maps a result set object into a userRatingsDto result set
   *
   * @param object Observable&lt;ResultSet&gt;
   * @return Observable&lt;Result&lt;UserRatingsDto&gt;&gt;
   */
  public Observable<Result<UserRatingsDto>> mapUserRatings(Observable<ResultSet> object) {
    return Observable.just(mappingManager.mapper(UserRatingsDto.class).map(
      object.toBlocking().first()));
  }

  /**
   * Deletes a specific user rating from the user ratings table
   *
   * @param userUuid    java.util.UUID
   * @param contentType String
   * @param contentUuid java.util.UUID
   * @return Observable&lt;Void&gt;
   */
  public Observable<Void> deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid) {
    UserRatingsQuery.remove(queryAccessor, mappingManager, userUuid, contentUuid, contentType);
    return Observable.empty();
  }
}
