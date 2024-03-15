package ru.coxey.diplom.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.coxey.diplom.model.Employee;
import ru.coxey.diplom.service.impl.AdminServiceImpl;

@Component
public class EmployeeValidator implements Validator {

    private final AdminServiceImpl adminServiceImpl;

    public EmployeeValidator(AdminServiceImpl adminServiceImpl) {
        this.adminServiceImpl = adminServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;
        Employee employeeById = adminServiceImpl.getEmployeeByLogin(employee.getLogin());
        if (employeeById != null) {
            errors.rejectValue("login", "", "Пользователь с таким логином уже существует");
        }
    }
}
