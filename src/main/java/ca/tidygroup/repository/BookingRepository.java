package ca.tidygroup.repository;

import ca.tidygroup.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCleaningTimeBetween(ZonedDateTime start, ZonedDateTime end);
}
