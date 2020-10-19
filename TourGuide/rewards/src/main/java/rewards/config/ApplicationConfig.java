package rewards.config;

import gps.services.GpsUtilService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewards.services.GpsUtilServiceHttpImpl;
import rewards.services.RewardsService;
import rewards.services.RewardsServiceImpl;

@Configuration
public class ApplicationConfig {

    @Bean
    public RewardsService getRewardService() {
        return new RewardsServiceImpl(getGpsUtilService());
    }

    @Bean
    public GpsUtilService getGpsUtilService() {
        return new GpsUtilServiceHttpImpl();
    }
}
