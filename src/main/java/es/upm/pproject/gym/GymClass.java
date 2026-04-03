package es.upm.pproject.gym;

public class GymClass {
    private final String name;
    private final String trainer;

    public GymClass(String name, String trainer) {
        validateText(name, "Class name cannot be null or blank.");
        validateText(trainer, "Trainer cannot be null or blank.");

        this.name = name;
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public String getTrainer() {
        return trainer;
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new GymSystemException(message);
        }
    }
}
