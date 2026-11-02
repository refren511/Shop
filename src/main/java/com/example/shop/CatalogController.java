package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
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
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productSumCol.setCellValueFactory(new PropertyValueFactory<>("productSum"));
        productCountCol.setCellValueFactory(new PropertyValueFactory<>("productCount"));

        catalogTable.setItems(productData);
        showProductDetails(null);

        catalogTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );
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
        if (product != null) {
            productIdLabel.setText(product.getProductId().toString());
            productNameLabel.setText(product.getProductName());
            productSumLabel.setText(product.getProductSum().toString());
            productCountLabel.setText(product.getProductCount().toString());
        } else {
            productIdLabel.setText("");
            productNameLabel.setText("");
            productSumLabel.setText("");
            productCountLabel.setText("");
        }
    }
}