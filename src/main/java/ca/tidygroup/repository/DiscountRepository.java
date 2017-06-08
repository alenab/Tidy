package ca.tidygroup.repository;

import ca.tidygroup.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findByCodeEqualsIgnoringCase(String code);
}
