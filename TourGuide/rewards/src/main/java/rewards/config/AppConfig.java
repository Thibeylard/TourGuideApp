package rewards.config;

import common.services.GpsUtilService;
import gps.services.GpsUtilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import rewards.services.GpsUtilServiceHttpClient;

@Configuration
public class AppConfig {

    @Bean
    @Profile({"prod", "itest", "docker"})
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile({"dev", "test"})
    public GpsUtilService getGpsUtilServiceImpl() {
        return new GpsUtilServiceImpl();
    }

    @Bean
    @Profile({"prod", "itest", "docker"})
    public GpsUtilService getGpsUtilServiceHttpImpl() {
        return new GpsUtilServiceHttpClient(getRestTemplate());
    }
}
