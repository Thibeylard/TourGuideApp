package tourGuide.user;

import org.javamoney.moneta.Money;
import tourGuide.dto.UserPreferencesDTO;

import javax.money.CurrencyUnit;
import javax.money.Monetary;


public class UserPreferences {

	private int attractionProximity = Integer.MAX_VALUE;
	private final CurrencyUnit currency = Monetary.getCurrency("USD");
	private Money lowerPricePoint = Money.of(0, currency);
	private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
	private int tripDuration = 1;
	private int ticketQuantity = 1;
	private int numberOfAdults = 1;
	private int numberOfChildren = 0;

	public UserPreferences() {
	}

	public UserPreferences(UserPreferencesDTO preferencesDTO) {
		this.attractionProximity = preferencesDTO.getAttractionProximity();
		this.lowerPricePoint = Money.of(preferencesDTO.getLowerPricePoint(), currency);
		this.highPricePoint = Money.of(preferencesDTO.getHighPricePoint(), currency);
		this.tripDuration = preferencesDTO.getTripDuration();
		this.ticketQuantity = preferencesDTO.getTicketQuantity();
		this.numberOfAdults = preferencesDTO.getNumberOfAdults();
		this.numberOfChildren = preferencesDTO.getNumberOfChildren();
	}

	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}

	public int getAttractionProximity() {
		return attractionProximity;
	}

	public Money getLowerPricePoint() {
		return lowerPricePoint;
	}

	public void setLowerPricePoint(Money lowerPricePoint) {
		this.lowerPricePoint = lowerPricePoint;
	}

	public Money getHighPricePoint() {
		return highPricePoint;
	}

	public void setHighPricePoint(Money highPricePoint) {
		this.highPricePoint = highPricePoint;
	}
	
	public int getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(int tripDuration) {
		this.tripDuration = tripDuration;
	}

	public int getTicketQuantity() {
		return ticketQuantity;
	}

	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}
	
	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public CurrencyUnit getCurrency() {
		return currency;
	}
}
