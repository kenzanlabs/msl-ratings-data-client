/*
 * Copyright 2015, Kenzan, All rights reserved.
 */
package com.kenzan.msl.ratings.client.cassandra.query;

public class RatingsHelper {

    /**
     * Determines whether a given content type is valid or not
     *
     * @param contentType String
     * @return Boolean
     */
    protected static boolean isValidContentType(String contentType) {
        return contentType.equals("Album") || contentType.equals("Artist") || contentType.equals("Song");
    }
}
