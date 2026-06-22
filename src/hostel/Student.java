package hostel;

/**
 * Demonstrates ENCAPSULATION: Private fields with public getters and setters.
 */
public class Student {
    private String studentId;
    private String name;
    private String gender;

    public Student(String studentId, String name, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}