package rewards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import rewards.services.GpsUtilService;
import rewards.services.GpsUtilServiceHttpImpl;

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
