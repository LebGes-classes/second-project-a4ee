package ru.itis.project.company.repository;

import ru.itis.project.company.models.SalesPoint;
import ru.itis.project.company.models.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class SalesPointRepository implements FileRepository {
    private WarehouseRepository warehouseRepository = new WarehouseRepository();
    private static final String SALES_POINT_FILE = "src/main/resources/sales_point.txt";
    List<SalesPoint> salesPoint;


    public SalesPointRepository() {
        this.salesPoint = loadAll();
    }

    @Override
    public List<SalesPoint> loadAll() {
        List<SalesPoint> sp = new ArrayList<>();
        List<String[]> file = FileStorage.readFile(SALES_POINT_FILE);

        for (String[] data : file) {
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            int warehouseId = Integer.parseInt(data[2]);
            Warehouse warehouse = warehouseRepository.findById(warehouseId);
            sp.add(new SalesPoint(id, name, warehouse));
        }
        return sp;

    }

    @Override
    public List<SalesPoint> findAll() {
        return new ArrayList<SalesPoint>(salesPoint);
    }

    @Override
    public SalesPoint findById(int id) {
        for (SalesPoint sp : salesPoint) {
            if (sp.getId() == id) {
                return sp;
            }
        }
        return null;
    }

    public void save(SalesPoint point){
        List<SalesPoint> points = this.findAll();
        List<String> updateLines = new ArrayList<>();
        String newLine = String.format("%d;%s;%d",
                point.getId(),
                point.getName(),
                point.getWarehouse().getId());
        for (SalesPoint p: points){
            String lines = String.format("%d;%s;%d",p.getId(),p.getName(),p.getWarehouse().getId());
            updateLines.add(lines);
        }
        updateLines.add(newLine);
        FileStorage.writeFile(SALES_POINT_FILE,updateLines);
    }

    public void delete(int id){
        List<SalesPoint> points =this.findAll();
        int indexId = -1;
        for (int i = 0; i < points.size(); i++){
            if (points.get(i).getId() == id){
                indexId = i;
            }
        }
        if (indexId == -1){
            System.out.println("Пункт продаж с этим Id не найден");
            return;
        }
        points.remove(indexId);
        for (int i = indexId; i < points.size(); i++){
            SalesPoint sp = points.get(i);
            sp.setId(sp.getId() -1);
        }

        List<String> updateLine = new ArrayList<>();
        for (SalesPoint point: points){
            String line = String.format("%d;%s;%d",point.getId(),point.getName(),point.getWarehouse().getId());
            updateLine.add(line);
        }
        FileStorage.writeFile(SALES_POINT_FILE,updateLine);
    }

    public void updateSales(int warehouseId){
        List<SalesPoint> salesPoints = this.findAll();
        List<String> lines = new ArrayList<>();
        for (SalesPoint p: salesPoints){
            int id = p.getWarehouse().getId();
            if (p.getWarehouse().getId()>=warehouseId){
                id--;
            }
            String line = String.format("%d;%s;%d", p.getId(),p.getName(),id);
            lines.add(line);
        }
        FileStorage.writeFile(SALES_POINT_FILE,lines);

    }
}