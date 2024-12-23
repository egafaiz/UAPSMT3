package ui;

import controller.DateController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.EventObject;

public class AddSchedulePage {
    private JFrame frame;
    private DefaultTableModel tableModel;

    public AddSchedulePage(String username) {
        frame = new JFrame("TRATUR - Add Schedule");
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

        JLabel headerLabel = new JLabel("Add New Schedule");
        headerLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        headerLabel.setForeground(new Color(11, 2, 101));
        headerLabel.setBounds(60, 20, 300, 40);
        frame.add(headerLabel);

        String[] columns = {"Item", "Status", "Priority", "Tanggal", "Attachment"};
        Object[][] data = {{null, null, null, null, "Add"}};

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 4 || column == 4;
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
        columnModel.getColumn(0).setPreferredWidth(300);
        columnModel.getColumn(1).setPreferredWidth(150); 
        columnModel.getColumn(2).setPreferredWidth(150); 
        columnModel.getColumn(3).setPreferredWidth(200); 
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


        JButton saveButton = new JButton("SAVE");
        saveButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        saveButton.setBackground(new Color(104, 93, 251));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(1000, 600, 150, 40); 
        saveButton.setBorder(BorderFactory.createEmptyBorder());
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveSchedule(username));
        frame.add(saveButton);

        frame.setVisible(true);
    }

    public void saveSchedule(String username) {
        try {
            File userFile = new File("data/" + username + ".txt");
            if (!userFile.exists()) userFile.getParentFile().mkdirs();
    
            FileWriter writer = new FileWriter(userFile, true);
    
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    Object value = tableModel.getValueAt(i, j);
                    row.append(value != null ? value.toString() : "").append(",");
                }
                writer.write(row.toString() + "\n"); 
            }
    
            writer.close();
    
            JOptionPane.showMessageDialog(frame, "Schedule saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    
            frame.dispose();
            new MainPage(username);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to save schedule!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
}

 class AttachmentCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private final JFrame parentFrame;
    private final DefaultTableModel tableModel;
    private int currentRow;

    public AttachmentCellEditor(JFrame parentFrame, DefaultTableModel tableModel) {
        this.parentFrame = parentFrame;
        this.tableModel = tableModel;

        button = new JButton("Add");
        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an Attachment");
            int returnValue = fileChooser.showOpenDialog(parentFrame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                tableModel.setValueAt(selectedFile.getName(), currentRow, 4); 
                JOptionPane.showMessageDialog(
                        parentFrame,
                        "Attachment added successfully!\nFile: " + selectedFile.getName() + "\nPath: " + selectedFile.getAbsolutePath(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row; 
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Add";
}
}
