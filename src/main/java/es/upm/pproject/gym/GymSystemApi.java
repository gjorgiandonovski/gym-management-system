package es.upm.pproject.gym;

import java.util.List;

public interface GymSystemApi {
    void registerClass(String className, String trainer);

    void registerPerson(Integer identificationNumber, String name, String email);

    void enrollPersonInClass(String email, String className);

    List<Person> getPersonsInClass(String className);

    void cancelEnrollment(String email, String className);

    void restartClass(String className);

    List<Person> getAllRegisteredPeople();

    List<GymClass> getAllRegisteredClasses();
}
