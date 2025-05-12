package ru.itis.project.company.service;

import ru.itis.project.company.models.Product;
import ru.itis.project.company.models.Warehouse;
import ru.itis.project.company.models.WarehouseCell;
import ru.itis.project.company.repository.ProductRepository;
import ru.itis.project.company.repository.PurchaseProductRepository;
import ru.itis.project.company.repository.WarehouseCellRepository;
import ru.itis.project.company.repository.WarehouseRepository;

import java.util.List;
import java.util.Scanner;

public class BuyService {
    private PurchaseProductRepository purchaseProductRepository = new PurchaseProductRepository();
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private ProductRepository productRepository = new ProductRepository();
    private WarehouseCellRepository warehouseCellRepository = new WarehouseCellRepository();
    private Scanner s = new Scanner(System.in);

    public void startProcces(){
        Product product = selectProduct();
        Warehouse warehouse = selectWarehouse();
        int quantity = selectQuantity();
        processPurchase(product,warehouse,quantity);
    }

    public Product selectProduct(){
        System.out.println("Товары для закупки");
        List<Product> productList = purchaseProductRepository.findAll();
        for (Product product: productList){
            System.out.printf("%d;%s;%.2f\n",
                    product.getId(),
                    product.getName(),
                    product.getPrice());
        }
        System.out.println("Выберите товар: ");
        int productId = s.nextInt();

        return purchaseProductRepository.findById(productId);
    }
    public int selectQuantity(){
        System.out.println("Выберите количество для закупки: ");
        int quantity = s.nextInt();
        return  quantity;

    }

    public Warehouse selectWarehouse(){
        System.out.println("Доступные склады");
        List<Warehouse> warehouses = warehouseRepository.findAll();
        for (Warehouse warehouse: warehouses){
            System.out.printf("%d. %s%n", warehouse.getId(),warehouse.getName());
        }
        System.out.print("Выберите ID склада: ");
        int id = s.nextInt();
        Warehouse warehouse = warehouseRepository.findById(id);
        if (warehouse != null){
            return warehouse;
        }
        System.out.println("Неправильный айди склада");
        return null;
    }
    private void processPurchase(Product purchaseProduct, Warehouse warehouse, int quanity){
        Product mainProduct = productRepository.findById(purchaseProduct.getId());
        if (mainProduct == null){
            mainProduct = new Product(purchaseProduct.getId(),purchaseProduct.getName(),purchaseProduct.getPrice() * 1.3);
            productRepository.save(mainProduct);
        }
        WarehouseCell exsitingCell = warehouseCellRepository.findCellWithProduct(warehouse.getId(),mainProduct.getId());
        if (exsitingCell != null){
            exsitingCell.setQuantity(exsitingCell.getQuantity() + quanity);
            warehouseCellRepository.save(exsitingCell);
        }
        else {
            WarehouseCell newCell = new WarehouseCell(warehouseCellRepository.findNextCellId(warehouse.getId()), warehouse.getId(),quanity,mainProduct);
            warehouseCellRepository.save(newCell);
        };

        double totalCost = purchaseProduct.getPrice() * quanity;
        purchaseProductRepository.logPurchase(purchaseProduct.getId(),quanity,totalCost,warehouse.getId());
        System.out.println("Успешно закуплено " + quanity + " " + purchaseProduct.getName());
        System.out.println("Общая стоимость покупки " + totalCost);
        System.out.println("Товар доставлен до склада " + warehouse.getName());
    }
}
