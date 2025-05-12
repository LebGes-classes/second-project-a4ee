package ru.itis.project.company.service;

import ru.itis.project.company.models.Product;
import ru.itis.project.company.models.SalesPoint;
import ru.itis.project.company.models.WarehouseCell;
import ru.itis.project.company.repository.*;

import java.util.*;

public class SellService {
    private ProductRepository productRepository = new ProductRepository();
    private List<Product> productList = productRepository.findAll();
    private SalesPointRepository salesPointRepository = new SalesPointRepository();
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private WarehouseCellRepository warehouseCellRepository = new WarehouseCellRepository();
    private OrderRepository orderRepository = new OrderRepository();
    private Scanner s = new Scanner(System.in);

    public void startSellService(){
        Product selectedProduct = choise();
        List<SalesPoint> points = findPointsWithProduct(selectedProduct);
        if (points.isEmpty()){
            System.out.println("Товара нет");
        }
        SalesPoint selectedPoint = chooseSalesPoint(points, selectedProduct);
        processQuantitySelection(selectedPoint,selectedProduct);


    }
    public void processOrder() {
        List<SalesPoint> points = salesPointRepository.findAll();
        points.forEach(p -> System.out.printf("%d. %s\n", p.getId(), p.getName()));

        System.out.print("Выберите ID пункта выдачи: ");
        int pointId = s.nextInt();
        SalesPoint point = salesPointRepository.findById(pointId);

        if (point != null) {
            List<String[]> orders = FileStorage.readFile("src/main/resources/order.txt");
            System.out.println("\nВаши заказы:");

            for (String[] order : orders) {
                if (order.length >= 4) {
                    try {
                        int currentPointId = Integer.parseInt(order[0]);
                        if (currentPointId == pointId) {
                            String productName = productRepository.findById(Integer.parseInt(order[1])).getName();
                            String totalPrice = order[2];
                            String quantity = order[3];

                            System.out.printf("%s: %s шт. на %s руб.\n",
                                    productName, quantity, totalPrice);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка формата в заказе: " + String.join(";", order));
                    }
                }
            }

            System.out.print("\nПодтвердить покупку? (да/нет): ");
            if (s.next().equalsIgnoreCase("да")) {
                completeOrderPurchase(point);
            }
        }
    }
    private Product choise(){
        System.out.println("Список продуктов на покупку, выберите товар");
        viewProduct();
        int id = s.nextInt();
        Product currentProduct = productRepository.findById(id);
        if (currentProduct == null){
            System.out.println("Неправильный айди товара");
            return null;
        }
        return currentProduct;
    }
    private List<SalesPoint> findPointsWithProduct(Product product) {
        List<SalesPoint> pointsWithProduct = new ArrayList<>();
        if (product == null) {
            return pointsWithProduct;
        }

        // 1. Находим все склады, где есть этот товар
        Set<Integer> warehouseIdsWithProduct = new HashSet<>();
        for (WarehouseCell cell : warehouseCellRepository.findAll()) {
            if (cell.getProduct() != null && cell.getProduct().getId() == product.getId()
                    && cell.getQuantity() > 0) {
                warehouseIdsWithProduct.add(cell.getWarehouseId());
            }
        }

        // 2. Находим пункты продаж, привязанные к этим складам
        for (SalesPoint point : salesPointRepository.findAll()) {
            if (point.getWarehouse() != null
                    && warehouseIdsWithProduct.contains(point.getWarehouse().getId())) {
                pointsWithProduct.add(point);
            }
        }

        return pointsWithProduct;
    }


    private SalesPoint chooseSalesPoint(List<SalesPoint> points, Product product) {

        List<SalesPoint> availablePoints = new ArrayList<>();
        Map<Integer, Integer> availableQuantities = new HashMap<>();

        for (SalesPoint point : points) {

            int quantity = getAvailableQuantity(point, product);
            if (quantity > 0) {
                availablePoints.add(point);
                availableQuantities.put(point.getId(), quantity);
            }
        }

        if (availablePoints.isEmpty()) {
            System.out.println("Товар отсутствует во всех пунктах выдачи");
            return null;
        }

        System.out.println("\n=== ПУНКТЫ ВЫДАЧИ С ТОВАРОМ '" + product.getName() + "' ===");
        System.out.printf("%-5s %-20s %-15s\n", "ID", "Название", "Доступное кол-во");

        for (SalesPoint point : availablePoints) {
            if (availableQuantities.get(point.getId()) > 0) {
                System.out.printf("%-5d %-20s %-15d\n",
                        point.getId(),
                        point.getName(),
                        availableQuantities.get(point.getId()));
            }
        }

        System.out.print("\nВыберите ID пункта выдачи: ");
        try {
            int pointId = s.nextInt();
            s.nextLine(); // Очистка буфера

            for (SalesPoint point : availablePoints) {
                if (point.getId() == pointId) {
                    return point;
                }
            }
            System.out.println("Пункт с указанным ID не найден среди доступных");
        } catch (Exception e) {
            System.out.println("Ошибка ввода! Пожалуйста, введите число.");
            s.nextLine();
        }
        return null;
    }
    private int getAvailableQuantity(SalesPoint point, Product product) {
        List<WarehouseCell> allCells = warehouseCellRepository.findAll();
        for (WarehouseCell cell : allCells) {
            if (cell.getProduct().getId() == product.getId() && cell.getProduct() != null && cell.getWarehouseId()== point.getWarehouse().getId()) {
                return cell.getQuantity();
            }
        }
        return 0;
    }
    private void processQuantitySelection(SalesPoint point, Product product) {
        int available = getAvailableQuantity(point, product);
        System.out.printf("\nМаксимально доступно: %d\n", available);
        System.out.print("Введите количество для покупки: ");
        try {
            int quantity = s.nextInt();
            if (quantity <= 0) {
                System.out.println("Количество должно быть положительным!");
                return;
            }
            if (quantity > available) {
                System.out.println("Недостаточно товара в выбранном пункте!");
                return;
            }
            completePurchase(point, product, quantity);
            System.out.println("Покупка успешно оформлена! Товары добавлены в корзину");

        } catch (Exception e) {
            System.out.println("Ошибка ввода!");
            s.nextLine();
        }
    }

    private void completePurchase(SalesPoint point, Product product, int quantity) {
        for (WarehouseCell cell : point.getWarehouse().getCells()) {
            if (cell.getProduct().getId() == product.getId() && cell.getWarehouseId() == point.getWarehouseId()) {
                cell.setQuantity(cell.getQuantity() - quantity);
                break;
            }
        }
        //warehouseCellRepository.save(point);
        orderRepository.save(point,product,quantity);

    }

    public void viewProduct(){
        for (Product p: productList){
            System.out.printf("%-5s%-10s%-15s\n",p.getId(),p.getName(),p.getPrice());
        }
    }
    public void completeOrderPurchase(SalesPoint point) {
        List<String[]> allOrders = FileStorage.readFile("src/main/resources/order.txt");
        List<String> ordersToKeep = new ArrayList<>();
        List<String> purchasedItems = new ArrayList<>();

        int pointId = point.getId();
        for (String[] order : allOrders) {
            if (order.length >= 4) {
                if (Integer.parseInt(order[0]) == pointId) {
                    Product product = productRepository.findById(Integer.parseInt(order[1]));
                    purchasedItems.add(String.format("%s - %s шт. на сумму %s руб.",
                            product.getName(), order[3], order[2]));
                    productRepository.save(product);
                } else {
                    ordersToKeep.add(String.join(";", order));
                }
            }
        }
        FileStorage.writeFile("src/main/resources/order.txt", ordersToKeep);

        if (!purchasedItems.isEmpty()) {
            logPurchase(point, purchasedItems);
            System.out.println("Заказ успешно выполнен!");
            System.out.println("Купленные товары:");
            purchasedItems.forEach(System.out::println);
        } else {
            System.out.println("Нет заказов для этого пункта");
        }
    }

    private void logPurchase(SalesPoint point, List<String> items) {
        String logEntry = String.format("%s|Пункт %d (%s)|%s",
                java.time.LocalDateTime.now(),
                point.getId(),
                point.getName(),
                String.join(", ", items));

        try {
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("purchase_log.txt"),
                    (logEntry + System.lineSeparator()).getBytes(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );
        } catch (java.io.IOException e) {
            System.err.println("Ошибка записи лога: " + e.getMessage());
        }
    }





}



