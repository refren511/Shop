package com.example.shop;

import java.time.LocalDate;
import java.util.List;

public class Purchases {
    private Integer id;
    private String login;
    private LocalDate date;
    private List<Product> products;
    private String status;

    public Purchases(Integer id, String login, LocalDate date, List<Product> products, String status){
        this.id = id;
        this.login = login;
        this.date = date;
        this.products = products;
        this.status = status;
    }

    public Integer getId() { return id; }
    public String getLogin() { return login; }
    public LocalDate getDate() { return date; }
    public List<Product> getProducts() { return products; }
    public String getStatus() { return status; }

    public void setProducts(List<Product> products) { this.products = products; }
    public void setStatus(String status) { this.status = status; }
}
