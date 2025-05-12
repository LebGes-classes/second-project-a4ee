package ru.itis.project.company.models;

public class Employee {
    private int id;
    private String name;
    private String workPlace;
    private String position;
    private int workPlaceId;


    public Employee(int id, String name, String workPlace, String position, int workPlaceId) {
        this.id = id;
        this.name = name;
        this.workPlace = workPlace;
        this.position = position;
        this.workPlaceId = workPlaceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public int getWorkPlaceId() {
        return workPlaceId;
    }

    public void setWorkPlaceId(int workPlaceId) {
        this.workPlaceId = workPlaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
