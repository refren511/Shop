package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private TabPane tabPane;

    @FXML private TextField searchField;
    @FXML private TableView<Purchases> ordersTable;
    @FXML private TableColumn<Purchases, Integer> orderIdCol;
    @FXML private TableColumn<Purchases, String> orderLoginCol;

    @FXML private Label orderNumberLabel;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TableView<Product> orderProductsTable;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Integer> productCountCol;
    @FXML private TableColumn<Product, Double> productSumCol;
    @FXML private Label orderSumLabel;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Catalog.fxml"));
            tabPane.getTabs().get(0).setContent(loader.load());
        }catch (IOException e){
            e.printStackTrace();
        }

        PurchasesManager.loadOrders();
        PurchasesManager.generateTestData();

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        FilteredList<Purchases> filteredData = new FilteredList<>(PurchasesManager.getAllOrders(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(purchases -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(purchases.getId()).contains(lowerCaseFilter)) {
                    return true;
                }
                else if (purchases.getLogin().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Purchases> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ordersTable.comparatorProperty());
        ordersTable.setItems(sortedData);

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productCountCol.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        productSumCol.setCellValueFactory(new PropertyValueFactory<>("productSum"));

        statusCombo.setItems(FXCollections.observableArrayList("Оплачено", "Доставлено", "В обработке"));
        statusCombo.setDisable(true);

        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                orderNumberLabel.setText(String.valueOf(newVal.getId()));
                statusCombo.setValue(newVal.getStatus());
                statusCombo.setDisable(true);

                orderProductsTable.setItems(FXCollections.observableArrayList(newVal.getProducts()));

                double totalSum = 0;
                for (Product p : newVal.getProducts()) {
                    totalSum += p.getProductSum() * p.getProductCount();
                }
                orderSumLabel.setText(totalSum + " руб.");
            } else {
                orderNumberLabel.setText("-");
                statusCombo.setValue(null);
                orderProductsTable.getItems().clear();
                orderSumLabel.setText("0.0");
            }
        });
    }

    @FXML
    public void selectCatalogTab() {
        tabPane.getSelectionModel().select(0);
    }
    @FXML
    private void handleShowOrders(){
        tabPane.getSelectionModel().select(1);
    }

    @FXML
    private void openCatalogWindow(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Catalog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Каталог товаров");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText(null);
        alert.setContentText("Программа Учебная. версия 1.0.");
        alert.showAndWait();
    }

    @FXML
    private void handleEditOrder() {
        if (ordersTable.getSelectionModel().getSelectedItem() != null) {
            statusCombo.setDisable(false);
        }
    }

    @FXML
    private void handleSaveOrder() {
        Purchases selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected != null && statusCombo.getValue() != null) {
            selected.setStatus(statusCombo.getValue());
            PurchasesManager.saveOrders();
            statusCombo.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успех");
            alert.setContentText("Статус заказа сохранен!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelOrder() {
        Purchases selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            statusCombo.setValue(selected.getStatus());
            statusCombo.setDisable(true);
        }
    }

    @FXML
    private void handleShowStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ShowDiagram.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Статистика продаж");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            ShowDiagramController controller = loader.getController();
            controller.setProductData(PurchasesManager.getAllOrders());

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }
}
