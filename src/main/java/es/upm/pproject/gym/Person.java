package es.upm.pproject.gym;

public class Person {
    private final Integer identificationNumber;
    private final String name;
    private final String email;

    public Person(Integer identificationNumber, String name, String email) {
        validateIdentificationNumber(identificationNumber);
        validateText(name, "Name cannot be null or blank.");
        validateEmail(email);

        this.identificationNumber = identificationNumber;
        this.name = name;
        this.email = email;
    }

    public Integer getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private void validateIdentificationNumber(Integer value) {
        if (value == null) {
            throw new GymSystemException("Identification number cannot be null.");
        }
    }

    private void validateEmail(String value) {
        validateText(value, "Email cannot be null or blank.");
        if (!value.contains("@") || value.endsWith(".")) {
            throw new GymSystemException("Invalid email format: " + value);
        }
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new GymSystemException(message);
        }
    }
}
