package com.example.shop;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.awt.*;

public class RegisterController {

    @FXML private TextField regLoginField;
    @FXML private PasswordField regPasswordField;

    @FXML
    protected void handleSave(){
        String login = regLoginField.getText();
        String password = regPasswordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setContentText("Заполните все поля!");
            alert.showAndWait();
            return;
        }

        if (UserManager.isLoginExists(login)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка регистрации");
            alert.setHeaderText("Логин уже занят");
            alert.setContentText("Пользователь с логином '" + login + "' уже существует. Придумайте другой.");
            alert.showAndWait();
            return;
        }

        User newUser = new User(login, password);
        UserManager.saveUser(newUser);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setContentText("Пользователь " + login + " зарегистрирован!");
        alert.showAndWait();

        Stage stage = (Stage) regLoginField.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleBack() {
        Stage stage = (Stage) regLoginField.getScene().getWindow();
        stage.close();
    }
}
