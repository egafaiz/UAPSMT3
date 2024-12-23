package ui;

import controller.DateController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DetailSchedulePage {
    private JFrame frame;
    private DefaultTableModel tableModel;

    public DetailSchedulePage(String username, String[] scheduleData, int scheduleIndex) {
        frame = new JFrame("TRATUR - Detail Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(null);

        JButton backButton = new JButton(new ImageIcon(new ImageIcon("assets/back_arrow.png")
                .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        backButton.setBounds(20, 20, 30, 30);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> {
            frame.dispose();
            new MainPage(username);
        });
        frame.add(backButton);

        JLabel headerLabel = new JLabel("Schedule Details");
        headerLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        headerLabel.setForeground(new Color(11, 2, 101));
        headerLabel.setBounds(60, 20, 300, 40);
        frame.add(headerLabel);

        String[] columns = {"Item", "Status", "Priority", "Date", "Attachment"};
        Object[][] data = {scheduleData};

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3 || column == 4; 
            }
        };

        JTable scheduleTable = new JTable(tableModel);

        scheduleTable.setRowHeight(50);
        JTableHeader header = scheduleTable.getTableHeader();
        header.setFont(new Font("Montserrat", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(100, 40));
        header.setBackground(new Color(104, 93, 251));
        header.setForeground(Color.WHITE);

        TableColumnModel columnModel = scheduleTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); 
        columnModel.getColumn(1).setPreferredWidth(150); 
        columnModel.getColumn(2).setPreferredWidth(150); 
        columnModel.getColumn(3).setPreferredWidth(150); 
        columnModel.getColumn(4).setPreferredWidth(150); 

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Done", "Unfinished", "Stuck", "In Progress"});
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"High", "Normal", "Low"});

        scheduleTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(statusComboBox));
        scheduleTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(priorityComboBox));


        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = scheduleTable.columnAtPoint(evt.getPoint());
                if (column == 3) { 
                    String selectedDate = DateController.selectDate(frame);
                    if (selectedDate != null) {
                        scheduleTable.setValueAt(selectedDate, scheduleTable.getSelectedRow(), column);
                    }
                }
            }
        });

        scheduleTable.getColumnModel().getColumn(4).setCellEditor(new AttachmentCellEditor(frame, tableModel));

        scheduleTable.setFont(new Font("Montserrat", Font.PLAIN, 14));
        scheduleTable.setGridColor(new Color(200, 200, 200));

        JScrollPane tableScrollPane = new JScrollPane(scheduleTable);
        tableScrollPane.setBounds(20, 80, 1150, 500);
        frame.add(tableScrollPane);

        JButton editButton = new JButton("EDIT");
        editButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        editButton.setBackground(new Color(104, 93, 251));
        editButton.setForeground(Color.WHITE);
        editButton.setBounds(750, 600, 150, 40);
        editButton.setBorder(BorderFactory.createEmptyBorder());
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> saveSchedule(username, scheduleData, scheduleIndex));
        frame.add(editButton);

        JButton deleteButton = new JButton("DELETE");
        deleteButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        deleteButton.setBackground(new Color(255, 51, 51));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBounds(920, 600, 150, 40);
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteSchedule(username, scheduleIndex));
        frame.add(deleteButton);

        frame.setVisible(true);
    }

    private void saveSchedule(String username, String[] scheduleData, int scheduleIndex) {
        try {
            ArrayList<String[]> schedules = loadUserSchedules(username);
            schedules.set(scheduleIndex, new String[]{
                    (String) tableModel.getValueAt(0, 0),
                    (String) tableModel.getValueAt(0, 1), 
                    (String) tableModel.getValueAt(0, 2), 
                    (String) tableModel.getValueAt(0, 3), 
                    (String) tableModel.getValueAt(0, 4)
            });
            saveUserSchedules(schedules, username);
            JOptionPane.showMessageDialog(frame, "Schedule updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new MainPage(username);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to edit schedule!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSchedule(String username, int scheduleIndex) {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ArrayList<String[]> schedules = loadUserSchedules(username);
                schedules.remove(scheduleIndex);
                saveUserSchedules(schedules, username);
                JOptionPane.showMessageDialog(frame, "Schedule deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new MainPage(username);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to delete schedule!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private ArrayList<String[]> loadUserSchedules(String username) {
        ArrayList<String[]> schedules = new ArrayList<>();
        File userFile = new File("data/" + username + ".txt");
        if (userFile.exists()) {
            try (var reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    schedules.add(line.split(","));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return schedules;
    }

    private void saveUserSchedules(ArrayList<String[]> schedules, String username) {
        File userFile = new File("data/" + username + ".txt");
        try (var writer = new PrintWriter(userFile)) {
            for (String[] schedule : schedules) {
                writer.println(String.join(",", schedule));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
