package com.kenzan.msl.ratings.client.cassandra.query;

public class RatingsHelper {

  protected static boolean isValidContentType(String contentType) {
    return contentType.equals("Album") || contentType.equals("Artist")
        || contentType.equals("Song");
  }
}
