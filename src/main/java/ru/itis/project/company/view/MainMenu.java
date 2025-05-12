package ru.itis.project.company.view;

import ru.itis.project.company.service.*;

import java.util.Scanner;

public class MainMenu {
    ViewFile viewFile = new ViewFile();
    private final Scanner scanner;
    private final SellService sellService;
    private final OpenService openService;
    private final CloseService closeService;
    private final BuyService buyService;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.sellService = new SellService();
        this.openService = new OpenService();
        this.closeService = new CloseService();
        this.buyService = new BuyService();
    }

    public void start() {
        System.out.println("Добро пожаловать в систему управления магазином!");

        while (true) {
            System.out.println("\nВыберите вашу роль:");
            System.out.println("1. Покупатель");
            System.out.println("2. Владелец");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    ownerMenu();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    private void customerMenu() {
        while (true) {
            System.out.println("\nМЕНЮ ПОКУПАТЕЛЯ");
            System.out.println("1. Заказать товары");
            System.out.println("2. Подтвердить покупку");
            System.out.println("3. Просмотреть заказанные товары");
            System.out.println("0. Назад");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    sellService.startSellService();
                    break;
                case 2:
                    sellService.processOrder();
                    break;
                case 3:
                    viewFile.viewOrderedProducts();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    private void ownerMenu() {
        while (true) {
            System.out.println("\n=== МЕНЮ ВЛАДЕЛЬЦА ===");
            System.out.println("1. Управление складами");
            System.out.println("2. Управление пунктами продаж");
            System.out.println("3. Управление сотрудниками");
            System.out.println("4. Закупка товаров");
            System.out.println("5. Просмотреть все склады");
            System.out.println("6. Просмотреть все пункты продаж");
            System.out.println("7. Просмотреть всех работников");
            System.out.println("0. Назад");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageWarehouses();
                    break;
                case 2:
                    manageSalesPoints();
                    break;
                case 3:
                    manageEmployees();
                    break;
                case 4:
                    buyService.startProcces();
                    break;
                case 5:
                    viewFile.viewAllWarehouses();
                    break;
                case 6:
                    viewFile.viewAllSalesPoints();
                    break;
                case 7:
                    closeService.showAllEmployees();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    private void manageWarehouses() {
        while (true) {
            System.out.println("\nУПРАВЛЕНИЕ СКЛАДАМИ");
            System.out.println("1. Открыть новый склад");
            System.out.println("2. Закрыть склад");
            System.out.println("0. Назад");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    openService.openNewWarehouse();
                    break;
                case 2:
                    closeService.closeWarehouse();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    private void manageSalesPoints() {
        while (true) {
            System.out.println("\nУПРАВЛЕНИЕ ПУНКТАМИ ПРОДАЖ");
            System.out.println("1. Открыть новый пункт продаж");
            System.out.println("2. Закрыть пункт продаж");
            System.out.println("0. Назад");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    openService.openNewSalesPoint();
                    break;
                case 2:
                    closeService.closeSalesPoint();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    private void manageEmployees() {
        while (true) {
            System.out.println("\nУПРАВЛЕНИЕ СОТРУДНИКАМИ");
            System.out.println("1. Нанять сотрудника");
            System.out.println("2. Уволить сотрудника");
            System.out.println("3. Просмотреть всех сотрудников");
            System.out.println("0. Назад");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    openService.addNewEmployee();
                    break;
                case 2:
                    closeService.dismissEmployee();
                    break;
                case 3:
                    closeService.showAllEmployees();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова");
            }
        }
    }

    public static void main(String[] args) {
        new MainMenu().start();
    }
}