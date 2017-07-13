package ca.tidygroup.repository;

import ca.tidygroup.model.TimeLimitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeLimitationsRepository extends JpaRepository <TimeLimitations, Long>{
    List<TimeLimitations>  findAllByDate (LocalDate date);
}
