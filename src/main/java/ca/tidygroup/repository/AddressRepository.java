package ca.tidygroup.repository;

import ca.tidygroup.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findOneByPostcodeEquals(String postCode);
}
