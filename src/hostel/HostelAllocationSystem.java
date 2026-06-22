package hostel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HostelAllocationSystem extends JFrame {
    private ArrayList<Room> roomList = new ArrayList<>(); // Array of Objects Handling [cite: 11]

    private JTextField txtStudentId, txtStudentName, txtRoomNum, txtBaseFee, txtDuration;
    private JComboBox<String> cmbGender, cmbRoomType;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public HostelAllocationSystem() {
        setTitle("Student Hostel Room Allocation System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeDefaultRooms();

        // --- TOP HEADER PANEL ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel lblTitle = new JLabel("Hostel Room Allocation Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- WEST INPUT FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Allocation Registration Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; txtStudentId = new JTextField(12); formPanel.add(txtStudentId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1; txtStudentName = new JTextField(12); formPanel.add(txtStudentName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        formPanel.add(cmbGender, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1; txtRoomNum = new JTextField(12); formPanel.add(txtRoomNum, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1; cmbRoomType = new JComboBox<>(new String[]{"Single Room (No AC)", "Single Room (AC)", "Shared Room (2 Pax)", "Shared Room (4 Pax)"});
        formPanel.add(cmbRoomType, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Base Fee (RM/mth):"), gbc);
        gbc.gridx = 1; txtBaseFee = new JTextField("400.0", 12); formPanel.add(txtBaseFee, gbc);

        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Duration (Months):"), gbc);
        gbc.gridx = 1; txtDuration = new JTextField("6", 12); formPanel.add(txtDuration, gbc);

        add(formPanel, BorderLayout.WEST);

        // --- CENTER TABLE DISPLAY PANEL ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Currently Assigned Allocation Records"));

        String[] columns = {"Student ID", "Name", "Gender", "Room No", "Room Type", "Base Fee (RM)", "Stay (Mths)", "Total Calc Fee (RM)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Make table read-only
        };
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // --- FIX 1: TABLE CLICK LISTENER ---
        // Automatically populates the input fields when you click a row in the table [cite: 7]
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtStudentId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtStudentName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    cmbGender.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                    txtRoomNum.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtBaseFee.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    txtDuration.setText(tableModel.getValueAt(selectedRow, 6).toString());

                    String typeStr = tableModel.getValueAt(selectedRow, 4).toString();
                    if (typeStr.contains("No AC")) cmbRoomType.setSelectedIndex(0);
                    else if (typeStr.contains("(AC)")) cmbRoomType.setSelectedIndex(1);
                    else if (typeStr.contains("2 Pax")) cmbRoomType.setSelectedIndex(2);
                    else if (typeStr.contains("4 Pax")) cmbRoomType.setSelectedIndex(3);
                }
            }
        });

        // --- SOUTH ACTION CONTROL BUTTONS PANEL ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAdd = new JButton("Add Record");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnSearch = new JButton("Search by Student ID");
        JButton btnRefresh = new JButton("Refresh Display");

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnSearch);
        actionPanel.add(btnRefresh);
        add(actionPanel, BorderLayout.SOUTH);

        // ==========================================
        // EVENT-DRIVEN ACTION LISTENERS
        // ==========================================

// ADD FEATURE
        btnAdd.addActionListener(e -> {
            try {
                String id = txtStudentId.getText().trim();
                String name = txtStudentName.getText().trim();
                String gender = cmbGender.getSelectedItem().toString();
                String roomNum = txtRoomNum.getText().trim();
                double baseFee = Double.parseDouble(txtBaseFee.getText().trim());

                if (id.isEmpty() || name.isEmpty() || roomNum.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill out all missing required data fields.");
                    return;
                }

                // ---  DUPLICATION ERROR HANDLING ---
                // Scans all rooms to see if this Student ID is already registered
                for (Room r : roomList) {
                    for (Student s : r.getAssignedStudents()) {
                        if (s.getStudentId().equalsIgnoreCase(id)) {
                            JOptionPane.showMessageDialog(null,
                                    "Error: A student with ID '" + id + "' is already registered in Room " + r.getRoomNumber() + ".\n" +
                                            "Duplicates are not allowed.",
                                    "Duplicate Record Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Stop execution immediately!
                        }
                    }
                }

                Student newStudent = new Student(id, name, gender);
                Room targetRoom = findOrCreateRoom(roomNum, cmbRoomType.getSelectedIndex(), baseFee);

                if (targetRoom.isFull()) {
                    JOptionPane.showMessageDialog(null, "Error: Selected Room " + roomNum + " is full.");
                    return;
                }

                targetRoom.addStudent(newStudent);
                refreshTableDisplay();
                clearInputFields();
                JOptionPane.showMessageDialog(null, "Allocation Record added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numerical formats for Fee or Duration.");
            }
        });

        // --- EDIT FEATURE ---
        btnEdit.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a record from the table first, modify the text fields, and click Edit Selected.");
                return;
            }

            // Get original keys from the table row before editing
            String originalStudentId = tableModel.getValueAt(selectedRow, 0).toString();

            try {
                String newId = txtStudentId.getText().trim();
                String newName = txtStudentName.getText().trim();
                String newGender = cmbGender.getSelectedItem().toString();
                String newRoomNum = txtRoomNum.getText().trim();
                double newBaseFee = Double.parseDouble(txtBaseFee.getText().trim());

                if (newId.isEmpty() || newName.isEmpty() || newRoomNum.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Input fields cannot be empty during an edit.");
                    return;
                }

                // 1. Locate the current room holding this student record
                Room oldRoom = null;
                Student existingStudent = null;
                for (Room r : roomList) {
                    for (Student s : r.getAssignedStudents()) {
                        if (s.getStudentId().equals(originalStudentId)) {
                            oldRoom = r;
                            existingStudent = s;
                            break;
                        }
                    }
                }

                if (existingStudent == null) {
                    JOptionPane.showMessageDialog(null, "Original student record could not be found.");
                    return;
                }

                // 2. Fetch or create the newly requested destination room
                Room targetRoom = findOrCreateRoom(newRoomNum, cmbRoomType.getSelectedIndex(), newBaseFee);

                // 3. Handle changing rooms vs updating details within the same room
                if (oldRoom != targetRoom) {
                    // Check if new room has space
                    if (targetRoom.isFull()) {
                        JOptionPane.showMessageDialog(null, "Cannot move student. Room " + newRoomNum + " is at full capacity.");
                        return;
                    }
                    // Remove from old room, apply updates, insert into new room
                    oldRoom.removeStudent(originalStudentId);
                    existingStudent.setStudentId(newId);
                    existingStudent.setName(newName);
                    existingStudent.setGender(newGender);
                    targetRoom.addStudent(existingStudent);
                } else {
                    // Updating parameters in place within the same room
                    existingStudent.setStudentId(newId);
                    existingStudent.setName(newName);
                    existingStudent.setGender(newGender);
                }

                refreshTableDisplay();
                clearInputFields();
                JOptionPane.showMessageDialog(null, "Record updated and saved successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid numeric input for Base Fee or Duration.");
            }
        });

        // DELETE FEATURE
        btnDelete.addActionListener(e -> {
            int selectedRow = dataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Select a specific row entry from the table to delete.");
                return;
            }

            String targetStudentId = tableModel.getValueAt(selectedRow, 0).toString();
            for (Room r : roomList) {
                r.removeStudent(targetStudentId);
            }
            refreshTableDisplay();
            clearInputFields();
            JOptionPane.showMessageDialog(null, "Allocation entry removed cleanly.");
        });

        // SEARCH FEATURE
        btnSearch.addActionListener(e -> {
            String searchId = JOptionPane.showInputDialog("Enter Student ID to find details:");
            if (searchId == null || searchId.trim().isEmpty()) return;

            searchId = searchId.trim();
            for (Room r : roomList) {
                for (Student s : r.getAssignedStudents()) {
                    if (s.getStudentId().equalsIgnoreCase(searchId)) {
                        int trackingDuration = Integer.parseInt(txtDuration.getText().trim());
                        JOptionPane.showMessageDialog(null, "--- MATCH FOUND ---\n" +
                                "ID: " + s.getStudentId() + "\n" +
                                "Name: " + s.getName() + "\n" +
                                "Room: " + r.getRoomNumber() + " (" + r.getRoomType() + ")\n" +
                                "Total Fee Due: RM " + r.calculateTotalFee(trackingDuration)); // Calculation Method [cite: 12]
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "No room records tracked matching that identifier.");
        });

        btnRefresh.addActionListener(e -> refreshTableDisplay());
        refreshTableDisplay();
    }

    private void initializeDefaultRooms() {
        SingleRoom r1 = new SingleRoom("A-101", 500.0, true);
        SingleRoom r2 = new SingleRoom("A-102", 400.0, false);
        SharedRoom r3 = new SharedRoom("B-201", 300.0, 2);

        r1.addStudent(new Student("STU001", "Ariff Firdaus", "Male"));
        r3.addStudent(new Student("STU002", "Siti Aminah", "Female"));

        roomList.add(r1);
        roomList.add(r2);
        roomList.add(r3);
    }

    private Room findOrCreateRoom(String roomNum, int typeIndex, double baseFee) {
        for (Room r : roomList) {
            if (r.getRoomNumber().equalsIgnoreCase(roomNum)) {
                return r;
            }
        }
        Room newRoom;
        if (typeIndex == 0) newRoom = new SingleRoom(roomNum, baseFee, false);
        else if (typeIndex == 1) newRoom = new SingleRoom(roomNum, baseFee, true);
        else if (typeIndex == 2) newRoom = new SharedRoom(roomNum, baseFee, 2);
        else newRoom = new SharedRoom(roomNum, baseFee, 4);

        roomList.add(newRoom);
        return newRoom;
    }

    private void refreshTableDisplay() {
        tableModel.setRowCount(0);
        int trackingDuration = 6;
        try {
            trackingDuration = Integer.parseInt(txtDuration.getText().trim());
        } catch (NumberFormatException ignored) {}

        for (Room room : roomList) {
            for (Student student : room.getAssignedStudents()) {
                tableModel.addRow(new Object[]{
                        student.getStudentId(),
                        student.getName(),
                        student.getGender(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        String.format("%.2f", room.getBaseFee()),
                        trackingDuration,
                        String.format("%.2f", room.calculateTotalFee(trackingDuration))
                });
            }
        }
    }

    private void clearInputFields() {
        txtStudentId.setText("");
        txtStudentName.setText("");
        txtRoomNum.setText("");
        dataTable.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new HostelAllocationSystem().setVisible(true);
        });
    }
}