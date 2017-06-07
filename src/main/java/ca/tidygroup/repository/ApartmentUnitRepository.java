package ca.tidygroup.repository;

import ca.tidygroup.model.ApartmentUnit;
import ca.tidygroup.model.CleaningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentUnitRepository extends JpaRepository<ApartmentUnit, Long> {
    ApartmentUnit findApartmentUnitByCleaningPlanAndNumberOfBedroomsAndNumberOfBathrooms(CleaningPlan plan, int numberOfRooms, int numberOfBathrooms);
}
