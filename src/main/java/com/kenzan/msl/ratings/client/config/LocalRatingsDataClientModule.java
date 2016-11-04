package com.kenzan.msl.ratings.client.config;

import com.google.inject.AbstractModule;
import com.kenzan.msl.ratings.client.services.RatingsDataClientService;
import com.kenzan.msl.ratings.client.services.RatingsDataClientServiceStub;

/**
 * @author Kenzan
 */
public class LocalRatingsDataClientModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(RatingsDataClientService.class).to(RatingsDataClientServiceStub.class).asEagerSingleton();
    }
}
