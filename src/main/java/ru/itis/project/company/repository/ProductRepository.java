package ru.itis.project.company.repository;

import ru.itis.project.company.models.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRepository implements FileRepository{
    private static final String PRODUCT_FILE = "src/main/resources/products.txt";
    private static final String PRODUCT_PURCHASES_FILE = "src/main/resources/products_for_purchases.txt";
    private List<Product> products;

    public ProductRepository() {
        this.products = loadAll();
    }


    //вывести все продукты в список
    @Override
    public List<Product> loadAll(){
        List<Product> product = new ArrayList<>();
        List<String[]> file = FileStorage.readFile(PRODUCT_FILE);

        for (String[] data: file){
            int id = Integer.parseInt(data[0].trim());
            String name = data[1];
            double price = Double.parseDouble(data[2].trim().replace(",", "."));

            product.add(new Product(id,name,price));
        }

        return product;
    }

    public ArrayList<Product> loadPurchases() {
        ArrayList<Product> products = new ArrayList<>();
        List<String[]> fileData = FileStorage.readFile(PRODUCT_PURCHASES_FILE);

        for (String[] data : fileData) {
            try {
                // Безопасный парсинг с проверкой массива
                if (data.length >= 3) {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());

                    products.add(new Product(id, name, price));
                } else {
                    System.err.println("Ошибка формата" + Arrays.toString(data));
                }
            } catch (NumberFormatException e) {
                System.err.println(Arrays.toString(data));
            }
        }

        return products;
    }


    @Override
    public List<Product> findAll() {
        return new ArrayList<Product>(products);
    }

    @Override
    public Product findById(int id) {
        for(Product product: products){
            if (product.getId() == id){
                return product;
            }
        }
        return null;

    }

    //найти продукт по имени
    public Product findByName(String productName){
        for(Product product: products){
            if (product.getName().equals(productName)){
                return product;
            }
        }
        return null;
    }
    public void save(Product product) {
        List<Product> products = findAll();

        boolean exists = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {

                products.set(i, product);
                exists = true;
                break;
            }
        }
        if (!exists) {
            products.add(product);
        }
        List<String> lines = new ArrayList<>();
        for (Product p : products) {
            lines.add(String.format("%d;%s;%.2f",
                    p.getId(),
                    p.getName(),
                    p.getPrice()));
        }

        FileStorage.writeFile(PRODUCT_FILE, lines);
    }
}
