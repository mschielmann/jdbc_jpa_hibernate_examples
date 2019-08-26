package pl.mschielmann.infoshare.currenda;

import java.time.LocalDate;

class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationalId;
    private Position position;

    Employee(Long id, String firstName, String lastName, LocalDate dateOfBirth, String nationalId, Position position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationalId = nationalId;
        this.position = position;
    }

    Long getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    String getNationalId() {
        return nationalId;
    }

    Position getPosition() {
        return position;
    }
}
