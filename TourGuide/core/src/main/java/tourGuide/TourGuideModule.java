package tourGuide;

import gps.GpsUtilService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewards.RewardsService;

import java.util.Locale;

@Configuration
public class TourGuideModule {

    @Bean
    public GpsUtilService getGpsUtilService() {
        return new GpsUtilService();
    }

	@Bean
	public RewardsService getRewardsService() {
        return new RewardsService(getGpsUtilService());
    }

	@Bean
	public Locale getLocale() {
		Locale.setDefault(Locale.US);
		return Locale.getDefault();
	}

}
