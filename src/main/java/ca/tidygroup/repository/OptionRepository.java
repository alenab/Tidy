package ca.tidygroup.repository;

import ca.tidygroup.model.CleaningOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<CleaningOption, Long> {

}