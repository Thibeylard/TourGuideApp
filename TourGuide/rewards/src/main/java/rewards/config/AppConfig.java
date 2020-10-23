package rewards.config;

import gps.services.GpsUtilService;
import gps.services.GpsUtilServiceHttpImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GpsUtilService getGpsUtilService() {
        return new GpsUtilServiceHttpImpl(getRestTemplate());
    }
}
