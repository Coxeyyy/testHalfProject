package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;

import java.util.List;

public interface AdminService {

    public List<Employee> getAllAdmins();

    public List<Employee> getAllSpecialists();

    public List<Order> getOrdersBySpecialist(int id);

    @Transactional
    public void setSpecialistToOrder(int id, String specialistId);

    public List<Customer> getAllCustomers();

    public List<Order> getOrdersByCustomer(int id);

    public Customer getCustomerById(int id);

    @Transactional
    public void updateCustomer(Customer customer, int id);

    @Transactional
    public void deleteCustomer(int id);

    public List<Order> getAllOrders();

    public List<Order> getActiveOrders();

    public Order getOrderById(int id);

    public List<Item> getAllItems();

    public Item getItemById(int id);

    @Transactional
    public void updateItem(Item item, int id);

    @Transactional
    public void deleteItem(int id);

    @Transactional
    public void createNewItem(Item item);

    public Item findItemByName(String name);

    public Employee getEmployeeById(int id);

    public Employee getEmployeeByLogin(String login);

    @Transactional
    public void updateEmployee(Employee admin, int id);

    @Transactional
    public void deleteEmployee(int id);

}
