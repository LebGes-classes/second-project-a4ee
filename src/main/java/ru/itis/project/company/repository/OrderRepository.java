package ru.itis.project.company.repository;

import ru.itis.project.company.models.Product;
import ru.itis.project.company.models.SalesPoint;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final String ORDER_FILE = "src/main/resources/order.txt";


    public void save(SalesPoint point, Product product, int quantity){
        List<String[]> currentLines = FileStorage.readFile(ORDER_FILE);
        List<String> listLine = new ArrayList<>();
        for (String[] data: currentLines){
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            double price = Double.parseDouble(data[2]);
            int quan = Integer.parseInt(data[3]);
            String line = String.format("%d;%s;%s;%s",id,name,price,quan);
            listLine.add(line);

        }
        int id = product.getId();
        double price = product.getPrice();
        String line = String.format("%d;%s;%s;%s", point.getId(),id,price*quantity,quantity);
        listLine.add(line);
        FileStorage.writeFile(ORDER_FILE,listLine);
    }

}

