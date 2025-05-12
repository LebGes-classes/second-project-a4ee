package ru.itis.project.company.repository;

import ru.itis.project.company.models.Product;
import ru.itis.project.company.models.SalesPoint;
import ru.itis.project.company.models.WarehouseCell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseCellRepository implements FileRepository{
    private static final String WAREHOUSE_CELL_FILE = "src/main/resources/warehouse_cell.txt";
    List<WarehouseCell> warehouseCells;

    public WarehouseCellRepository() {
        this.warehouseCells = loadAll();
    }

    public List<WarehouseCell> findAll() {
        return new ArrayList<WarehouseCell>(warehouseCells);
    }

    @Override
    public Object findById(int id) {
        return null;
    }

    @Override
    public List<WarehouseCell> loadAll() {
        ProductRepository repo = new ProductRepository();
        List<WarehouseCell> wc = new ArrayList<>();
        List<String[]> file = FileStorage.readFile(WAREHOUSE_CELL_FILE);

        for (String[] data: file){

            int warehouseId = Integer.parseInt(data[0]);
            int cellId = Integer.parseInt(data[1]);
            int productId = Integer.parseInt(data[2]);
            int quantity = Integer.parseInt(data[3]);

            Product pr = repo.findById(productId);

            wc.add(new WarehouseCell(cellId,warehouseId,quantity,pr));
        }
        return wc;
    }
    public void saveForBuy(SalesPoint point) {
        // 0. Проверка существования файла
        String filePath = "src/main/resources/warehouse_cell.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalStateException("Файл warehouse_cell.txt не найден по пути: " + file.getAbsolutePath());
        }

        // 1. Получаем текущие данные из файла
        List<String[]> allCellsData = FileStorage.readFile(filePath);
        List<String> updatedLines = new ArrayList<>();

        // 2. Проверка склада
        if (point.getWarehouse() == null) {
            throw new IllegalArgumentException("У пункта продаж не указан склад");
        }

        // 3. Создаем карту обновленных ячеек (используем cellId как ключ)
        Map<Integer, WarehouseCell> updatedCells = new HashMap<>();
        for (WarehouseCell cell : point.getWarehouse().getCells()) {
            updatedCells.put(cell.getId(), cell);
        }

        // 4. Обрабатываем данные
        for (String[] cellData : allCellsData) {
            if (cellData.length < 4) {
                System.err.println("Пропущена некорректная строка: " + String.join(";", cellData));
                continue;
            }

            try {
                int warehouseId = Integer.parseInt(cellData[0]);
                int cellId = Integer.parseInt(cellData[1]);

                if (warehouseId == point.getWarehouse().getId()) {
                    WarehouseCell updatedCell = updatedCells.get(cellId);
                    if (updatedCell != null) {
                        // Формируем обновленную строку
                        String newLine = String.format("%d;%d;%d;%d",
                                warehouseId,
                                cellId,
                                updatedCell.getProduct().getId(),
                                updatedCell.getQuantity());
                        updatedLines.add(newLine);
                        System.out.println("Обновлена ячейка: " + newLine);
                        continue;
                    }
                }
                // Сохраняем неизмененные данные
                updatedLines.add(String.join(";", cellData));
            } catch (NumberFormatException e) {
                System.err.println("Ошибка формата данных: " + String.join(";", cellData));
            }
        }

        // 5. Создаем резервную копию перед записью
        createBackup(filePath);

        // 6. Записываем обновленные данные
        FileStorage.writeFile(filePath, updatedLines);
        System.out.println("Данные успешно сохранены");
    }

    private void createBackup(String originalPath) {
        try {
            Path source = Paths.get(originalPath);
            Path backup = Paths.get(originalPath + ".bak");
            Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Не удалось создать резервную копию: " + e.getMessage());
        }
    }
    public void saveAll(List<WarehouseCell> cells) {
        List<String> lines = new ArrayList<>();
        for (WarehouseCell cell : cells) {
            int productId = cell.getProduct() != null ? cell.getProduct().getId() : 0;
            String line = String.format("%d;%d;%d;%d",
                    cell.getWarehouseId(),
                    cell.getId(),
                    productId,
                    cell.getQuantity());
            lines.add(line);
        }
        FileStorage.writeFile(WAREHOUSE_CELL_FILE, lines);
    }

    public void save(WarehouseCell cell){
        List<WarehouseCell> allCells = findAll();
        for (int i = 0; i < allCells.size(); i++) {
            WarehouseCell existing = allCells.get(i);
            if (existing.getWarehouseId() == cell.getWarehouseId() &&
                    existing.getId() == cell.getId()) {
                allCells.set(i, cell);
                break;
            }
        }
        saveAll(allCells);
    }
    public WarehouseCell findCellWithProduct(int warehouseId, int productId) {
        for (WarehouseCell cell : this.findAll()) {
            if (cell.getWarehouseId() == warehouseId &&
                    cell.getProduct() != null &&
                    cell.getProduct().getId() == productId) {
                return cell;
            }
        }
        return null;
    }

    public int findNextCellId(int warehouseId) {
        int maxCellId = 0;
        for (WarehouseCell cell : this.findAll()) {
            if (cell.getWarehouseId() == warehouseId && cell.getId() > maxCellId) {
                maxCellId = cell.getId();
            }
        }
        return maxCellId + 1;
    }


}

