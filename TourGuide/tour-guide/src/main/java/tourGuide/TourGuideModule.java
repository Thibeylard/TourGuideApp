package tourGuide;

import common.services.GpsUtilService;
import common.services.RewardsService;
import common.services.TripPricerService;
import gps.services.GpsUtilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import rewards.services.GpsUtilServiceHttpClient;
import rewards.services.RewardsServiceImpl;
import tourGuide.services.RewardsServiceHttpClient;
import tourGuide.services.TripPricerServiceHttpClient;
import tripPricer.services.TripPricerServiceImpl;

import java.util.Locale;

@Configuration
public class TourGuideModule {


    // Common beans

    @Bean
    public Locale getLocale() {
        Locale.setDefault(Locale.US);
        return Locale.getDefault();
    }

    // Production and Integration tests environment beans

    @Bean
    @Profile({"prod", "itest", "docker"})
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile({"prod", "itest", "docker"})
    public RewardsService getRewardsServiceHttp() {
        return new RewardsServiceHttpClient(getRestTemplate());
    }

    @Bean
    @Profile({"prod", "itest", "docker"})
    public GpsUtilService getGpsUtilServiceHttp() {
        return new GpsUtilServiceHttpClient(getRestTemplate());
    }

    @Bean
    @Profile({"prod", "itest", "docker"})
    public TripPricerService getTripPricerServiceHttp() {
        return new TripPricerServiceHttpClient(getRestTemplate());
    }

    // Development and Unit tests environment beans

    @Bean
    @Profile({"dev", "test"})
    public GpsUtilService getGpsUtilServiceImpl() {
        return new GpsUtilServiceImpl();
    }


    @Bean
    @Profile({"dev", "test"})
    public RewardsService getRewardsServiceImpl() {
        return new RewardsServiceImpl(getGpsUtilServiceImpl());
    }

    @Bean
    @Profile({"dev", "test"})
    public TripPricerService getTripPricerServiceImpl() {
        return new TripPricerServiceImpl();
    }

}
