package ru.itis.project.company.service;

import ru.itis.project.company.models.Employee;
import ru.itis.project.company.models.SalesPoint;
import ru.itis.project.company.models.Warehouse;
import ru.itis.project.company.repository.EmployeeRepository;
import ru.itis.project.company.repository.SalesPointRepository;
import ru.itis.project.company.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenService {
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private SalesPointRepository salesPointRepository = new SalesPointRepository();
    private EmployeeRepository employeeRepository= new EmployeeRepository();
    private Scanner s = new Scanner(System.in);


    public void openNewSalesPoint(){
        System.out.println("Введите имя нового пункта продаж: ");
        String name = s.next();
        System.out.println("Введите к какому складу ее прикрепить: ");
        Warehouse warehouse = choiceWarehouse();
        if (warehouse == null) {
            System.out.println("Неправильный склад, попробуйте еще.");
        }
        int id = generateNewId();
        SalesPoint newSalesPoint = new SalesPoint(id,name,warehouse);
        salesPointRepository.save(newSalesPoint);

    }

    public Warehouse choiceWarehouse(){
        List<Warehouse> warehouses = warehouseRepository.findAll();
        int id = s.nextInt();
        Warehouse warehouse = warehouses.get(id - 1);
        if (warehouse != null){
            return warehouse;
        }
        return null;
    }

    public int generateNewId(){
        List<SalesPoint> salesPoints = salesPointRepository.findAll();
        return salesPoints.get(salesPoints.size() - 1).getId() + 1;
    }

    public void openNewWarehouse(){
        System.out.println("Введите название нового склада");
        String name = s.next();
        int warehouseId = generateNewWarehouseId();
        Warehouse warehouse = new Warehouse(warehouseId,name);
        warehouseRepository.save(warehouse);
        warehouseRepository.createNewCells(warehouse);

    }
    public int generateNewWarehouseId(){
        List<Warehouse> warehouses = warehouseRepository.findAll();
        return warehouses.get(warehouses.size() -1).getId() + 1;

    }


    public void addNewEmployee() {
        System.out.println("\n=== Добавление нового сотрудника ===");

        System.out.print("Введите ФИО сотрудника: ");
        String name = s.nextLine();

        String workPlaceType = selectWorkPlaceType();
        if (workPlaceType == null) return;

        int workplaceId = selectWorkplaceId(workPlaceType);
        if (workplaceId == -1) return;

        System.out.print("Введите должность сотрудника: ");
        String position = s.nextLine();

        Employee newEmployee = new Employee(0, name, workPlaceType, position, workplaceId);
        employeeRepository.save(newEmployee);

        System.out.println("Сотрудник успешно добавлен!");
    }

    private String selectWorkPlaceType() {
        while (true) {
            System.out.println("\nВыберите тип рабочего места:");
            System.out.println("1 - Склад");
            System.out.println("2 - Пункт продаж");
            System.out.print("Ваш выбор: ");

            String choice = s.nextLine();
            switch (choice) {
                case "1":
                    return "Склад";
                case "2":
                    return "Пункт продаж";
                default:
                    System.out.println("Некорректный ввод! Попробуйте снова.");
            }
        }
    }

    private int selectWorkplaceId(String workPlaceType) {
        List<Integer> availableIds = new ArrayList<>();

        if (workPlaceType.equals("Склад")) {
            System.out.println("\nДоступные склады:");
            for (var warehouse : warehouseRepository.findAll()) {
                System.out.println(warehouse.getId() + " - " + warehouse.getName());
                availableIds.add(warehouse.getId());
            }
        } else {
            System.out.println("\nДоступные пункты продаж:");
            for (var salesPoint : salesPointRepository.findAll()) {
                System.out.println(salesPoint.getId() + " - " + salesPoint.getName());
                availableIds.add(salesPoint.getId());
            }
        }

        while (true) {
            System.out.print("Введите ID места работы: ");
            try {
                int id = Integer.parseInt(s.nextLine());
                if (availableIds.contains(id)) {
                    return id;
                } else {
                    System.out.println("ID не найден! Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод! Введите число.");
            }
        }
    }
}


