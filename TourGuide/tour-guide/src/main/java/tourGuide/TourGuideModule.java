package tourGuide;

import gps.services.GpsUtilService;
import gps.services.GpsUtilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewards.services.RewardsServiceImpl;

import java.util.Locale;

@Configuration
public class TourGuideModule {

    @Bean
    public GpsUtilService getGpsUtilService() {
        return new GpsUtilServiceImpl();
    }

	@Bean
	public RewardsServiceImpl getRewardsService() {
		return new RewardsServiceImpl(getGpsUtilService());
	}

	@Bean
	public Locale getLocale() {
		Locale.setDefault(Locale.US);
		return Locale.getDefault();
	}

}
