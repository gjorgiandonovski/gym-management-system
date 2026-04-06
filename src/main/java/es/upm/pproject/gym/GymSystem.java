package es.upm.pproject.gym;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GymSystem implements GymSystemApi {
    private static final Logger logger = LoggerFactory.getLogger(GymSystem.class);
    private static final int MAX_ENROLLED_PER_CLASS = 20;

    private final Map<String, Person> peopleByEmail = new HashMap<>();
    private final Map<String, GymClass> classesByName = new HashMap<>();
    private final Map<String, Set<String>> enrollmentsByClassName = new HashMap<>();

    @Override
    public void registerClass(String className, String trainer) {
        validateText(className, "Class name cannot be null or blank.");
        validateText(trainer, "Trainer cannot be null or blank.");

        if (classesByName.containsKey(className)) {
            logger.warn("Class already exists: {}", className);
            throw new GymSystemException("Class already exists: " + className);
        }

        classesByName.put(className, new GymClass(className, trainer));
        enrollmentsByClassName.put(className, new HashSet<>());
        logger.info("Registered class '{}' with trainer '{}'", className, trainer);
    }

    @Override
    public void registerPerson(Integer identificationNumber, String name, String email) {
        if (identificationNumber == null) {
            logger.warn("Identification number cannot be null.");
            throw new GymSystemException("Identification number cannot be null.");
        }

        validateText(name, "Name cannot be null or blank.");
        validateText(email, "Email cannot be null or blank.");

        if (peopleByEmail.containsKey(email)) {
            logger.warn("Person already exists with email: {}", email);
            throw new GymSystemException("Person already exists with email: " + email);
        }

        peopleByEmail.put(email, new Person(identificationNumber, name, email));
        logger.info("Registered person '{}' with email '{}'", name, email);
    }

    @Override
    public void enrollPersonInClass(String email, String className) {
        validateRegisteredPerson(email);
        Set<String> enrolledEmails = validateRegisteredClass(className);

        if (enrolledEmails.contains(email)) {
            logger.warn("Person already enrolled in class '{}' for email '{}'", className, email);
            throw new GymSystemException("Person already enrolled in class: " + className);
        }

        if (enrolledEmails.size() >= MAX_ENROLLED_PER_CLASS) {
            logger.warn("Class is full: {}", className);
            throw new GymSystemException("Class is full: " + className);
        }

        enrolledEmails.add(email);
        logger.info("Enrolled '{}' in class '{}'", email, className);
    }

    @Override
    public List<Person> getPersonsInClass(String className) {
        Set<String> enrolledEmails = validateRegisteredClass(className);

        List<Person> result = new ArrayList<>();
        for (String email : enrolledEmails) {
            result.add(peopleByEmail.get(email));
        }

        result.sort(Comparator.comparing(Person::getName));
        logger.info("Retrieved {} enrolled people for class '{}'", result.size(), className);
        return result;
    }

    @Override
    public void cancelEnrollment(String email, String className) {
        validateRegisteredPerson(email);
        Set<String> enrolledEmails = validateRegisteredClass(className);

        if (!enrolledEmails.contains(email)) {
            logger.warn("Person is not enrolled in class '{}' for email '{}'", className, email);
            throw new GymSystemException("Person is not enrolled in class: " + className);
        }

        enrolledEmails.remove(email);
        logger.info("Cancelled enrollment of '{}' from class '{}'", email, className);
    }

    @Override
    public void restartClass(String className) {
        Set<String> enrolledEmails = validateRegisteredClass(className);
        enrolledEmails.clear();
        logger.info("Restarted class '{}' and cleared all enrollments", className);
    }

    @Override
    public List<Person> getAllRegisteredPeople() {
        List<Person> result = new ArrayList<>(peopleByEmail.values());
        result.sort(Comparator.comparing(Person::getEmail));
        logger.info("Retrieved {} registered people", result.size());
        return result;
    }

    @Override
    public List<GymClass> getAllRegisteredClasses() {
        List<GymClass> result = new ArrayList<>(classesByName.values());
        result.sort(Comparator.comparing(GymClass::getName));
        logger.info("Retrieved {} registered classes", result.size());
        return result;
    }

    private void validateRegisteredPerson(String email) {
        validateText(email, "Email cannot be null or blank.");
        if (!peopleByEmail.containsKey(email)) {
            logger.warn("Person not found with email: {}", email);
            throw new GymSystemException("Person not found with email: " + email);
        }
    }

    private Set<String> validateRegisteredClass(String className) {
        validateText(className, "Class name cannot be null or blank.");
        Set<String> enrolledEmails = enrollmentsByClassName.get(className);
        if (enrolledEmails == null) {
            logger.warn("Class not found: {}", className);
            throw new GymSystemException("Class not found: " + className);
        }
        return enrolledEmails;
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            logger.warn(message);
            throw new GymSystemException(message);
        }
    }
}
