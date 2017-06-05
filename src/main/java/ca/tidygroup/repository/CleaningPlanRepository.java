package ca.tidygroup.repository;

import ca.tidygroup.model.CleaningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningPlanRepository extends JpaRepository<CleaningPlan, Long> {
}
