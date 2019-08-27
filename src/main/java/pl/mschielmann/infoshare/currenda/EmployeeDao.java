package pl.mschielmann.infoshare.currenda;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

class EmployeeDao {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "my-secret-pw";
    private static final String INSERT_QUERY = "INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_POSITION_QUERY = "UPDATE employee SET position = ? WHERE id = ?";
    private static final String UPDATE_LAST_NAME_QUERY = "UPDATE employee SET last_name = ? WHERE id = ?";
    private static final String SELECT_ALL_ORDERED_BY_DESC_QUERY = "SELECT * FROM employee ORDER BY ? DESC";
    private static final String SELECT_ALL_ORDERED_BY_ASC_QUERY = "SELECT * FROM employee ORDER BY ? ASC";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM employee WHERE ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM employee WHERE ID = ?";

    void addEmployee(Employee employee) {
        System.out.println("Adding employee");
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            writeEmployeeToDb(statement, employee);
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Employee added");
    }

    void addEmployees(List<Employee> employees) {
        System.out.println(format("Adding %s employees", employees.size()));
        int numberOfAdded = 0;
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            for (Employee employee : employees) {
                writeEmployeeToDb(statement, employee);
            }
            numberOfAdded++;
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println(format("Added %s employees", numberOfAdded));
    }

    void updatePosition(Long id, Position position) {
        System.out.println(format("Altering position for employee with id: %s", id));
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPDATE_POSITION_QUERY)) {
            statement.setLong(2, id);
            statement.setString(1, position.name());
            int result = statement.executeUpdate();
            System.out.println(result + " rows modified.");
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void updateLastName(Long id, String lastName) {
        System.out.println(format("Altering last name for employee with id: %s", id));
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPDATE_LAST_NAME_QUERY)) {
            statement.setLong(2, id);
            statement.setString(1, lastName);
            int result = statement.executeUpdate();
            System.out.println(result + " rows modified.");
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    List<Employee> getOrderedEmployees(String field, boolean reversed) {
        String query = reversed ? SELECT_ALL_ORDERED_BY_DESC_QUERY : SELECT_ALL_ORDERED_BY_ASC_QUERY;
        System.out.println(format("Getting all employees ordered by: %s, reversed: %s", field, reversed));
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, field);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getString(5),
                        Position.valueOf(resultSet.getString(6)));
                employees.add(employee);
            }
            System.out.println(employees.size() + " rows returned.");
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return employees;
    }

    Optional<Employee> getEmployee(Long id) {
        System.out.println(format("Getting employee by id: %s", id));
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Employee employee = new Employee(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getString(5),
                        Position.valueOf(resultSet.getString(6)));
                return Optional.of(employee);
            }
            System.out.println("Employee with specified id not found.");
            return Optional.empty();
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void deleteEmployee(Long id) {
        System.out.println(format("Altering last name for employee with id: %s", id));
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            System.out.println(result + " rows deleted.");
        } catch (SQLException e) {
            System.out.println("Error while adding a row: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void writeEmployeeToDb(PreparedStatement statement, Employee employee) throws SQLException {
        statement.setLong(1, employee.getId());
        statement.setString(2, employee.getFirstName());
        statement.setString(3, employee.getLastName());
        statement.setDate(4, Date.valueOf(employee.getDateOfBirth()));
        statement.setString(5, employee.getNationalId());
        statement.setString(6, employee.getPosition().toString());
        statement.executeUpdate();
        System.out.println("Row added.");
    }
}
