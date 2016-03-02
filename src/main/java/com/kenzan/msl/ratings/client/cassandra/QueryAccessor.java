/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import java.util.UUID;

@Accessor
public interface QueryAccessor {

  // =================================================================================================================
  // AVERAGE RATINGS
  // =================================================================================================================

  @Query("UPDATE average_ratings SET num_rating = num_rating + :num_rating, sum_rating = sum_rating + :sum_rating WHERE content_id = :content_id AND content_type = :content_type")
  public void setAverageRating(@Param("content_id") UUID content_id,
      @Param("content_type") String content_type, @Param("num_rating") long num_rating,
      @Param("sum_rating") long sum_rating);

  @Query("SELECT * FROM average_ratings WHERE content_id = :content_id AND content_type = :content_type LIMIT 1")
  public ResultSet getAverageRating(@Param("content_id") UUID content_id,
      @Param("content_type") String content_type);

  @Query("DELETE FROM average_ratings WHERE content_id = :content_id AND content_type = :content_type")
  public void deleteAverageRating(@Param("content_id") UUID content_id,
      @Param("content_type") String content_type);

  // =================================================================================================================
  // USER RATINGS
  // =================================================================================================================

  @Query("INSERT INTO user_ratings (user_id, content_id, content_type, rating) VALUES (:user_id, :content_id, :content_type, :num_rating)")
  public void setUserRating(@Param("user_id") UUID user_id, @Param("content_id") UUID content_id,
      @Param("content_type") String content_type, @Param("num_rating") Integer num_rating);

  @Query("SELECT * FROM user_ratings WHERE user_id = :user_id AND content_id = :content_id AND content_type = :content_type LIMIT 1")
  public ResultSet getUserRating(@Param("user_id") UUID user_id,
      @Param("content_id") UUID content_id, @Param("content_type") String content_type);

  @Query("SELECT * FROM user_ratings WHERE user_id = :user_id AND content_type = :content_type LIMIT :max")
  public ResultSet getUserRatingsWithTypeAndLimit(@Param("user_id") UUID user_id,
      @Param("content_type") String content_type, @Param("max") int limit);

  @Query("SELECT * FROM user_ratings WHERE user_id = :user_id LIMIT :max")
  public ResultSet getUserRatingsWithLimit(@Param("user_id") UUID user_id, @Param("max") int limit);

  @Query("SELECT * FROM user_ratings WHERE user_id = :user_id AND content_type = :content_type")
  public ResultSet getUserRatingsWithType(@Param("user_id") UUID user_id,
      @Param("content_type") String content_type);

  @Query("SELECT * FROM user_ratings WHERE user_id = :user_id")
  public ResultSet getUserRatings(@Param("user_id") UUID user_id);

  @Query("DELETE FROM user_ratings WHERE user_id = :user_id AND content_id = :content_id AND content_type = :content_type")
  public void deleteUserRating(@Param("user_id") UUID user_id,
      @Param("content_id") UUID content_id, @Param("content_type") String content_type);
}
