package com.example.shop;

import javafx.fxml.FXML;
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

        User user = UserManager.authenticate(login, password);

        if (user != null) {
            if ("admin".equals(user.getRole())) {
                openWindow("MainMenu.fxml", "Shop - Администратор", 800, 600);
            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Доступ");
                alert.setHeaderText("Вы вошли как Клиент");
                alert.setContentText("Меню доступно только администратору. Открываем каталог...");
                alert.showAndWait();
                openWindow("Catalog.fxml", "Shop - Каталог", 600, 400);
            }
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверный логин или пароль");
            alert.showAndWait();
        }
    }

    private void openWindow(String fxmlFile, String title, int width, int height) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/shop/" + fxmlFile));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) textField1.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
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
