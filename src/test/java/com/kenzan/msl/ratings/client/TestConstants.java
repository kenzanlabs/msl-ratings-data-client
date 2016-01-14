package com.kenzan.msl.ratings.client;

import java.util.UUID;

import com.kenzan.msl.ratings.client.dao.AverageRatingsDao;
import com.kenzan.msl.ratings.client.dao.UserRatingsDao;

public class TestConstants {

    private static TestConstants instance = null;

    public UUID ALBUM_ID = UUID.fromString("2883607a-176d-4729-a20b-ec441c285afb");
    public UUID USER_ID = UUID.fromString("2883607a-176d-4729-a20b-ec441c285afb");
    public String ALBUM_CONTENT_TYPE = "Album";
    public int RATING = 2;
    public Long NUM_RATING = Long.valueOf("4");
    public Long SUM_RATING = Long.valueOf("3");
    public int LIMIT = 12;

    public AverageRatingsDao AVERAGE_RATINGS_DAO = new AverageRatingsDao();
    public UserRatingsDao USER_RATINGS_DAO = new UserRatingsDao();

    private TestConstants() {
        AVERAGE_RATINGS_DAO.setContentId(ALBUM_ID);
        AVERAGE_RATINGS_DAO.setContentType(ALBUM_CONTENT_TYPE);
        AVERAGE_RATINGS_DAO.setNumRating(NUM_RATING);
        AVERAGE_RATINGS_DAO.setSumRating(SUM_RATING);

        USER_RATINGS_DAO.setContentType(ALBUM_CONTENT_TYPE);
        USER_RATINGS_DAO.setContentUuid(ALBUM_ID);
        USER_RATINGS_DAO.setRating(RATING);
        USER_RATINGS_DAO.setUserId(USER_ID);
    }

    public static TestConstants getInstance() {
        if ( instance == null ) {
            instance = new TestConstants();
        }
        return instance;
    }
}
