package ca.tidygroup.repository;

import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<CleaningOption, Long> {

    List<CleaningOption> findAllByPlanListContains(CleaningPlan cleaningPlan);

}