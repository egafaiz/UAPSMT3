package tests.ui;

import org.junit.jupiter.api.*;
import ui.AddSchedulePage;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddSchedulePageTest {

    private DefaultTableModel tableModel;
    private AddSchedulePage addSchedulePage;

    @BeforeEach
    void setUp() {
        String[] columns = {"Item", "Status", "Priority", "Tanggal", "Attachment"};
        Object[][] data = {{null, null, null, null, null}};
        tableModel = new DefaultTableModel(data, columns);
        addSchedulePage = new AddSchedulePage("testuser");
    }

    @Test
    void testSaveSchedule() throws Exception {
    DefaultTableModel tableModel = addSchedulePage.getTableModel();
    tableModel.setValueAt("Meeting", 0, 0);
    tableModel.setValueAt("In Progress", 0, 1);
    tableModel.setValueAt("High", 0, 2);
    tableModel.setValueAt("2024-01-01", 0, 3);
    tableModel.setValueAt("notes.pdf", 0, 4);

    addSchedulePage.saveSchedule("testuser");

    File file = new File("data/testuser.txt");
    assertTrue(file.exists(), "File should exist after saving schedule");

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String savedData = reader.readLine();
    reader.close();

    assertEquals("Meeting,In Progress,High,2024-01-01,notes.pdf,", savedData,
            "Saved data does not match expected content");
}


    @AfterEach
    void tearDown() {
        File file = new File("data/testuser.txt");
        if (file.exists()) {
            assertTrue(file.delete(), "Temporary file should be deleted after the test");
        }
    }
}
