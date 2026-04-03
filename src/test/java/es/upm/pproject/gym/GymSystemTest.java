package es.upm.pproject.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GymSystemTest {

    private GymSystemApi gymSystem;

    @BeforeEach
    void setUp() {
        gymSystem = new GymSystem();
    }

    @Test
    void registerClassStoresRegisteredClass() {
        gymSystem.registerClass("Yoga", "Alice");

        List<GymClass> classes = gymSystem.getAllRegisteredClasses();

        assertEquals(1, classes.size());
        assertEquals("Yoga", classes.get(0).getName());
        assertEquals("Alice", classes.get(0).getTrainer());
    }

    @Test
    void registerClassRejectsNullOrBlankName() {
        assertThrows(GymSystemException.class, () -> gymSystem.registerClass(null, "Alice"));
        assertThrows(GymSystemException.class, () -> gymSystem.registerClass(" ", "Alice"));
    }

    @Test
    void registerClassRejectsNullOrBlankTrainer() {
        assertThrows(GymSystemException.class, () -> gymSystem.registerClass("Yoga", null));
        assertThrows(GymSystemException.class, () -> gymSystem.registerClass("Yoga", " "));
    }

    @Test
    void registerClassRejectsDuplicateName() {
        gymSystem.registerClass("Yoga", "Alice");

        assertThrows(GymSystemException.class, () -> gymSystem.registerClass("Yoga", "Bob"));
    }

    @Test
    void registerPersonStoresRegisteredPerson() {
        gymSystem.registerPerson(1, "Laura", "laura@example.com");

        List<Person> people = gymSystem.getAllRegisteredPeople();

        assertEquals(1, people.size());
        assertEquals(1, people.get(0).getIdentificationNumber());
        assertEquals("Laura", people.get(0).getName());
        assertEquals("laura@example.com", people.get(0).getEmail());
    }

    @Test
    void registerPersonRejectsNullIdentificationNumber() {
        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(null, "Laura", "laura@example.com"));
    }

    @Test
    void registerPersonRejectsNullOrBlankName() {
        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(1, null, "laura@example.com"));
        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(1, " ", "laura@example.com"));
    }

    @Test
    void registerPersonRejectsNullOrBlankEmail() {
        assertThrows(GymSystemException.class, () -> gymSystem.registerPerson(1, "Laura", null));
        assertThrows(GymSystemException.class, () -> gymSystem.registerPerson(1, "Laura", " "));
    }

    @Test
    void registerPersonRejectsInvalidEmailFormat() {
        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(1, "Laura", "laura.example.com"));
        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(1, "Laura", "laura@example.com."));
    }

    @Test
    void registerPersonRejectsDuplicateEmail() {
        gymSystem.registerPerson(1, "Laura", "laura@example.com");

        assertThrows(GymSystemException.class,
                () -> gymSystem.registerPerson(2, "Ana", "laura@example.com"));
    }

    @Test
    void enrollPersonInClassAddsPersonToClass() {
        registerBasePersonAndClass();

        gymSystem.enrollPersonInClass("laura@example.com", "Yoga");

        List<Person> peopleInClass = gymSystem.getPersonsInClass("Yoga");

        assertEquals(1, peopleInClass.size());
        assertEquals("laura@example.com", peopleInClass.get(0).getEmail());
    }

    @Test
    void enrollPersonInClassRejectsUnknownPerson() {
        gymSystem.registerClass("Yoga", "Alice");

        assertThrows(GymSystemException.class,
                () -> gymSystem.enrollPersonInClass("laura@example.com", "Yoga"));
    }

    @Test
    void enrollPersonInClassRejectsUnknownClass() {
        gymSystem.registerPerson(1, "Laura", "laura@example.com");

        assertThrows(GymSystemException.class,
                () -> gymSystem.enrollPersonInClass("laura@example.com", "Yoga"));
    }

    @Test
    void enrollPersonInClassRejectsWhenClassIsFull() {
        gymSystem.registerClass("Yoga", "Alice");
        IntStream.rangeClosed(1, 20).forEach(index -> {
            String email = "person" + index + "@example.com";
            gymSystem.registerPerson(index, "Person " + index, email);
            gymSystem.enrollPersonInClass(email, "Yoga");
        });
        gymSystem.registerPerson(21, "Overflow", "overflow@example.com");

        assertThrows(GymSystemException.class,
                () -> gymSystem.enrollPersonInClass("overflow@example.com", "Yoga"));
    }

    @Test
    void getPersonsInClassReturnsPeopleSortedByName() {
        gymSystem.registerClass("Yoga", "Alice");
        gymSystem.registerPerson(1, "Zoe", "zoe@example.com");
        gymSystem.registerPerson(2, "Ana", "ana@example.com");
        gymSystem.registerPerson(3, "Mario", "mario@example.com");
        gymSystem.enrollPersonInClass("zoe@example.com", "Yoga");
        gymSystem.enrollPersonInClass("ana@example.com", "Yoga");
        gymSystem.enrollPersonInClass("mario@example.com", "Yoga");

        List<Person> peopleInClass = gymSystem.getPersonsInClass("Yoga");

        assertEquals(List.of("Ana", "Mario", "Zoe"),
                peopleInClass.stream().map(Person::getName).collect(Collectors.toList()));
    }

    @Test
    void getPersonsInClassRejectsUnknownClass() {
        assertThrows(GymSystemException.class, () -> gymSystem.getPersonsInClass("Yoga"));
    }

    @Test
    void cancelEnrollmentRemovesPersonFromClass() {
        registerBasePersonAndClass();
        gymSystem.enrollPersonInClass("laura@example.com", "Yoga");

        gymSystem.cancelEnrollment("laura@example.com", "Yoga");

        assertEquals(0, gymSystem.getPersonsInClass("Yoga").size());
    }

    @Test
    void cancelEnrollmentRejectsUnknownPerson() {
        gymSystem.registerClass("Yoga", "Alice");

        assertThrows(GymSystemException.class,
                () -> gymSystem.cancelEnrollment("laura@example.com", "Yoga"));
    }

    @Test
    void cancelEnrollmentRejectsUnknownClass() {
        gymSystem.registerPerson(1, "Laura", "laura@example.com");

        assertThrows(GymSystemException.class,
                () -> gymSystem.cancelEnrollment("laura@example.com", "Yoga"));
    }

    @Test
    void cancelEnrollmentRejectsPersonNotEnrolledInClass() {
        registerBasePersonAndClass();

        assertThrows(GymSystemException.class,
                () -> gymSystem.cancelEnrollment("laura@example.com", "Yoga"));
    }

    @Test
    void restartClassRemovesAllEnrolledPeople() {
        gymSystem.registerClass("Yoga", "Alice");
        gymSystem.registerPerson(1, "Laura", "laura@example.com");
        gymSystem.registerPerson(2, "Mario", "mario@example.com");
        gymSystem.enrollPersonInClass("laura@example.com", "Yoga");
        gymSystem.enrollPersonInClass("mario@example.com", "Yoga");

        gymSystem.restartClass("Yoga");

        assertEquals(0, gymSystem.getPersonsInClass("Yoga").size());
    }

    @Test
    void restartClassRejectsUnknownClass() {
        assertThrows(GymSystemException.class, () -> gymSystem.restartClass("Yoga"));
    }

    @Test
    void getAllRegisteredPeopleReturnsPeopleSortedByEmail() {
        gymSystem.registerPerson(1, "Mario", "mario@example.com");
        gymSystem.registerPerson(2, "Laura", "laura@example.com");
        gymSystem.registerPerson(3, "Ana", "ana@example.com");

        List<Person> people = gymSystem.getAllRegisteredPeople();

        assertEquals(List.of("ana@example.com", "laura@example.com", "mario@example.com"),
                people.stream().map(Person::getEmail).collect(Collectors.toList()));
    }

    @Test
    void getAllRegisteredClassesReturnsClassesSortedByName() {
        gymSystem.registerClass("Zumba", "Mario");
        gymSystem.registerClass("Boxing", "Laura");
        gymSystem.registerClass("Aerobics", "Ana");

        List<GymClass> classes = gymSystem.getAllRegisteredClasses();

        assertEquals(List.of("Aerobics", "Boxing", "Zumba"),
                classes.stream().map(GymClass::getName).collect(Collectors.toList()));
    }

    private void registerBasePersonAndClass() {
        gymSystem.registerPerson(1, "Laura", "laura@example.com");
        gymSystem.registerClass("Yoga", "Alice");
    }
}
