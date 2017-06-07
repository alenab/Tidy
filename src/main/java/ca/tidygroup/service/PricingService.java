package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.ApartmentUnit;
import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import ca.tidygroup.repository.ApartmentUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricingService {

    public static final int TAX = 12;

    private ApartmentUnitRepository apartmentUnitRepository;

    @Autowired
    public PricingService(ApartmentUnitRepository apartmentUnitRepository) {
        this.apartmentUnitRepository = apartmentUnitRepository;
    }

    public double getPrice(BookingForm form) {
        int numberOfRooms = Integer.parseInt(form.getNumberOfRooms());
        int numberOfBathrooms = Integer.parseInt(form.getNumberOfBathrooms());
        Double basePrice = getApartmentUnit(form.getCleaningPlan(), numberOfRooms, numberOfBathrooms).getPrice();
        int discount = Integer.parseInt(form.getDiscount());
        return getFinalPrice(basePrice, discount, form.getCleaningOptions());
    }

    private double getPriceWithOptions(double basePrice, List<CleaningOption> additionalOptions) {
        double price = basePrice;
        for (CleaningOption option : additionalOptions) {
            price += option.getPrice();
        }
        return price;
    }

    private double getPriceWithDiscount(double basePrice, int discountPercent, List<CleaningOption> additionalOptions) {
        double price = getPriceWithOptions(basePrice, additionalOptions);
        return price - price * discountPercent / 100;
    }

    public double getFinalPrice(double basePrice, int discountPercent, List<CleaningOption> additionalOptions) {
        double price = getPriceWithDiscount(basePrice, discountPercent, additionalOptions);
        return price + price * TAX / 100;
    }

    private ApartmentUnit getApartmentUnit(CleaningPlan plan, int numberOfRooms, int numberOfBathroom) {
        return apartmentUnitRepository.findApartmentUnitByCleaningPlanAndNumberOfBedroomsAndNumberOfBathrooms(plan, numberOfRooms, numberOfBathroom);
    }
}
