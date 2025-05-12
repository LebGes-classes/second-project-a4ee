package ru.itis.project.company.models;

public class WarehouseCell {
    private int id;
    private int warehouseId;
    private int quantity;
    private Product product;

    public WarehouseCell(int id, int warehouseId, int quantity, Product product) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
}
