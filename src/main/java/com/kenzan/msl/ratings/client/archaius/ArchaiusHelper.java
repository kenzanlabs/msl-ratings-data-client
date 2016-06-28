package com.kenzan.msl.ratings.client.archaius;

public class ArchaiusHelper {
    public static void setupArchaius(){
        String configUrl = "file://" + System.getProperty("user.dir");
        configUrl += "/../msl-ratings-data-client-config/data-client-config.properties";
        String additionalUrlsProperty = "archaius.configurationSource.additionalUrls";
        System.setProperty(additionalUrlsProperty, configUrl);
    }
}
