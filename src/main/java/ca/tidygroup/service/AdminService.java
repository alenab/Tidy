package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.CustomerDTO;
import ca.tidygroup.dto.EmployeeDTO;
import ca.tidygroup.dto.WorkingHoursDTO;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);
    private BookingRepository bookingRepository;
    private EmployeeRepository employeeRepository;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private WorkingHoursRepository workingHoursRepository;

    @Autowired
    public AdminService(BookingRepository bookingRepository, EmployeeRepository employeeRepository, AccountRepository accountRepository, CustomerRepository customerRepository, WorkingHoursRepository workingHoursRepository) {
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.workingHoursRepository = workingHoursRepository;
    }

    @Transactional
    public List<BookingDTOAdmin> getAllBookings() {
        List<BookingDTOAdmin> result = new ArrayList<>();
        List<Booking> allBookings = bookingRepository.findAllByStatusNotIn(Status.CANCELLED);
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
        bookingDTOAdmin.setEmail(booking.getCustomer().getAccount().getEmail());
        bookingDTOAdmin.setPhone(booking.getCustomer().getPhoneNumber());
        bookingDTOAdmin.setDiscount(booking.getDiscountPercent());
        bookingDTOAdmin.setPrice(booking.getPrice());
        bookingDTOAdmin.setFinalPrice(getFinalPrice(booking));
        bookingDTOAdmin.setEmployeeId(booking.getEmployeeId());
        bookingDTOAdmin.setDuration(booking.getDuration());
        bookingDTOAdmin.setStatus(booking.getStatus());
        bookingDTOAdmin.setGetInNotes(booking.getGetInNotes());
        bookingDTOAdmin.setAdminNotes(booking.getAdminNotes());
        bookingDTOAdmin.setBillingCustomerId(booking.getCustomer().getBillingCustomerId());
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


    public List<Employee> getEmployees() {
        return employeeRepository.findAllEmployeeByActive(true);
    }

    public List<Employee> getNoActiveEmployees() {
        return employeeRepository.findAllEmployeeByActive(false);
    }

    private List<EmployeeDTO> transformListEmployeeToDTO(List <Employee> employees) {
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

    public List<EmployeeDTO> getAllEmployeeDTO() {
        List<Employee> employees = getEmployees();
        return transformListEmployeeToDTO(employees);
    }

    public List<EmployeeDTO> getNoActiveEmployeeDTO() {
        List<Employee> employees = getNoActiveEmployees();
        return transformListEmployeeToDTO(employees);
    }


    @Transactional
    public void addEmployee(EmployeeDTO employeeDTO) {
        if (accountRepository.findAccountByEmailIgnoreCase(employeeDTO.getEmail()) == null) {

            Account account = new Account();
            account.setEmail(employeeDTO.getEmail().toLowerCase());
            account.setLogin(employeeDTO.getEmail().toLowerCase());
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
            Employee employee = employeeRepository.findEmployeeByAccount(accountRepository.findAccountByEmailIgnoreCase(employeeDTO.getEmail()));
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
        account.setEmail(employeeDTO.getEmail().toLowerCase());
        account.setLogin(employeeDTO.getEmail().toLowerCase());

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

    public List<CustomerDTO> getAllCustomersDTO() {
        List<Customer> list = customerRepository.findAll();
        List<CustomerDTO> resultList = new ArrayList<>();
         for (Customer customer : list) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customer.getId());
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setEmail(customer.getAccount().getEmail());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            customerDTO.setAddresses(customer.getUserAddress());
            resultList.add(customerDTO);
        }
        return resultList;
    }

    public WorkingHoursDTO getWorkingHoursDTO() {
        WorkingHoursDTO workingHoursDTO = new WorkingHoursDTO();
        WorkingHours workingHours = workingHoursRepository.findAll().get(0);
        workingHoursDTO.setStartTime(workingHours.getStartTime().toString());
        workingHoursDTO.setEndTime(workingHours.getEndTime().toString());
        workingHoursDTO.setStep(((Integer)workingHours.getStep()).toString());
        return workingHoursDTO;
    }

    public void updateWorkingHours(WorkingHoursDTO workingHoursDTO) {
        WorkingHours workingHours = workingHoursRepository.findAll().get(0);
        workingHours.setStartTime(LocalTime.parse(workingHoursDTO.getStartTime()));
        workingHours.setEndTime(LocalTime.parse(workingHoursDTO.getEndTime()));
        workingHours.setStep(Integer.parseInt(workingHoursDTO.getStep()));
        workingHoursRepository.save(workingHours);
    }

}
