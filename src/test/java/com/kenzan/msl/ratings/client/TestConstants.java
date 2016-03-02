package com.kenzan.msl.ratings.client;

import java.util.UUID;

import com.kenzan.msl.ratings.client.dto.AverageRatingsDto;
import com.kenzan.msl.ratings.client.dto.UserRatingsDto;

public class TestConstants {

  private static TestConstants instance = null;

  public UUID ALBUM_ID = UUID.fromString("2883607a-176d-4729-a20b-ec441c285afb");
  public UUID USER_ID = UUID.fromString("2883607a-176d-4729-a20b-ec441c285afb");
  public String ALBUM_CONTENT_TYPE = "Album";
  public int RATING = 2;
  public Long NUM_RATING = Long.valueOf("4");
  public Long SUM_RATING = Long.valueOf("3");
  public int LIMIT = 12;

  public AverageRatingsDto AVERAGE_RATINGS_DTO = new AverageRatingsDto();
  public UserRatingsDto USER_RATINGS_DTO = new UserRatingsDto();

  private TestConstants() {
    AVERAGE_RATINGS_DTO.setContentId(ALBUM_ID);
    AVERAGE_RATINGS_DTO.setContentType(ALBUM_CONTENT_TYPE);
    AVERAGE_RATINGS_DTO.setNumRating(NUM_RATING);
    AVERAGE_RATINGS_DTO.setSumRating(SUM_RATING);

    USER_RATINGS_DTO.setContentType(ALBUM_CONTENT_TYPE);
    USER_RATINGS_DTO.setContentUuid(ALBUM_ID);
    USER_RATINGS_DTO.setRating(RATING);
    USER_RATINGS_DTO.setUserId(USER_ID);
  }

  public static TestConstants getInstance() {
    if (instance == null) {
      instance = new TestConstants();
    }
    return instance;
  }
}
