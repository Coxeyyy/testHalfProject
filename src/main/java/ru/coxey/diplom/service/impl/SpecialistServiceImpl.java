package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.repository.EmployeeRepository;
import ru.coxey.diplom.repository.OrderRepository;
import ru.coxey.diplom.service.SpecialistService;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistServiceImpl implements SpecialistService {

    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;

    public SpecialistServiceImpl(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public List<Order> getOrders() {
        int idSpecialist = getIdSpecialist();
        return orderRepository.findOrdersByEmployee_Id(idSpecialist);
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public List<Order> getActiveOrders() {
        int idSpecialist = getIdSpecialist();
        return orderRepository.findOrdersByEmployee_id_AndStatus(idSpecialist, Status.IN_PROCESS);
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public Order getOrderById(int id) {
        Optional<Order> orderById = orderRepository.findById(id);
        return orderById.orElse(null);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    public void setStatusToOrder(int id, String statusOrder) {
        Optional<Order> orderById = orderRepository.findById(id);
        if (orderById.isPresent()) {
            Order order = orderById.get();
            order.setStatus(Status.valueOf(statusOrder));
            orderRepository.save(order);
        }
    }

    private Integer getIdSpecialist() {
        String loginSpecialist = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Person> byLogin = employeeRepository.findByLogin(loginSpecialist);
        if (byLogin.isPresent()) {
            return byLogin.get().getId();
        }
        return null;
    }
}
