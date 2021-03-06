package ca.tidygroup.repository;

import ca.tidygroup.model.Booking;
import ca.tidygroup.model.Customer;
import ca.tidygroup.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCleaningTimeBetween(ZonedDateTime start, ZonedDateTime end);
    List<Booking> findAllByCleaningTimeAfterAndStatusNotIn(ZonedDateTime afterDate, Status... statuses);
    List<Booking> findAllByStatusIn(Status... statuses);
    List<Booking> findAllByStatusNotIn(Status... statuses);
    List<Booking> findAllByCustomerOrderByCleaningTime(Customer customer);
}
