package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class MainPage {
    private JFrame frame;
    private final String username;

    public MainPage(String username) {
        this.username = username;

        frame = new JFrame("TRATUR - Main Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(null);

        JLabel usernameLabel = new JLabel(username.toUpperCase());
        usernameLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        usernameLabel.setForeground(new Color(11, 2, 101));
        usernameLabel.setBounds(1000, 20, 200, 40);
        frame.add(usernameLabel);

        JLabel userIcon = new JLabel(new ImageIcon("assets/user_icon.png"));
        userIcon.setBounds(1150, 20, 40, 40);
        frame.add(userIcon);

        JButton createButton = new JButton("CREATE");
        createButton.setBounds(20, 20, 150, 40);
        createButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        createButton.setBackground(new Color(104, 93, 251));
        createButton.setForeground(Color.WHITE);
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.setFocusPainted(false);
        frame.add(createButton);

        JPanel todayPanel = createPanel("Today's Schedule", 20, 80, 720, 200);
        JPanel tomorrowPanel = createPanel("Tomorrow", 760, 80, 400, 200);
        JPanel thisMonthPanel = createPanel("This Month", 20, 300, 900, 450);
        JPanel urgentPanel = createPanel("Urgent", 940, 300, 200, 450);

        ArrayList<String[]> schedules = loadUserSchedules();

        LocalDate today = LocalDate.now();
        addSchedulesToTodayPanel(todayPanel, schedules, today.toString());
        addSchedulesToPanel(tomorrowPanel, schedules, today.plusDays(1).toString());
        addHighPrioritySchedulesToPanel(urgentPanel, schedules);
        addCalendarToPanel(thisMonthPanel, schedules, today);

        frame.add(todayPanel);
        frame.add(tomorrowPanel);
        frame.add(thisMonthPanel);
        frame.add(urgentPanel);

        createButton.addActionListener(e -> {
            frame.dispose();
            new AddSchedulePage(username);
        });

        frame.setVisible(true);
    }

    private JPanel createPanel(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(104, 93, 251), 1),
                title, 0, 0, new Font("Montserrat", Font.BOLD, 16), new Color(11, 2, 101)
        ));
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private void addSchedulesToTodayPanel(JPanel panel, ArrayList<String[]> schedules, String dateFilter) {
        panel.setLayout(new BorderLayout());

        LocalDate today = LocalDate.now();
        JLabel dateLabel = new JLabel(today.getDayOfWeek() + ", " + today.getDayOfMonth() + " " +
                today.getMonth().toString() + " " + today.getYear(), SwingConstants.LEFT);
        dateLabel.setFont(new Font("Montserrat", Font.BOLD, 16));
        dateLabel.setForeground(new Color(11, 2, 101));
        panel.add(dateLabel, BorderLayout.NORTH);

        String[] columnNames = {"Item", "Status", "Priority", "Attachment"};
        ArrayList<Object[]> rowData = new ArrayList<>();

        for (String[] schedule : schedules) {
            String date = schedule[3];
            if (date.equals(dateFilter)) {
                Object[] row = new Object[4];
                row[0] = schedule[0]; 
                row[1] = schedule[1]; 
                row[2] = schedule[2]; 

                if (schedule.length > 5 && schedule[4] != null && !schedule[4].isEmpty()) {
                    String attachment = schedule[4];
                    if (attachment.endsWith(".png") || attachment.endsWith(".jpg")) {
                        ImageIcon imageIcon = new ImageIcon(attachment);
                        JLabel imageLabel = new JLabel(new ImageIcon(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                        row[3] = imageLabel; // Image attachment
                    } else {
                        row[3] = "<html><a href='" + attachment + "'>Download File</a></html>"; 
                    }
                } else {
                    row[3] = "None";
                }
                rowData.add(row);
            }
        }

        if (rowData.isEmpty()) {
            JLabel noScheduleLabel = new JLabel("No schedule", SwingConstants.CENTER);
            noScheduleLabel.setFont(new Font("Montserrat", Font.PLAIN, 18));
            noScheduleLabel.setForeground(new Color(11, 2, 101));
            panel.add(noScheduleLabel, BorderLayout.CENTER);
        } else {
            Object[][] tableData = rowData.toArray(new Object[0][]);
            JTable scheduleTable = new JTable(tableData, columnNames) {
                @Override
                public Class<?> getColumnClass(int column) {
                    if (column == 3) {
                        return Object.class; 
                    }
                    return String.class;
                }
            };
            scheduleTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
            scheduleTable.setRowHeight(60);
            scheduleTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 12));

            JScrollPane scrollPane = new JScrollPane(scheduleTable);
            panel.add(scrollPane, BorderLayout.CENTER);
        }
    }

    private void addSchedulesToPanel(JPanel panel, ArrayList<String[]> schedules, String dateFilter) {
        panel.setLayout(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String[] schedule : schedules) {
            if (schedule[3].equals(dateFilter)) {
                listModel.addElement(schedule[0] + " - " + schedule[1] + " - " + schedule[2]);
            }
        }

        JList<String> scheduleList = new JList<>(listModel);
        scheduleList.setFont(new Font("Montserrat", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(scheduleList);
        panel.add(scrollPane, BorderLayout.CENTER);

        scheduleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = scheduleList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    new DetailSchedulePage(username, schedules.get(selectedIndex), selectedIndex);
                }
            }
        });
    }

    private void addHighPrioritySchedulesToPanel(JPanel panel, ArrayList<String[]> schedules) {
        panel.setLayout(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String[] schedule : schedules) {
            if ("High".equalsIgnoreCase(schedule[2])) {
                listModel.addElement(schedule[0] + " - Priority: High");
            }
        }

        JList<String> priorityList = new JList<>(listModel);
        priorityList.setFont(new Font("Montserrat", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(priorityList);
        panel.add(scrollPane, BorderLayout.CENTER);

        priorityList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = priorityList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    new DetailSchedulePage(username, schedules.get(selectedIndex), selectedIndex);
                }
            }
        });
    }

    private void addCalendarToPanel(JPanel panel, ArrayList<String[]> schedules, LocalDate currentDate) {
        JPanel daysPanel = new JPanel(new GridLayout(1, 7));
        JPanel calendarPanel = new JPanel(new GridLayout(5, 7));

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
            daysPanel.add(dayLabel);
        }

        panel.add(daysPanel, BorderLayout.NORTH);

        LocalDate firstDay = currentDate.withDayOfMonth(1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentDate.lengthOfMonth();

        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarPanel.add(new JLabel());
        }

        HashSet<Integer> scheduleDates = new HashSet<>();
        for (String[] schedule : schedules) {
            LocalDate scheduleDate = LocalDate.parse(schedule[3]);
            if (scheduleDate.getMonth() == currentDate.getMonth()) {
                scheduleDates.add(scheduleDate.getDayOfMonth());
            }
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int selectedDay = day;
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Montserrat", Font.PLAIN, 12));
            dayButton.setFocusPainted(false);

            if (scheduleDates.contains(day)) {
                dayButton.setBackground(new Color(104, 93, 251));
                dayButton.setForeground(Color.WHITE);
                dayButton.addActionListener(e -> {
                    ArrayList<String[]> daySchedules = new ArrayList<>();
                    for (String[] schedule : schedules) {
                        LocalDate scheduleDate = LocalDate.parse(schedule[3]);
                        if (scheduleDate.getDayOfMonth() == selectedDay && scheduleDate.getMonth() == currentDate.getMonth()) {
                            daySchedules.add(schedule);
                        }
                    }

                    JDialog dialog = new JDialog(frame, "Schedules for " + currentDate.withDayOfMonth(selectedDay));
                    dialog.setSize(600, 400);
                    dialog.setLayout(new BorderLayout());

                    String[] columnNames = {"Item", "Status", "Priority"};
                    Object[][] tableData = new Object[daySchedules.size()][3];

                    for (int i = 0; i < daySchedules.size(); i++) {
                        tableData[i][0] = daySchedules.get(i)[0];
                        tableData[i][1] = daySchedules.get(i)[1];
                        tableData[i][2] = daySchedules.get(i)[2];
                    }

                    JTable scheduleTable = new JTable(tableData, columnNames);
                    scheduleTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
                    scheduleTable.setRowHeight(30);
                    scheduleTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 12));

                    scheduleTable.getSelectionModel().addListSelectionListener(event -> {
                        if (!event.getValueIsAdjusting()) {
                            int selectedRow = scheduleTable.getSelectedRow();
                            if (selectedRow >= 0) {
                                dialog.dispose();
                                new DetailSchedulePage(username, daySchedules.get(selectedRow), selectedRow);
                            }
                        }
                    });

                    JScrollPane scrollPane = new JScrollPane(scheduleTable);
                    dialog.add(scrollPane, BorderLayout.CENTER);
                    dialog.setVisible(true);
                });
            } else {
                dayButton.setBackground(Color.WHITE);
                dayButton.setForeground(new Color(11, 2, 101));
            }
            calendarPanel.add(dayButton);
        }

        int totalCells = firstDayOfWeek + daysInMonth;
        int remainingCells = (7 - (totalCells % 7)) % 7;
        for (int i = 0; i < remainingCells; i++) {
            calendarPanel.add(new JLabel());
        }

        panel.add(calendarPanel, BorderLayout.CENTER);
    }

    private ArrayList<String[]> loadUserSchedules() {
        ArrayList<String[]> schedules = new ArrayList<>();
        File userFile = new File("data/" + username + ".txt");
        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
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
}
