package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.EmployeeDTO;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Booking;
import ca.tidygroup.model.Employee;
import ca.tidygroup.model.Role;
import ca.tidygroup.repository.AccountRepository;
import ca.tidygroup.repository.BookingRepository;
import ca.tidygroup.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private BookingRepository bookingRepository;
    private EmployeeRepository employeeRepository;
    private AccountRepository accountRepository;

    @Autowired
    public AdminService(BookingRepository bookingRepository, EmployeeRepository employeeRepository, AccountRepository accountRepository) {
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public List<BookingDTOAdmin> getAllBookings() {
        List<BookingDTOAdmin> result = new ArrayList<>();
        List<Booking> allBookings = bookingRepository.findAll();
        for (Booking booking : allBookings) {
            BookingDTOAdmin bookingDTOAdmin = getBookingDTO(booking);
            result.add(bookingDTOAdmin);
        }
        return result;
    }

    @Transactional
    public BookingDTOAdmin getBookingDTO(Booking booking) {
        BookingDTOAdmin bookingDTOAdmin = new BookingDTOAdmin();
        bookingDTOAdmin.setId(booking.getId());
        bookingDTOAdmin.setCleaningPlan(booking.getCleaningPlan());
        bookingDTOAdmin.setNumberOfRooms(booking.getNumberOfRooms());
        bookingDTOAdmin.setNumberOfBathrooms(booking.getNumberOfBathrooms());
        bookingDTOAdmin.setCleaningOptions(booking.getAdditionalOptions());
        bookingDTOAdmin.setSpecialRequest(booking.getSpecialRequest());
        bookingDTOAdmin.setCleaningDate(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        bookingDTOAdmin.setCleaningTime(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        bookingDTOAdmin.setAddress(booking.getAddressForClean().getAddress());
        bookingDTOAdmin.setFirstName(booking.getCustomer().getFirstName());
        bookingDTOAdmin.setLastName(booking.getCustomer().getLastName());
        bookingDTOAdmin.setEmail(booking.getAccount().getEmail());
        bookingDTOAdmin.setPhone(booking.getCustomer().getPhoneNumber());
        bookingDTOAdmin.setDiscount(booking.getDiscountPercent());
        bookingDTOAdmin.setPrice(booking.getPrice());
        bookingDTOAdmin.setFinalPrice(getFinalPrice(booking));
        return bookingDTOAdmin;
    }

    private double getFinalPrice(Booking booking) {
        double price = booking.getPrice();
        double priceWithDiscount = price - price * booking.getDiscountPercent() / 100;
        return priceWithDiscount + priceWithDiscount * PricingService.TAX / 100;
    }

    public BookingDTOAdmin getBookingById(long id) {
        return getBookingDTO(bookingRepository.getOne(id));
    }

    @Transactional
    public List<EmployeeDTO> getAllEmployeeDTO () {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> resultList = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setFirstName(employee.getFirstName());
            employeeDTO.setLastName(employee.getLastName());
            employeeDTO.setEmail(employee.getAccount().getEmail());
            employeeDTO.setPhoneNumber(employee.getPhoneNumber());
            employeeDTO.setRate(employee.getRate());

            resultList.add(employeeDTO);
        }
        return resultList;
    }

    @Transactional
    public void addEmployee(EmployeeDTO employeeDTO) {
        if (accountRepository.findAccountByEmail(employeeDTO.getEmail()) == null) {

            Account account = new Account();
            account.setEmail(employeeDTO.getEmail());
            account.setLogin(employeeDTO.getEmail());
            account.setUserRole(Role.EMPLOYEE);
            accountRepository.save(account);

            Employee employee = new Employee();
            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setPhoneNumber(employeeDTO.getPhoneNumber());
            employee.setRate(employeeDTO.getRate());
            employee.setAccount(account);
            employeeRepository.save(employee);

        } else {
            employeeRepository.findEmployeeByAccount(accountRepository.findAccountByEmail(employeeDTO.getEmail()));
            // propose to correct employee
        }




    }

}
