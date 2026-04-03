package es.upm.pproject.gym;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GymSystem implements GymSystemApi {
    private static final int MAX_ENROLLED_PER_CLASS = 20;

    private final Map<String, Person> peopleByEmail = new HashMap<>();
    private final Map<String, GymClass> classesByName = new HashMap<>();
    private final Map<String, Set<String>> enrollmentsByClassName = new HashMap<>();

    @Override
    public void cancelEnrollment(String email, String className) {
        validateRegisteredPerson(email);
        Set<String> enrolledEmails = validateRegisteredClass(className);

        if (!enrolledEmails.contains(email)) {
            throw new GymSystemException("Person is not enrolled in class: " + className);
        }

        enrolledEmails.remove(email);
    }

    @Override
    public void restartClass(String className) {
        Set<String> enrolledEmails = validateRegisteredClass(className);
        enrolledEmails.clear();
    }

    @Override
    public List<Person> getAllRegisteredPeople() {
        List<Person> result = new ArrayList<>(peopleByEmail.values());
        result.sort(Comparator.comparing(Person::getEmail));
        return result;
    }

    @Override
    public List<GymClass> getAllRegisteredClasses() {
        List<GymClass> result = new ArrayList<>(classesByName.values());
        result.sort(Comparator.comparing(GymClass::getName));
        return result;
    }

    private void validateRegisteredPerson(String email) {
        validateText(email, "Email cannot be null or blank.");
        if (!peopleByEmail.containsKey(email)) {
            throw new GymSystemException("Person not found with email: " + email);
        }
    }

    private Set<String> validateRegisteredClass(String className) {
        validateText(className, "Class name cannot be null or blank.");
        Set<String> enrolledEmails = enrollmentsByClassName.get(className);
        if (enrolledEmails == null) {
            throw new GymSystemException("Class not found: " + className);
        }
        return enrolledEmails;
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new GymSystemException(message);
        }
    }
}