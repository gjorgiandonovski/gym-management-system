package es.upm.pproject.gym;

import java.util.*;

public class GymSystem implements GymSystemApi {
    private static final int MAX_ENROLLED_PER_CLASS = 20;

    private final Map<String, Person> peopleByEmail = new HashMap<>();
    private final Map<String, GymClass> classesByName = new HashMap<>();
    private final Map<String, Set<String>> enrollmentsByClassName = new HashMap<>();

    @Override
    public void registerClass(String className, String trainer) {
        validateText(className, "Class name cannot be null or blank.");
        validateText(trainer, "Trainer cannot be null or blank.");

        if (classesByName.containsKey(className)) {
            throw new GymSystemException("Class already exists: " + className);
        }

        classesByName.put(className, new GymClass(className, trainer));
        enrollmentsByClassName.put(className, new HashSet<>());
    }

    @Override
    public void registerPerson(Integer identificationNumber, String name, String email) {
        if (identificationNumber == null) {
            throw new GymSystemException("Identification number cannot be null.");
        }

        if (peopleByEmail.containsKey(email)) {
            throw new GymSystemException("Person already exists with email: " + email);
        }

        peopleByEmail.put(email, new Person(identificationNumber, name, email));
    }

    @Override
    public void enrollPersonInClass(String email, String className) {
        validateRegisteredPerson(email);
        Set<String> enrolledEmails = validateRegisteredClass(className);

        if (enrolledEmails.contains(email)) {
            throw new GymSystemException("Person already enrolled in class: " + className);
        }

        if (enrolledEmails.size() >= MAX_ENROLLED_PER_CLASS) {
            throw new GymSystemException("Class is full: " + className);
        }

        enrolledEmails.add(email);
    }

    @Override
    public List<Person> getPersonsInClass(String className) {
        Set<String> enrolledEmails = validateRegisteredClass(className);

        List<Person> result = new ArrayList<>();
        for (String email : enrolledEmails) {
            Person person = peopleByEmail.get(email);
            if (person != null) {
                result.add(person);
            }
        }

        result.sort(Comparator.comparing(Person::getName));
        return result;
    }

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