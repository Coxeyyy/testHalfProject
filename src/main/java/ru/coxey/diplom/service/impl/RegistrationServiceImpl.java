package ru.coxey.diplom.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.repository.CustomerRepository;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(CustomerRepository customerRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);
    }

    @Transactional
    public void registerEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }
}
