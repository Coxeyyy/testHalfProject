package ru.coxey.diplom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.coxey.diplom.model.Customer;
import ru.coxey.diplom.service.impl.RegistrationServiceImpl;
import ru.coxey.diplom.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationServiceImpl registrationServiceImpl;

    public AuthController(PersonValidator personValidator, RegistrationServiceImpl registrationServiceImpl) {
        this.personValidator = personValidator;
        this.registrationServiceImpl = registrationServiceImpl;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("customer") Customer customer) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("customer") @Valid Customer customer,
                                      BindingResult bindingResult) {
        personValidator.validate(customer, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationServiceImpl.registerCustomer(customer);
        return "redirect:/auth/login";
    }
}
