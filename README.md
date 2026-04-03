# Gym Management System

## Overview

This project implements the classes and interfaces required to manage a gym system without any graphical user interface.
The system allows registering gym classes and people, enrolling people in classes, canceling enrollments, restarting classes,
and obtaining sorted lists of registered people and classes.

The main public contract of the project is the Java interface
[GymSystemApi](/Users/gjorgiandonovski/IdeaProjects/untitled34/proyect/src/main/java/es/upm/pproject/gym/GymSystemApi.java),
which defines all operations needed to interact with the system.

## Functional Description Of The Interface

The interface `GymSystemApi` provides the following operations:

### `void registerClass(String className, String trainer)`

Registers a new gym class with its name and trainer.

Validation rules:
- `className` cannot be `null` or blank.
- `trainer` cannot be `null` or blank.
- A class with the same name cannot already exist.

### `void registerPerson(Integer identificationNumber, String name, String email)`

Registers a new person in the gym.

Validation rules:
- `identificationNumber` cannot be `null`.
- `name` cannot be `null` or blank.
- `email` cannot be `null` or blank.
- `email` must contain `@`.
- `email` cannot end with `.`.
- A person with the same email cannot already exist.

### `void enrollPersonInClass(String email, String className)`

Enrolls a registered person in a registered class.

Validation rules:
- The person identified by `email` must already exist in the system.
- The class identified by `className` must already exist in the system.
- A class can contain at most 20 enrolled people.

### `List<Person> getPersonsInClass(String className)`

Returns the list of people enrolled in the given class.

Behavior:
- The class must exist.
- Each returned person includes identification number, name, and email.
- The resulting list is sorted alphabetically by person name.

### `void cancelEnrollment(String email, String className)`

Cancels the enrollment of a person in a class.

Validation rules:
- The person identified by `email` must be registered.
- The class identified by `className` must be registered.
- The person must be enrolled in that class.

### `void restartClass(String className)`

Removes all enrolled people from the given class.

Validation rules:
- The class must exist.

### `List<Person> getAllRegisteredPeople()`

Returns all registered people in the system.

Behavior:
- Each returned person includes identification number, name, and email.
- The resulting list is sorted by email.

### `List<GymClass> getAllRegisteredClasses()`

Returns all registered gym classes in the system.

Behavior:
- Each returned class includes its name and trainer.
- The resulting list is sorted by class name.

## Exception Handling

The project uses `GymSystemException` to report situations where the preconditions of an operation are not satisfied.
Examples include invalid input data, attempts to use unregistered people or classes, duplicate registrations, and enrollment
requests for a class that has already reached its maximum capacity.

## Main Classes

- `GymSystemApi`: public interface that defines the system operations.
- `GymSystem`: implementation of the interface and core business rules.
- `Person`: model class that stores identification number, name, and email.
- `GymClass`: model class that stores class name and trainer.
- `GymSystemException`: runtime exception used for validation and business rule violations.
