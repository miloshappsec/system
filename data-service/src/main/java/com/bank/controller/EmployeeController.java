package com.bank.controller;

import com.bank.model.Employee;
import com.bank.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * List all employees including salaries.
     * VULNERABILITY: No authentication — sensitive data exposed to anyone (A01, A02).
     */
    @GetMapping
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    /**
     * Create an employee.
     * VULNERABILITY: Mass assignment — caller supplies all fields including salary (A04).
     * No authentication required.
     */
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Update any employee record by ID.
     * VULNERABILITY: IDOR + mass assignment — caller can update any employee's salary/role (A01, A04).
     * No auth check, no ownership validation.
     */
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee updated) {
        updated.setId(id);
        return employeeRepository.save(updated);
    }

    /**
     * Delete an employee.
     * VULNERABILITY: IDOR — no auth, anyone can delete any employee record.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
