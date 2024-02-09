package org.example.service;

import org.example.exception.EmployeeAlreadyAddedException;
import org.example.exception.EmployeeNotFoundException;
import org.example.exception.EmployeeStoragelsFullException;
import org.example.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    private final EmployeeService employeeService = new EmployeeService();

    @AfterEach
    public void afterEach() {
        Collection<Employee> copy = new ArrayList<>(employeeService.findAll());
        for (Employee employee : copy) {
            employeeService.remove(employee.getFirstName(), employee.getLastName());
        }
    }

    @BeforeEach
    public void beforeEach() {
        employeeService.add("Ivan", "Ivanov", 1, 10000);
        employeeService.add("Petr", "Petrov", 1, 20000);
        employeeService.add("Ivan", "Petrov", 2, 30000);
        employeeService.add("Petr", "Ivanov", 1, 40000);
    }

    @Test
    void addPositiveTest() {
        Employee expected = new Employee("Aleksey", "Alekseev", 3, 50000);

        employeeService.add("Aleksey", "Alekseev", 3, 50000);
        assertThatNoException().isThrownBy(() -> employeeService.find("Aleksey", "Alekseev"));
        assertThat(employeeService.find("Aleksey", "Alekseev")).isEqualTo(expected);
        assertThat(employeeService.findAll()).contains(expected);
    }

    @Test
    void addNegative1Test() {
        for (int i = 0; i < 6; i++) {
            employeeService.add("Aleksey" + i, "Alekseev" + i, 3, 50000);
        }
        assertThat(employeeService.findAll()).hasSize(10);

        assertThatExceptionOfType(EmployeeStoragelsFullException.class)
                .isThrownBy(() -> employeeService.add("Aleksey", "Alekseev", 3, 50000));
    }

    @Test
    void addNegative2Test() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10000);
        assertThat(employeeService.findAll()).contains(expected);

        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.add("Ivan", "Ivanov", 1, 10000));
    }

    @Test
    void removePositiveTest() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10000);
        assertThat(employeeService.findAll()).contains(expected);

        employeeService.remove("Ivan", "Ivanov");

        assertThat(employeeService.findAll()).doesNotContain(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Ivan", "Ivanov"));
    }

    @Test
    void removeNegativeTest() {
        Employee expected = new Employee("Andrey", "Andereev", 1, 10000);
        assertThat(employeeService.findAll()).doesNotContain(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class).isThrownBy(() -> employeeService.remove("Andrey", "Andereev"));
    }

    @Test
    void findPositiveTest() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10000);
        assertThat(employeeService.findAll()).contains(expected);
        assertThat(employeeService.find("Ivan", "Ivanov")).isEqualTo(expected);
    }

    @Test
    void findNegativeTest() {
        Employee employee = new Employee("Andrey", "Andereev", 1, 10000);
        assertThat(employeeService.findAll()).doesNotContain(employee);
        assertThatExceptionOfType(EmployeeNotFoundException.class).isThrownBy(() -> employeeService.find("Andrey", "Andereev"));
    }

    @Test
    void findAll() {
        assertThat(employeeService.findAll()).containsExactlyInAnyOrder(
                new Employee("Ivan", "Ivanov", 1, 10000),
                new Employee("Petr", "Petrov", 1, 20000),
                new Employee("Ivan", "Petrov", 2, 30000),
                new Employee("Petr", "Ivanov", 1, 40000)
        );
    }
}
