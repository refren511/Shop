package com.example.shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML private TextField textField1;
    @FXML private TextField textField2;
    @FXML private Label resultLabel;

    @FXML
    protected void handleButtonAction() {
        String login = textField1.getText();
        String password = textField2.getText();

        if (UserManager.authenticate(login, password)) {
            resultLabel.setText("Успешный вход!");
            resultLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Сообщение об ошибке");
            alert.setContentText("Проверьте введенные логин и пароль");
            alert.showAndWait();
        }
    }

    @FXML
    protected void handleCancel(){
        textField1.clear();
        textField2.clear();
        resultLabel.setText("");
    }

    @FXML
    protected void handleRegisterLink() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("register.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Регистрация");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
