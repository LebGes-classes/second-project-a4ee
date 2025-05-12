package ru.itis.project.company.models;

import java.util.List;

public class Warehouse {
    private String name;
    private List<WarehouseCell> cells;
    private Employee responsibility;
    private int id;


    public Warehouse( int id, String name, List<WarehouseCell> cells) {
        this.id = id;
        this.name = name;
        this.cells = cells;
    }

    public Warehouse(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void addCell(WarehouseCell cell){
        cells.add(cell);
    }

    public List<WarehouseCell> getCells() {
        return cells;
    }

    public void setCells(List<WarehouseCell> cells) {
        this.cells = cells;
    }

    public Employee getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(Employee responsibility) {
        this.responsibility = responsibility;
    }

    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }


}
