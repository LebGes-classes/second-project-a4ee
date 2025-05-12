package ru.itis.project;


import ru.itis.project.company.service.SellService;

public class Main {

    public static void main(String[] args) {
        /*Scanner s = new Scanner(System.in);
        while (true) {
            SellService service = new SellService();
            service.startSellService();
            System.out.println("Хотите продолжить покупку?");
            System.out.println("1.Да\n2.Нет");
            int choice = s.nextInt();
            if (choice == 2){
                System.out.println("Покупки завершены");
                break;
            }
            }
*/
        SellService service = new SellService();
        service.processOrder();
    }




}