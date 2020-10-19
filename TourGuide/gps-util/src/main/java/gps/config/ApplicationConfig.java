package gps.config;

import gps.services.GpsUtilService;
import gps.services.GpsUtilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public GpsUtilService getGpsUtilService() {
        return new GpsUtilServiceImpl();
    }
}
