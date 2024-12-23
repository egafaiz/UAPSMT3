package controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private static final String USER_DATA_FILE = "resources/users.txt";
    private static final String DATA_FOLDER = "data";
    private static Map<String, String> users = new HashMap<>();

    public static void loadUserData() {
        File userDataFile = new File(USER_DATA_FILE);

        if (!userDataFile.exists()) {
            try {
                if (!userDataFile.getParentFile().exists()) {
                    userDataFile.getParentFile().mkdirs();
                }
                userDataFile.createNewFile();
                System.out.println("File users.txt berhasil dibuat di folder resources.");
            } catch (IOException e) {
                System.err.println("Gagal membuat file users.txt: " + e.getMessage());
            }
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
            String line;
            users.clear(); 
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
            System.out.println("Data pengguna berhasil dimuat dari file users.txt.");
        } catch (IOException e) {
            System.err.println("Gagal membaca file users.txt: " + e.getMessage());
        }
    }

    public static void saveUserData() {
        System.out.println("Menyimpan data pengguna ke: " + USER_DATA_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            System.out.println("Data pengguna berhasil disimpan ke file users.txt.");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data pengguna: " + e.getMessage());
        }
    }

    public static boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username sudah terdaftar: " + username);
            return false; 
        }
        users.put(username, password);
        saveUserData(); 
        createUserScheduleFile(username);
        return true;
    }

    public static boolean validateLogin(String username, String password) {
        loadUserData(); 
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private static void createUserScheduleFile(String username) {
        File userFile = new File(DATA_FOLDER + "/" + username + ".txt");
        try {
            if (!userFile.getParentFile().exists()) {
                userFile.getParentFile().mkdirs(); 
                System.out.println("Folder data berhasil dibuat.");
            }
            if (!userFile.exists()) {
                userFile.createNewFile();
                System.out.println("File jadwal untuk pengguna " + username + " berhasil dibuat.");
            }
        } catch (IOException e) {
            System.err.println("Gagal membuat file jadwal untuk pengguna: " + username);
        }
    }


    public static String loadUserSchedule(String username) {
        File userFile = new File(DATA_FOLDER + "/" + username + ".txt");
        if (!userFile.exists()) {
            return ""; 
        }
        StringBuilder schedule = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                schedule.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file jadwal untuk pengguna: " + username);
        }
        return schedule.toString();
    }

    public static void saveUserSchedule(String username, String scheduleData) {
        File userFile = new File(DATA_FOLDER + "/" + username + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            writer.write(scheduleData);
            System.out.println("Jadwal untuk pengguna " + username + " berhasil disimpan.");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan jadwal untuk pengguna: " + username);
        }
    }
}
