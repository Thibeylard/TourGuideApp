package tourGuide;

import gps.services.GpsUtilService;
import gps.services.GpsUtilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import rewards.services.GpsUtilServiceHttpImpl;
import rewards.services.RewardsService;
import rewards.services.RewardsServiceImpl;
import tourGuide.services.RewardsServiceHttpImpl;

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
    @Profile({"prod", "itest"})
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile({"prod", "itest"})
    public RewardsService getRewardsServiceHttp() {
        return new RewardsServiceHttpImpl(getRestTemplate());
    }


    @Bean
    @Profile({"prod", "itest"})
    public GpsUtilService getGpsUtilServiceHttp() {
        return new GpsUtilServiceHttpImpl(getRestTemplate());
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

}
