package src.entities;

public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private int age;
    private String condition;

    // Constructor matching FileHandler or DB usage
    public Patient(int patientId, String firstName, String lastName, int age, String condition) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.condition = condition;
    }

    // Getters
    public int getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getCondition() {
        return condition;
    }

    // Setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Patient [ ID = " + patientId + ", First Name = " + firstName + ", Last Name = " + lastName + ", Age = " + age + ", Condition = " + condition + " ]";
    }
}
