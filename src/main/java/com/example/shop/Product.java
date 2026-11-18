package com.example.shop;

import javafx.collections.ObservableList;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Product {
    private Integer productId;
    private String productName;
    private Double productSum;
    private Integer productCount;

    public Product(){}

    public Product(Integer productId, String productName, Double productSum, Integer productCount){
        this.productId = productId;
        this.productName = productName;
        this.productSum = productSum;
        this.productCount = productCount;
    }

    public Integer getProductId() {return productId;}
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getProductSum() { return productSum; }
    public void setProductSum(Double productSum) { this.productSum = productSum; }

    public Integer getProductCount() { return productCount; }
    public void setProductCount(Integer productCount) { this.productCount = productCount; }

    public static void  changeFile(ObservableList<Product> changedList){
            try(PrintWriter writer = new PrintWriter(new FileWriter("products.txt"))){
                for (Product p : changedList){
                    if (p.getProductId() != null) {
                        writer.println(p.getProductId() + ";" + p.getProductName() + ";" + p.getProductSum() + ";"
                                + p.getProductCount());
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка сохранения: " + e.getMessage());
            }
    }

    public static void addProduct(Product product) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("products.txt", true))) {
            writer.println(product.getProductId() + ";" + product.getProductName() + ";"
                    + product.getProductSum() + ";" + product.getProductCount());
        } catch (Exception e) {
            System.out.println("Ошибка добавления в файл: " + e.getMessage());
        }
    }
}
