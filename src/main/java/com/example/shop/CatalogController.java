package com.example.shop;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CatalogController implements Initializable {

    private final ObservableList<Product> productData = FXCollections.observableArrayList();

    @FXML private TableView<Product> catalogTable;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> productSumCol;
    @FXML private TableColumn<Product, Integer> productCountCol;

    @FXML private Label productIdLabel;
    @FXML private Label productNameLabel;
    @FXML private Label productSumLabel;
    @FXML private Label productCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadProductsFromFile("products.txt");

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productIdCol.setSortable(false);
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productNameCol.setSortable(false);
        productSumCol.setCellValueFactory(new PropertyValueFactory<>("productSum"));
        productSumCol.setSortable(false);
        productCountCol.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        productCountCol.setSortable(false);

        catalogTable.setItems(productData);
        showProductDetails(null);

        Platform.runLater(this::adjustEmptyRows);
        catalogTable.heightProperty().addListener((observable, oldValue, newValue) -> {
            adjustEmptyRows();
        });

        catalogTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );
    }

    private void adjustEmptyRows() {
        if (catalogTable.getHeight() == 0) return;

        double rowHeight = 24.0;
        int totalRowsNeeded = (int) (catalogTable.getHeight() / rowHeight) + 1;

        int realProducts = 0;
        for (Product p : productData) {
            if (p.getProductId() != null) realProducts++;
        }

        int emptyRowsNeeded = totalRowsNeeded - realProducts;
        if (emptyRowsNeeded < 0) emptyRowsNeeded = 0;

        int currentEmpty = 0;
        for (Product p : productData) {
            if (p.getProductId() == null) currentEmpty++;
        }

        if (emptyRowsNeeded > currentEmpty) {
            for (int i = 0; i < (emptyRowsNeeded - currentEmpty); i++) {
                productData.add(new Product(null, null, null, null));
            }
        }
        else if (emptyRowsNeeded < currentEmpty) {
            int toRemove = currentEmpty - emptyRowsNeeded;
            for (int i = productData.size() - 1; i >= 0 && toRemove > 0; i--) {
                if (productData.get(i).getProductId() == null) {
                    productData.remove(i);
                    toRemove--;
                }
            }
        }
    }

    private void loadProductsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");

                if (parts.length == 4) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int count = Integer.parseInt(parts[3]);

                        productData.add(new Product(id, name, price, count));
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка парсинга строки: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Не удалось прочитать файл " + fileName + ". Ошибка: " + e.getMessage());
            productData.add(new Product(1, "Chair", 1500.00, 2));
        }
    }

    private void showProductDetails(Product product) {
        if (product == null || product.getProductId() == null) {
            productIdLabel.setText("");
            productNameLabel.setText("");
            productSumLabel.setText("");
            productCountLabel.setText("");
        } else {
            productIdLabel.setText(product.getProductId().toString());
            productNameLabel.setText(product.getProductName());
            productSumLabel.setText(product.getProductSum().toString());
            productCountLabel.setText(product.getProductCount().toString());
        }
    }

    @FXML
    private void handleDeleteProduct(){
        Product selected = catalogTable.getSelectionModel().getSelectedItem();

        if (selected != null && selected.getProductId() != null) {
            productData.remove(selected);
            Product.changeFile(productData);
            showProductDetails(null);
            adjustEmptyRows();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Не выделено");
            alert.setHeaderText("Не выбран товар");
            alert.setContentText("Выберите товар в таблице");

            alert.showAndWait();
        }
    }

    public boolean showProductEditDialog(Product product){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EditSceneController.class.getResource("EditScene.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EditSceneController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleNewProduct() throws IOException{
        Product tempProduct = new Product();

        boolean okClicked = this.showProductEditDialog(tempProduct);
        if(okClicked){
            int insertIndex = productData.size();
            for(int i = 0; i < productData.size(); i++){
                if (productData.get(i).getProductId() == null){
                    insertIndex = i;
                    break;
                }
            }

            productData.add(insertIndex, tempProduct);
            Product.addProduct(tempProduct);
            catalogTable.getSelectionModel().select(tempProduct);
            adjustEmptyRows();
        }
    }

    @FXML
    private void handleEditProduct(){
        Product selectedProduct = catalogTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null && selectedProduct.getProductId() != null){
            boolean okClicked = showProductEditDialog(selectedProduct);
            if(okClicked){
                showProductDetails(selectedProduct);
                int selectedIndex = catalogTable.getSelectionModel().getSelectedIndex();
                productData.set(selectedIndex, selectedProduct);
                Product.changeFile(productData);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Нет выбранного продукта");
            alert.setContentText("Выберите продукт в таблице");

            alert.showAndWait();
        }
    }

    private <T extends Comparable<T>> int compareNullSafe(T v1, T v2) {
        if (v1 == null && v2 == null) return 0;
        if (v1 == null) return 1;
        if (v2 == null) return -1;
        return v1.compareTo(v2);
    }
}