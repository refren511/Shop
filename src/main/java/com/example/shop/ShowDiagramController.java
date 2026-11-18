package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class ShowDiagramController implements Initializable {
    @FXML private BarChart<String, Integer> barChart;
    @FXML private CategoryAxis xAxis;

    private ObservableList<String> productNames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] products = getUniqueProductNames();
        productNames.addAll(Arrays.asList(products));
        xAxis.setCategories(productNames);
    }

    private String[] getUniqueProductNames() {
        Set<String> uniqueNames = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    uniqueNames.add(parts[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения products.txt: " + e.getMessage());
        }
        return uniqueNames.toArray(new String[0]);
    }

    public void setProductData(List<Purchases> orders) {
        int[] productCounter = new int[productNames.size()];

        for (Purchases o : orders) {
            for (Product p : o.getProducts()) {
                int index = productNames.indexOf(p.getProductName());
                if (index != -1) {
                    productCounter[index] += p.getProductCount();
                }
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Продажи");

        for (int i = 0; i < productCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(productNames.get(i), productCounter[i]));
        }

        barChart.getData().add(series);
    }
}