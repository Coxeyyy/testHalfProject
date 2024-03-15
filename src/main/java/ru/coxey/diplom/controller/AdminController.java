package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.model.Order;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.impl.AdminServiceImpl;
import ru.coxey.diplom.service.impl.RegistrationServiceImpl;
import ru.coxey.diplom.util.EmployeeValidator;
import ru.coxey.diplom.util.ItemValidator;
import ru.coxey.diplom.util.PersonValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/adminpanel")
public class AdminController {

    private final PersonValidator personValidator;
    private final RegistrationServiceImpl registrationServiceImpl;
    private final AdminServiceImpl adminServiceImpl;
    private final ItemValidator itemValidator;
    private final EmployeeValidator employeeValidator;

    public AdminController(PersonValidator personValidator, RegistrationServiceImpl registrationServiceImpl, AdminServiceImpl adminServiceImpl, ItemValidator itemValidator, EmployeeValidator employeeValidator) {
        this.personValidator = personValidator;
        this.registrationServiceImpl = registrationServiceImpl;
        this.adminServiceImpl = adminServiceImpl;
        this.itemValidator = itemValidator;
        this.employeeValidator = employeeValidator;
    }

    // Главная страница админ панели
    @GetMapping()
    public String getAdminPanel() {
        return "adminpanel/adminpanel";
    }

    // Страница регистрации
    @GetMapping("/register")
    public String registerNewEmployee(Model model) {
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("employee", new Employee());
        model.addAttribute("roles", role);
        return "adminpanel/register";
    }

    // Регистрация нового сотрудника
    @PostMapping("/register")
    public String registerNewEmployee(@ModelAttribute("employee") @Valid Employee employee,
                                      BindingResult bindingResult, Model model) {
        personValidator.validate(employee, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/register";
        }
        registrationServiceImpl.registerEmployee(employee);
        return "redirect:/adminpanel";
    }

    // Получение всех админов
    @GetMapping("/admins")
    public String getAllAdmins(Model model) {
        model.addAttribute("admins", adminServiceImpl.getAllAdmins());
        return "adminpanel/admin/admins";
    }

    // Получение админов по ID
    @GetMapping("/admins/{id}")
    public String getAdminById(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", adminServiceImpl.getEmployeeById(id));
        return "adminpanel/admin/showAdminByIndex";
    }

    // Изменение админов
    @GetMapping("/admins/{id}/edit")
    public String editAdmin(Model model, @PathVariable("id") int id) {
        model.addAttribute("admin", adminServiceImpl.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/admin/editAdmin";
    }

    // Обновление инфы по админам
    @PatchMapping("/admins/{id}")
    public String updateAdmin(@ModelAttribute("admin") @Valid Employee admin, @PathVariable("id") int id,
                              BindingResult bindingResult, Model model) {
        employeeValidator.validate(admin, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/admin/editAdmin";
        }
        adminServiceImpl.updateEmployee(admin, id);
        return "redirect:/adminpanel/admins";
    }

    // Удаление админа
    @DeleteMapping("/admins/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        adminServiceImpl.deleteEmployee(id);
        return "redirect:/adminpanel";
    }

    // Получение всех специалистов
    @GetMapping("/specialists")
    public String getAllSpecialist(Model model) {
        model.addAttribute("specialists", adminServiceImpl.getAllSpecialists());
        return "adminpanel/specialist/specialists";
    }

    // Специалист по ID
    @GetMapping("/specialists/{id}")
    public String getSpecialistById(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", adminServiceImpl.getEmployeeById(id));
        return "adminpanel/specialist/showSpecialistByIndex";
    }

    // Изменение специалистов
    @GetMapping("/specialists/{id}/edit")
    public String editSpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", adminServiceImpl.getEmployeeById(id));
        List<Role> role = new ArrayList<>();
        role.add(Role.getByCode("Администратор"));
        role.add(Role.getByCode("Специалист"));
        model.addAttribute("roles", role);
        return "adminpanel/specialist/editSpecialist";
    }

    // Обновление инфы по спецам
    @PatchMapping("/specialists/{id}")
    public String updateSpecialist(@ModelAttribute("specialist") @Valid Employee specialist, @PathVariable("id") int id,
                                   BindingResult bindingResult, Model model) {
        employeeValidator.validate(specialist, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Role> role = new ArrayList<>();
            role.add(Role.getByCode("Администратор"));
            role.add(Role.getByCode("Специалист"));
            model.addAttribute("roles", role);
            return "adminpanel/specialist/editSpecialist";
        }
        adminServiceImpl.updateEmployee(specialist, id);
        return "redirect:/adminpanel/specialists";
    }

    // Удалить специалиста
    @DeleteMapping("/specialists/{id}")
    public String deleteSpecialist(@PathVariable("id") int id) {
        adminServiceImpl.deleteEmployee(id);
        return "redirect:/adminpanel";
    }

    // Получить список заказов конкретного специалиста
    @GetMapping("/specialists/{id}/orders")
    public String getOrdersBySpecialist(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", adminServiceImpl.getEmployeeById(id));
        model.addAttribute("ordersBySpecialist", adminServiceImpl.getOrdersBySpecialist(id));
        return "adminpanel/specialist/getOrdersBySpecialist";
    }

    // Список всех заказчиков
    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        model.addAttribute("customers", adminServiceImpl.getAllCustomers());
        return "adminpanel/customer/customers";
    }

    //  Заказчик по ID
    @GetMapping("/customers/{id}")
    public String getCustomerById(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", adminServiceImpl.getCustomerById(id));
        return "adminpanel/customer/showCustomerByIndex";
    }

    // Изменение заказчиков
    @GetMapping("/customers/{id}/edit")
    public String editCustomer(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", adminServiceImpl.getCustomerById(id));
        return "adminpanel/customer/editCustomer";
    }

    // Обновление инфы по заказчику
    @PatchMapping("/customers/{id}")
    public String updateCustomer(@ModelAttribute("specialist") Customer customer, @PathVariable("id") int id) {
        adminServiceImpl.updateCustomer(customer, id);
        return "redirect:/adminpanel/customer/customers";
    }

    // Удалить заказчика
    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable("id") int id) {
        adminServiceImpl.deleteCustomer(id);
        return "redirect:/adminpanel";
    }

    // Получить список заказов конкретного заказчика
    @GetMapping("/customers/{id}/orders")
    public String getOrdersByCustomer(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", adminServiceImpl.getCustomerById(id));
        model.addAttribute("ordersByCustomer", adminServiceImpl.getOrdersByCustomer(id));
        return "adminpanel/customer/getOrdersByCustomer";
    }

    // Все заказы
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", adminServiceImpl.getAllOrders());
        return "adminpanel/order/orders";
    }

    // Заказ по ID
    @GetMapping("/orders/{id}")
    public String getOrderById(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", adminServiceImpl.getOrderById(id));
        model.addAttribute("specialists", adminServiceImpl.getAllSpecialists());
        return "adminpanel/order/showOrderByIndex";
    }

//    // Изменить заказ
//    @GetMapping("/orders/{id}/edit")
//    public String editOrder(Model model, @PathVariable("id") int id) {
//        model.addAttribute("order", adminService.getOrderById(id));
//        model.addAttribute("specialists", adminService.getAllSpecialists());
//        model.addAttribute("items", adminService.getAllItems());
//        return "adminpanel/order/editOrder";
//    }
//
//    @PostMapping("/orders/{id}")
//    public void editListOrders(Model model, @PathVariable("id")int id) {
//        System.out.println("haha");
//    }

    // Назначить специалиста на заказ
    @PatchMapping("/orders/{id}")
    public String setSpecialistToOrder(@PathVariable("id") int id, @RequestParam("selectedOption") String specialistId) {
        adminServiceImpl.setSpecialistToOrder(id, specialistId);
        return "redirect:/adminpanel";
    }

    // Все услуги
    @GetMapping("/items")
    public String getAllItems(Model model) {
        model.addAttribute("items", adminServiceImpl.getAllItems());
        return "adminpanel/item/items";
    }

    // Услуги по ID
    @GetMapping("/items/{id}")
    public String getItemById(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", adminServiceImpl.getItemById(id));
        return "adminpanel/item/showItemByIndex";
    }

    // Изменение услуги
    @GetMapping("/items/{id}/edit")
    public String editItem(Model model, @PathVariable("id") int id) {
        model.addAttribute("item", adminServiceImpl.getItemById(id));
        return "adminpanel/item/editItem";
    }

    // Обновление инфы по услуге
    @PatchMapping("/items/{id}")
    public String updateItem(@ModelAttribute("item") @Valid Item item, @PathVariable("id") int id,
                             BindingResult bindingResult) {
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) {
            return "adminpanel/item/editItem";
        }
        adminServiceImpl.updateItem(item, id);
        return "redirect:/adminpanel/items";
    }

    // Удалить услугу
    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        adminServiceImpl.deleteItem(id);
        return "redirect:/adminpanel";
    }

    // Создать новую услугу
    @GetMapping("/items/new")
    public String createNewItem(Model model) {
        model.addAttribute("item", new Item());
        return "adminpanel/item/newItem";
    }

    // Создать новую услугу
    @PostMapping("/items/new")
    public String createNewItem(@ModelAttribute("item") @Valid Item item,
                                BindingResult bindingResult) {
        itemValidator.validate(item, bindingResult);
        if (bindingResult.hasErrors()) {
            return "adminpanel/item/newItem";
        }
        adminServiceImpl.createNewItem(item);
        return "redirect:/adminpanel/items";
    }

    @GetMapping("/activeOrders")
    public String checkActiveOrders(Model model) {
        model.addAttribute("activeOrders", adminServiceImpl.getActiveOrders());
        model.addAttribute("specialists", adminServiceImpl.getAllSpecialists());
        model.addAttribute("order", new Order());
        return "adminpanel/order/checkActiveOrders";
    }

}
