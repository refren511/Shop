package com.example.shop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PurchasesManager {
    private static final String ORDERS_FILE = "orders.txt";
    private static final String ITEMS_FILE = "order_items.txt";
    private static final String PRODUCTS_FILE = "products.txt";

    private static final ObservableList<Purchases> allOrders = FXCollections.observableArrayList();

    public static ObservableList<Purchases> getAllOrders() {return allOrders;}

    public static Purchases findOrderById(int id) {
        for (Purchases p : allOrders) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static void loadOrders() {
        allOrders.clear();

        Map<Integer, Product> productMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    productMap.put(Integer.parseInt(parts[0]),
                            new Product(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3])));
                }
            }
        } catch (Exception e) { System.out.println("Ошибка products.txt: " + e.getMessage()); }

        File ordersFile = new File(ORDERS_FILE);
        if (ordersFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ordersFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 3) {
                        LocalDate date = LocalDate.parse(parts[2]);
                        String status = (parts.length > 3) ? parts[3] : "Оплачено";
                        allOrders.add(new Purchases(Integer.parseInt(parts[0]), parts[1], date, new ArrayList<>(), status));
                    }
                }
            } catch (Exception e) { System.out.println("Ошибка orders.txt: " + e.getMessage()); }
        }

        File itemsFile = new File(ITEMS_FILE);
        if (itemsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(itemsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        int orderId = Integer.parseInt(parts[0]);
                        Purchases order = findOrderById(orderId);

                        if (order != null) {
                            String[] itemsArray = parts[1].split("\\|");
                            for (String itemStr : itemsArray) {
                                String[] itemData = itemStr.split(",");
                                int prodId = Integer.parseInt(itemData[0]);
                                int qtyInOrder = Integer.parseInt(itemData[1]);

                                Product originalProduct = productMap.get(prodId);
                                if (originalProduct != null) {

                                    Product orderProduct = new Product(
                                            originalProduct.getProductId(),
                                            originalProduct.getProductName(),
                                            originalProduct.getProductSum(),
                                            qtyInOrder
                                    );
                                    order.getProducts().add(orderProduct);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) { System.out.println("Ошибка order_items.txt: " + e.getMessage()); }
        }
    }

    public static void saveOrders() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Purchases p : allOrders) {
                writer.println(p.getId() + ";" + p.getLogin() + ";" + p.getDate().toString() + ";" + p.getStatus());
            }
        } catch (Exception e) { e.printStackTrace(); }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ITEMS_FILE))) {
            for (Purchases p : allOrders) {
                if (p.getProducts().isEmpty()) continue;

                StringBuilder sb = new StringBuilder();
                sb.append(p.getId()).append(";");

                for (int i = 0; i < p.getProducts().size(); i++) {
                    Product prod = p.getProducts().get(i);
                    sb.append(prod.getProductId()).append(",").append(prod.getProductCount());
                    if (i < p.getProducts().size() - 1) sb.append("|");
                }
                writer.println(sb);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void generateTestData() {
        if (!allOrders.isEmpty()) return;

        Product p1 = new Product(1, "Chair", 1500.0, 2);
        Product p2 = new Product(2, "Table", 3000.0, 1);

        allOrders.add(new Purchases(101, "client1", LocalDate.of(2026, 11, 13), new ArrayList<>(Arrays.asList(p1, p2)), "Оплачено"));
        allOrders.add(new Purchases(102, "client2", LocalDate.of(2026, 11, 1), new ArrayList<>(Collections.singletonList(p1)), "Доставлено"));

        saveOrders();
        System.out.println("Сгенерированы тестовые заказы!");
    }
}
