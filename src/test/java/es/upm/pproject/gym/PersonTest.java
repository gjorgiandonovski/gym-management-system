package es.upm.pproject.gym;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void constructorRejectsNullIdentificationNumber() {
        assertThrows(GymSystemException.class,
                () -> new Person(null, "Laura", "laura@example.com"));
    }

    @Test
    void constructorRejectsNullOrBlankName() {
        assertThrows(GymSystemException.class,
                () -> new Person(1, null, "laura@example.com"));
        assertThrows(GymSystemException.class,
                () -> new Person(1, " ", "laura@example.com"));
    }

    @Test
    void constructorRejectsNullOrBlankEmail() {
        assertThrows(GymSystemException.class,
                () -> new Person(1, "Laura", null));
        assertThrows(GymSystemException.class,
                () -> new Person(1, "Laura", " "));
    }

    @Test
    void constructorRejectsInvalidEmailFormat() {
        assertThrows(GymSystemException.class,
                () -> new Person(1, "Laura", "laura.example.com"));
        assertThrows(GymSystemException.class,
                () -> new Person(1, "Laura", "laura@example.com."));
    }

    @Test
    void constructorAcceptsNonNullIdentificationNumber() {
        assertDoesNotThrow(() -> new Person(1, "Laura", "laura@example.com"));
    }
}
