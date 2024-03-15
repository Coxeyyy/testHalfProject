package ru.coxey.diplom.model;

import ru.coxey.diplom.model.enums.Status;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id_order")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "id_customer")
    @OneToOne
    private Customer customer;
    @JoinColumn(name = "id_specialist")
    @OneToOne
    private Employee employee;
    @ManyToMany
    @JoinTable(
            name = "orders_items",
            joinColumns = @JoinColumn(name = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "id_item")
    )
    private List<Item> items;
    @Column(name = "status_order")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Order() {
    }

    public Order(Customer customer, Employee employee, List<Item> items, Status status) {
        this.customer = customer;
        this.employee = employee;
        this.items = items;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", employee=" + employee +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
