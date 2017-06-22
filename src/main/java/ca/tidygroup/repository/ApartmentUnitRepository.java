package ca.tidygroup.repository;

import ca.tidygroup.model.ApartmentUnit;
import ca.tidygroup.model.CleaningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import java.util.List;

@Repository
public interface ApartmentUnitRepository extends JpaRepository<ApartmentUnit, Long> {
    ApartmentUnit findApartmentUnitByCleaningPlanAndNumberOfBedroomsAndNumberOfBathrooms(CleaningPlan plan, int numberOfRooms, int numberOfBathrooms);

    @Query("select distinct a.numberOfBedrooms  from ApartmentUnit a order by a.numberOfBedrooms ASC")
    List<Integer> getListOfBedrooms();
}
