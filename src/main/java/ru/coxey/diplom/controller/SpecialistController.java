package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.enums.Status;
import ru.coxey.diplom.service.impl.SpecialistServiceImpl;

@Controller
@RequestMapping("/specialistpanel")
public class SpecialistController {

    private final SpecialistServiceImpl specialistServiceImpl;

    public SpecialistController(SpecialistServiceImpl specialistServiceImpl) {
        this.specialistServiceImpl = specialistServiceImpl;
    }

    // Показать главную страницу
    @GetMapping()
    public String showSpecialistPanel() {
        return "specialistpanel/specialistpanel";
    }

    // Получить все заказы
    @GetMapping("/orders")
    public String showOrders(Model model) {
        model.addAttribute("orders", specialistServiceImpl.getOrders());
        return "specialistpanel/orders";
    }

    // Получить детальную информацию по ID заказа
    @GetMapping("/orders/{id}")
    public String showOrdersById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", specialistServiceImpl.getOrderById(id));
        model.addAttribute("statuses", Status.values());
        return "specialistpanel/showOrderByIndex";
    }

    // Поменять статус заказа
    @PatchMapping("/orders/{id}")
    public String setStatusToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String statusOrder) {
        specialistServiceImpl.setStatusToOrder(id, statusOrder);
        return "redirect:/specialistpanel";
    }

    // Показать все активные заказы
    @GetMapping("/activeOrders")
    public String showActiveOrders(Model model) {
        model.addAttribute("orders", specialistServiceImpl.getActiveOrders());
        return "specialistpanel/activeOrders";
    }
}
