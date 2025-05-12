package ru.itis.project.company.repository;


import ru.itis.project.company.models.Product;

import java.util.ArrayList;
import java.util.List;

public class PurchaseProductRepository {
    private static final String FILE_PATH = "src/main/resources/products_for_purchases.txt";
    private static final String PURCHASE_LOG_FILE = "src/main/resources/purchase_log.txt";

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        List<String[]> data = FileStorage.readFile(FILE_PATH);

        for (String[] parts : data) {
            if (parts.length >= 3) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                products.add(new Product(id, name, price));
            }
        }
        return products;
    }

    public Product findById(int id) {
        for (Product p : findAll()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    public void logPurchase(int productId, int quantity, double totalCost, int warehouseId) {
        String logEntry = String.format("%s|%d|%d|%.2f|%d",
                java.time.LocalDateTime.now().toString(),
                productId,
                quantity,
                totalCost,
                warehouseId);

        try {
            java.nio.file.Files.write(
                    java.nio.file.Paths.get(PURCHASE_LOG_FILE),
                    (logEntry + System.lineSeparator()).getBytes(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );
        } catch (java.io.IOException e) {
            System.err.println("Ошибка записи лога закупки: " + e.getMessage());
        }
    }
}