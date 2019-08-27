package pl.mschielmann.infoshare.currenda;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static pl.mschielmann.infoshare.currenda.Position.DEVELOPER;
import static pl.mschielmann.infoshare.currenda.Position.MANAGER;

public class App {
    public static void main(String[] args) {
        Employee employee1 = createEmployee(1L, "Jane", "Doe",
                LocalDate.of(1979, 11, 8), "79110811221", MANAGER);
        Employee employee2 = createEmployee(2L, "John", "Doe",
                LocalDate.of(1981, 2, 28), "81022822112", DEVELOPER);
        Employee employee3 = createEmployee(3L, "Ian", "Smith",
                LocalDate.of(1984, 06, 14), "84061433223", DEVELOPER);

        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.addEmployee(employee1);
        employeeDao.addEmployees(asList(employee2, employee3));

        employeeDao.updateLastName(employee1.getId(), "Kowalski");
        employeeDao.updatePosition(employee3.getId(), MANAGER);

        List<Employee> employees = employeeDao.getOrderedEmployees("last_name", true);
        System.out.println(employees);
        Optional<Employee> employee = employeeDao.getEmployee(employee3.getId());
        if (employee.isPresent()) {
            System.out.println(employee);
        } else {
            System.out.println("Employee not found");
        }

        employeeDao.deleteEmployee(employee3.getId());
        employee = employeeDao.getEmployee(employee3.getId());
        if (employee.isPresent()) {
            System.out.println(employee);
        } else {
            System.out.println("Employee not found");
        }
    }

    private static Employee createEmployee(Long id, String firstName, String lastName,
                                           LocalDate dateOfBirth, String nationalId, Position position) {

        return new Employee(id, firstName, lastName, dateOfBirth, nationalId, position);
    }
}
