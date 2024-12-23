// File: src/main/java/ui/OnboardingPage.java

package ui;

import javax.swing.*;

import controller.UserController;

import java.awt.*;

public class OnboardingPage {

    public static void main(String[] args) {
        UserController.loadUserData();
        JFrame frame = new JFrame("Welcome to TRATUR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(null);

        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 700, 700); 
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(null);

        JLabel titleLabel = new JLabel("WELCOME TO TRATUR");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        titleLabel.setForeground(new Color(11, 2, 101));
        titleLabel.setBounds(100, 120, 500, 50); 
        leftPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));
        usernameLabel.setBounds(100, 200, 500, 20);
        usernameLabel.setForeground(new Color(11, 2, 101));
        leftPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(100, 230, 500, 50);
        usernameField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(104, 93, 251), 2));
        leftPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));
        passwordLabel.setBounds(100, 300, 500, 20);
        passwordLabel.setForeground(new Color(11, 2, 101));
        leftPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 330, 500, 50);
        passwordField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(104, 93, 251), 2));
        leftPanel.add(passwordField);

        JButton registerButton = new JButton("DAFTAR");
        registerButton.setBounds(100, 420, 220, 50);
        registerButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(11, 2, 101));
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(11, 2, 101), 2));
        leftPanel.add(registerButton);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(380, 420, 220, 50);
        loginButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        loginButton.setBackground(new Color(104, 93, 251));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        leftPanel.add(loginButton);

        frame.add(leftPanel);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username atau password tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!UserController.registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Username sudah terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(frame, "Registrasi berhasil.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username atau password tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (UserController.validateLogin(username, password)) {
                frame.dispose(); 
                new MainPage(username); 
            } else {
                JOptionPane.showMessageDialog(frame, "Username atau password salah.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(700, 0, 500, 700); 
        rightPanel.setBackground(new Color(11, 2, 101));
        rightPanel.setLayout(null);

        JLabel appName = new JLabel("TRATUR");
        appName.setFont(new Font("Montserrat", Font.BOLD, 28));
        appName.setForeground(Color.WHITE);
        appName.setBounds(190, 30, 200, 40);
        rightPanel.add(appName);

        JLabel appDescription = new JLabel("Schedule Reminder App");
        appDescription.setFont(new Font("Montserrat", Font.PLAIN, 16));
        appDescription.setForeground(Color.WHITE);
        appDescription.setBounds(160, 80, 300, 20); 
        rightPanel.add(appDescription);

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(
            new ImageIcon("assets/Group 12.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)
        ); 
        logoLabel.setIcon(logoIcon);
        logoLabel.setBounds(150, 150, 200, 200);
        rightPanel.add(logoLabel);

        JLabel powerTitle = new JLabel("OUR POWER");
        powerTitle.setFont(new Font("Montserrat", Font.BOLD, 20));
        powerTitle.setForeground(Color.WHITE);
        powerTitle.setBounds(100, 400, 200, 50); 
        rightPanel.add(powerTitle);

        JLabel powerDescription = new JLabel("<html>Tratur adalah sebuah software yang dapat membantu anda merencanakan masa depan.<br>Menjadikan anda mahasiswa yang lebih produktif.</html>");
        powerDescription.setFont(new Font("Montserrat", Font.PLAIN, 14));
        powerDescription.setForeground(Color.WHITE);
        powerDescription.setBounds(100, 440, 300, 100); 
        rightPanel.add(powerDescription);

        frame.add(rightPanel);

        frame.setVisible(true);
    }
}
