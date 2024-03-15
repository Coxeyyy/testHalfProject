package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.*;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.*;
import ru.coxey.diplom.service.AdminService;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(CustomerRepository customerRepository, EmployeeRepository employeeRepository, OrderRepository orderRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Admins
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllAdmins() {
        return employeeRepository.findEmployeesByRole(Role.ADMIN);
    }
    //

    //Specialists
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllSpecialists() {
        return employeeRepository.findEmployeesByRole(Role.SPECIALIST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getOrdersBySpecialist(int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        List<Order> byEmployee = null;
        if (employeeById.isPresent()) {
            byEmployee = orderRepository.findOrdersByEmployee_Id(employeeById.get().getId());
        }
        return byEmployee;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setSpecialistToOrder(int id, String specialistId) {
        Optional<Order> orderById = orderRepository.findById(id);
        Optional<Employee> specialistById = employeeRepository.findById(Integer.parseInt(specialistId));
        if (orderById.isPresent() && specialistById.isPresent()) {
            Order order = orderById.get();
            Employee specialist = specialistById.get();
            order.setEmployee(specialist);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Ошибка");
        }
    }
    //

    //Customers
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getOrdersByCustomer(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        List<Order> listOrders = null;
        if (customerById.isPresent()) {
            listOrders = orderRepository.findOrdersByCustomer_Id(customerById.get().getId());
        }
        return listOrders;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Customer getCustomerById(int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        return customerById.orElse(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCustomer(Customer customer, int id) {
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent()) {
            Customer customerFromDB = customerById.get();
            customerFromDB.setId(customer.getId());
            customerFromDB.setLogin(customer.getLogin());
            customerFromDB.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customerFromDB);
        } else {
            throw new UsernameNotFoundException("Not found user");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
    //

    //Orders
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getActiveOrders() {
        return orderRepository.findByStatus(Status.IN_PROCESS);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Order getOrderById(int id) {
        Optional<Order> orderById = orderRepository.findById(id);
        return orderById.orElse(null);
    }
    //

    //Items
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item getItemById(int id) {
        Optional<Item> itemById = itemRepository.findById(id);
        return itemById.orElse(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateItem(Item item, int id) {
        Optional<Item> itemById = itemRepository.findById(id);
        if (itemById.isPresent()) {
            Item itemFromDB = itemById.get();
            itemFromDB.setId(item.getId());
            itemFromDB.setName(item.getName());
            itemFromDB.setPrice(item.getPrice());
            itemRepository.save(itemFromDB);
        } else {
            throw new UsernameNotFoundException("Not found user");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createNewItem(Item item) {
        itemRepository.save(item);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item findItemByName(String name) {
        Optional<Item> itemByName = itemRepository.findItemByName(name);
        return itemByName.orElse(null);
    }
    //

    //Employees
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee getEmployeeById(int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        return employeeById.orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee getEmployeeByLogin(String login) {
        Optional<Employee> employeeByLogin = employeeRepository.findEmployeeByLogin(login);
        return employeeByLogin.orElse(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateEmployee(Employee admin, int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isPresent()) {
            Employee employeeFromDB = employeeById.get();
            employeeFromDB.setId(admin.getId());
            employeeFromDB.setLogin(admin.getLogin());
            employeeFromDB.setPassword(passwordEncoder.encode(admin.getPassword()));
            employeeFromDB.setRole(admin.getRole());
            employeeRepository.save(employeeFromDB);
        } else {
            throw new UsernameNotFoundException("Not found user");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }
    //
}
