package com.example.shop;

public class Product {
    private Integer productId;
    private String productName;
    private Double productSum;
    private Integer productCount;

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

}
