package ru.itis.project.company.service;

import ru.itis.project.company.models.Employee;
import ru.itis.project.company.repository.EmployeeRepository;
import ru.itis.project.company.repository.SalesPointRepository;
import ru.itis.project.company.repository.WarehouseRepository;

import java.util.Scanner;

public class CloseService {
    private SalesPointRepository salesPointRepository = new SalesPointRepository();
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private EmployeeRepository employeeRepository = new EmployeeRepository();
    private Scanner s = new Scanner(System.in);


    public void closeSalesPoint() {
        System.out.println("Введите id пункта продаж, которых вы хотите закрыть: ");
        int id = s.nextInt();
        salesPointRepository.delete(id);
    }

    public void closeWarehouse() {
        System.out.println("Введите id склада который хотите закрыть: ");
        int id = s.nextInt();
        salesPointRepository.updateSales(id);
        warehouseRepository.delete(id);
    }

    public void dismissEmployee() {
        System.out.println("\n=== Увольнение сотрудника ===");

        showAllEmployees();

        System.out.print("\nВведите ID сотрудника для увольнения: ");
        int employeeId = s.nextInt();
        s.nextLine(); // Очистка буфера


        boolean isRemoved = employeeRepository.delete(employeeId);
        if (isRemoved) {
            System.out.println("Сотрудник успешно уволен.");
        } else {
            System.out.println("Сотрудник с указанным ID не найден.");
        }
    }

    public void showAllEmployees(){
        System.out.println("Список все сотрудников");
        System.out.println("ID\tФИО\tМесто Работы\tДолжность");
        for (Employee employee: employeeRepository.findAll()){
            System.out.printf("%d\t%s\t%s\t%s%n",employee.getId(),employee.getName(),employee.getWorkPlace(),employee.getWorkPlaceId());
            }
    }
}