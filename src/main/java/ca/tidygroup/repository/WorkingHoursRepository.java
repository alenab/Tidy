package ca.tidygroup.repository;

import ca.tidygroup.model.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    WorkingHours getWorkingHoursById(Long id);
}
