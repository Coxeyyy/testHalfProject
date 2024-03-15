package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Order;

import java.util.List;


public interface SpecialistService {

    public List<Order> getOrders();

    public List<Order> getActiveOrders();

    public Order getOrderById(int id);

    @Transactional
    public void setStatusToOrder(int id, String statusOrder);

}
