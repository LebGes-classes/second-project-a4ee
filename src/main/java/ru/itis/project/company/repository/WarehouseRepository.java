package ru.itis.project.company.repository;

import ru.itis.project.company.models.Warehouse;
import ru.itis.project.company.models.WarehouseCell;

import java.util.ArrayList;
import java.util.List;

public class WarehouseRepository implements FileRepository {
    private static final String WAREHOUSE_FILE = "src/main/resources/warehouse.txt";
    private List<Warehouse> warehouses;
    private List<WarehouseCell> cells;
    private static final String WAREHOUSE_CELL_FILE = "src/main/resources/warehouse_cell.txt";
    WarehouseCellRepository warehouseCellRepository = new WarehouseCellRepository();

    public WarehouseRepository() {
        this.warehouses = loadAll();
    }
    @Override
    public List<Warehouse> loadAll(){
        List<WarehouseCell> warehouseCells = new ArrayList<>();
        List<Warehouse> wr = new ArrayList<>();
        List<String[]> file = FileStorage.readFile(WAREHOUSE_FILE);

        for(String[] data:file){
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            cells = warehouseCellRepository.findAll();

            for(WarehouseCell cell:cells){
                if (cell.getWarehouseId() == id){
                    warehouseCells.add(cell);
                }
            }
            wr.add(new Warehouse(id,name,warehouseCells));
        }
        return wr;
    }

    @Override
    public List<Warehouse> findAll() {
        return new ArrayList<>(warehouses);
    }

    @Override
    public Warehouse findById(int id) {
        for (Warehouse wr: warehouses){
            if (wr.getId() == id){
                return wr;
            }
        }
        return null;
    }

    public void save(Warehouse warehouse){
        List<Warehouse> warehouseList = this.findAll();
        List<String> lines = new ArrayList<>();
        for (Warehouse w: warehouseList){
            String line = String.format("%d;%s",w.getId(),w.getName());
            lines.add(line);
        }
        String newLine = String.format("%d;%s", warehouse.getId(),warehouse.getName());
        lines.add(newLine);
        FileStorage.writeFile(WAREHOUSE_FILE,lines);

    }
    public void createNewCells(Warehouse warehouse){
        List<String> lines = new ArrayList<>();
        List<WarehouseCell> warehouseCells = warehouseCellRepository.findAll();
        List<WarehouseCell> newCells = new ArrayList<>();
        int warehouseId = warehouse.getId();
        for (WarehouseCell c: warehouseCells){
            String line = String.format("%d;%s;%s;%s", c.getWarehouseId(), c.getId(),c.getProduct().getId(),c.getQuantity());
            lines.add(line);
        }
        for(int i = 1; i <= 5;i++){
            int cellId = warehouseId * 100 + i;
            String cell = String.format("%d;%s;%s;%s",warehouseId,cellId,0,0);
            lines.add(cell);
        }
        FileStorage.writeFile(WAREHOUSE_CELL_FILE,lines);
    }
    public void delete(int id){
        List<Warehouse> warehouseList = this.findAll();
        int indexId = -1;
        for (int i = 0; i < warehouseList.size(); i++){
            if (warehouseList.get(i).getId() == id){
                indexId = i;
            }
        }
        warehouseList.remove(indexId);
        for (int i = indexId; i < warehouseList.size(); i++){
            Warehouse wr = warehouseList.get(i);
            wr.setId(wr.getId() -1);
        }

        List<String> updateLine = new ArrayList<>();
        for (Warehouse warehouse: warehouseList){
            String line = String.format("%d;%s",warehouse.getId(),warehouse.getName());
            updateLine.add(line);
        }
        FileStorage.writeFile(WAREHOUSE_FILE,updateLine);
        deleteCells(id);
    }

    private void deleteCells(int deletedWarehouseId) {
        WarehouseCellRepository cellRepo = new WarehouseCellRepository();
        List<WarehouseCell> allCells = cellRepo.findAll();
        List<WarehouseCell> cellsToDelete = new ArrayList<>();
        List<WarehouseCell> cellsToUpdate = new ArrayList<>();

        for (WarehouseCell cell : allCells) {
            if (cell.getWarehouseId() == deletedWarehouseId) {
                cellsToDelete.add(cell);
            }
        }

        for (WarehouseCell cell : cellsToDelete) {
            if (cell.getProduct() == null || cell.getQuantity() == 0) {
                continue;
            }

            WarehouseCell targetCell = null;
            for (WarehouseCell other : allCells) {
                if (other.getWarehouseId() != deletedWarehouseId &&
                        other.getProduct() != null &&
                        other.getProduct().getId() == cell.getProduct().getId()) {
                    targetCell = other;
                    break;
                }
            }

            if (targetCell != null) {
                targetCell.setQuantity(targetCell.getQuantity() + cell.getQuantity());
                cellsToUpdate.add(targetCell);
            } else {
                for (WarehouseCell other : allCells) {
                    if (other.getWarehouseId() != deletedWarehouseId &&
                            (other.getProduct() == null || other.getQuantity() == 0)) {

                        other.setProduct(cell.getProduct());
                        other.setQuantity(cell.getQuantity());
                        cellsToUpdate.add(other);
                        break;
                    }
                }
            }
        }

        allCells.removeIf(cell -> cell.getWarehouseId() == deletedWarehouseId);

        for (WarehouseCell cell : allCells) {
            if (cell.getWarehouseId() > deletedWarehouseId) {
                cell.setWarehouseId(cell.getWarehouseId() - 1);
            }
        }

        cellRepo.saveAll(allCells);
    }
}
