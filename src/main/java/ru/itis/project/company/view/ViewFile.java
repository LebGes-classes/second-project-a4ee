package ru.itis.project.company.view;

import ru.itis.project.company.models.SalesPoint;
import ru.itis.project.company.models.Warehouse;
import ru.itis.project.company.repository.FileStorage;
import ru.itis.project.company.repository.SalesPointRepository;
import ru.itis.project.company.repository.WarehouseCellRepository;
import ru.itis.project.company.repository.WarehouseRepository;

import java.util.List;

public class ViewFile {
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private SalesPointRepository salesPointRepository = new SalesPointRepository();
    private WarehouseCellRepository warehouseCellRepository = new WarehouseCellRepository();
    public void viewOrderedProducts() {
        System.out.println("\n=== ЗАКАЗАННЫЕ ТОВАРЫ ===");
        List<String[]> orders = FileStorage.readFile("src/main/resources/order.txt");

        if (orders.isEmpty()) {
            System.out.println("У вас нет заказанных товаров");
            return;
        }

        System.out.println("Пункт\tТовар\tКоличество\tСумма");
        for (String[] order : orders) {
            if (order.length >= 4) {
                try {
                    int pointId = Integer.parseInt(order[0]);
                    String productName = order[1];
                    String totalPrice = order[2];
                    String quantity = order[3];

                    System.out.printf("%d\t%s\t%s\t%s руб.\n",
                            pointId, productName, quantity, totalPrice);
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка формата заказа: " + String.join(";", order));
                }
            }
        }
    }

    public void viewAllWarehouses() {
        System.out.println("\n=== ВСЕ СКЛАДЫ ===");
        List<Warehouse> warehouses = warehouseRepository.findAll();

        if (warehouses.isEmpty()) {
            System.out.println("Нет доступных складов");
            return;
        }

        System.out.println("ID\tНазвание\tКоличество товаров");
        for (Warehouse warehouse : warehouses) {
            System.out.printf("%d\t%s\t\n",
                    warehouse.getId(),
                    warehouse.getName()
                    );
        }
    }

    public void viewAllSalesPoints() {
        System.out.println("\n=== ВСЕ ПУНКТЫ ПРОДАЖ ===");
        List<SalesPoint> points = salesPointRepository.findAll();

        if (points.isEmpty()) {
            System.out.println("Нет доступных пунктов продаж");
            return;
        }

        System.out.println("ID\tНазвание\tПривязанный склад");
        for (SalesPoint point : points) {
            System.out.printf("%d\t%s\t%s (ID: %d)\n",
                    point.getId(),
                    point.getName(),
                    point.getWarehouse().getName(),
                    point.getWarehouse().getId());
        }
    }
}
