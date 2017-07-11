package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.EmployeeDTO;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.AccountRepository;
import ca.tidygroup.repository.BookingRepository;
import ca.tidygroup.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
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
        bookingDTOAdmin.setAptNumber(booking.getAddressForClean().getAptNumber());
        bookingDTOAdmin.setFirstName(booking.getCustomer().getFirstName());
        bookingDTOAdmin.setLastName(booking.getCustomer().getLastName());
        bookingDTOAdmin.setEmail(booking.getAccount().getEmail());
        bookingDTOAdmin.setPhone(booking.getCustomer().getPhoneNumber());
        bookingDTOAdmin.setDiscount(booking.getDiscountPercent());
        bookingDTOAdmin.setPrice(booking.getPrice());
        bookingDTOAdmin.setFinalPrice(getFinalPrice(booking));
        bookingDTOAdmin.setEmployeeId(booking.getEmployeeId());
        bookingDTOAdmin.setDuration(booking.getDuration());
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

    @Transactional(readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepository.findAllEmployeeByActive(true);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployeeDTO() {
        List<Employee> employees = getEmployees();
        List<EmployeeDTO> resultList = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(employee.getId());
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
            employee.setActive(true);
            employeeRepository.save(employee);
        } else {
            Employee employee = employeeRepository.findEmployeeByAccount(accountRepository.findAccountByEmail(employeeDTO.getEmail()));
            if (employee != null) {
                // propose to correct employee
            }
        }
    }

    public EmployeeDTO getEmployeeById(long id) {
         Employee employee = employeeRepository.getEmployeeById(id);
         EmployeeDTO employeeDTO = new EmployeeDTO();
         employeeDTO.setId(employee.getId());
         employeeDTO.setFirstName(employee.getFirstName());
         employeeDTO.setLastName(employee.getLastName());
         employeeDTO.setEmail(employee.getAccount().getEmail());
         employeeDTO.setPhoneNumber(employee.getPhoneNumber());
         employeeDTO.setRate(employee.getRate());
         return employeeDTO;
    }

    @Transactional
    public void updateEmployee(long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee: {}", employeeDTO);

        Employee employee = employeeRepository.getOne(id);

        Account account = employee.getAccount();
        account.setEmail(employeeDTO.getEmail());
        account.setLogin(employeeDTO.getEmail());

        employee.setAccount(account);
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setRate(employeeDTO.getRate());
        employeeRepository.save(employee);

    }

    @Transactional
    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }
}
