package com.example.shop;

import java.io.*;

public class UserManager {
    private static final String FILE_NAME = "users.txt";

    public static boolean isLoginExists(String login) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0 && parts[0].equals(login)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(user.getLogin() + ";" + user.getPassword() + ";" + user.getRole());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User authenticate(String login, String password) {
        if (login.equals("admin") && password.equals("admin")) {
            return new User("admin", "admin", "admin");
        }

        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2 && parts[0].equals(login) && parts[1].equals(password)) {
                    String role = (parts.length == 3) ? parts[2] : "client";
                    return new User(parts[0], parts[1], role);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}