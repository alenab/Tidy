package ca.tidygroup.service;

import ca.tidygroup.dto.ApartmentUnitDTO;
import ca.tidygroup.dto.ApartmentUnitListDTO;
import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.dto.OptionListDTO;
import ca.tidygroup.model.ApartmentUnit;
import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import ca.tidygroup.repository.ApartmentUnitRepository;
import ca.tidygroup.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PricingService {

    public static final int TAX = 5;

    private ApartmentUnitRepository apartmentUnitRepository;

    private OptionRepository optionRepository;

    @Autowired
    public PricingService(ApartmentUnitRepository apartmentUnitRepository, OptionRepository optionRepository) {
        this.apartmentUnitRepository = apartmentUnitRepository;
        this.optionRepository = optionRepository;
    }

    public List<ApartmentUnitDTO> getAllApartmentUnits() {
        return apartmentUnitRepository.findAll().stream()
                .map(item -> new ApartmentUnitDTO(item.getCleaningPlan().getId(), item.getNumberOfBedrooms(),
                        item.getNumberOfBathrooms(), item.getPrice())
                ).collect(Collectors.toList());
    }

    public List<ApartmentUnit> getAllApartUnits() {
        return apartmentUnitRepository.findAll(new Sort(Sort.Direction.ASC, ApartmentUnit.ID_COL_NAME));
    }

    public double getPrice(BookingForm form) {
        int numberOfRooms = Integer.parseInt(form.getNumberOfRooms());
        int numberOfBathrooms = Integer.parseInt(form.getNumberOfBathrooms());
        Double basePrice = getApartmentUnit(form.getCleaningPlan(), numberOfRooms, numberOfBathrooms).getPrice();
        return getPriceWithOptions(basePrice, form.getCleaningOptions());
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

    @Transactional
    public void updatePrices(ApartmentUnitListDTO allUnits) {
        apartmentUnitRepository.save(allUnits.getUnits());
    }

    @Transactional
    public void updateOptionsPrices(OptionListDTO allOptions) {
        optionRepository.save(allOptions.getOptions());

    }

    public List<CleaningOption> getAllOptions() {
        return optionRepository.findAll(new Sort(Sort.Direction.ASC, CleaningOption.ID_COL_NAME));
    }
}
