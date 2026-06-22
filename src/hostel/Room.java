package hostel;

import java.util.ArrayList;

/**
 * Demonstrates ABSTRACTION: Use of an abstract class.
 */
public abstract class Room {
    private String roomNumber;
    protected double baseFee;
    private int capacity;
    private ArrayList<Student> assignedStudents; // Demonstrates ARRAY OF OBJECTS HANDLING

    public Room(String roomNumber, double baseFee, int capacity) {
        this.roomNumber = roomNumber;
        this.baseFee = baseFee;
        this.capacity = capacity;
        this.assignedStudents = new ArrayList<>();
    }

    // Abstract method to be overridden (POLYMORPHISM)
    public abstract double calculateTotalFee(int durationMonths);

    public String getRoomNumber() { return roomNumber; }
    public double getBaseFee() { return baseFee; }
    public int getCapacity() { return capacity; }
    public ArrayList<Student> getAssignedStudents() { return assignedStudents; }

    public boolean isFull() {
        return assignedStudents.size() >= capacity;
    }

    public void addStudent(Student student) {
        if (!isFull()) {
            assignedStudents.add(student);
        }
    }

    public void removeStudent(String studentId) {
        assignedStudents.removeIf(s -> s.getStudentId().equals(studentId));
    }

    public abstract String getRoomType();
}