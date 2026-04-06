package es.upm.pproject.gym;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GymClassTest {

    @Test
    void constructorRejectsNullOrBlankName() {
        assertThrows(GymSystemException.class,
                () -> new GymClass(null, "Alice"));
        assertThrows(GymSystemException.class,
                () -> new GymClass(" ", "Alice"));
    }

    @Test
    void constructorRejectsNullOrBlankTrainer() {
        assertThrows(GymSystemException.class,
                () -> new GymClass("Yoga", null));
        assertThrows(GymSystemException.class,
                () -> new GymClass("Yoga", " "));
    }

    @Test
    void constructorAcceptsValidNameAndTrainer() {
        assertDoesNotThrow(() -> new GymClass("Yoga", "Alice"));
    }
}
