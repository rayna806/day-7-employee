package com.example.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;

    }

    @GetMapping("{id}")
    public Employee get(@PathVariable int id) {
        for (Employee e : employees) {
            if (e.id().equals(id)) {
                return e;
            }
        }
        return null;
        //return employees.stream().filter(employee -> employee.id() == id).findFirst().orElse(null);
    }

    @GetMapping
    public List<Employee> index(@RequestParam(required = false) String gender) {
        List<Employee> result = new ArrayList<>();
        if (gender == null) {
            return employees;
        }
        for (Employee e : employees) {
            if (e.gender().equals(gender)) {
                result.add(e);
            }
        }
        return result;
    }

    @PutMapping("{id}")
    public Employee update(@PathVariable int id, @RequestBody Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id().equals(id)) {
                Employee updatedEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
                employees.set(i, updatedEmployee);
                return updatedEmployee;
            }
        }
        return null;
    }
}

