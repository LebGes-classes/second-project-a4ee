package ru.itis.project.company.repository;

import ru.itis.project.company.models.Employee;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRepository {
    private static final String EMPLOYEE_FILE = "src/main/resources/employees.txt";
    private List<Employee> employees;

    public EmployeeRepository() {
        this.employees = loadAll();
    }

    public List<Employee> loadAll() {
        List<Employee> employeeList = new ArrayList<>();
        List<String[]> fileData = FileStorage.readFile(EMPLOYEE_FILE);

        for (String[] data : fileData) {
            if (data.length < 5) continue;

            try {
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String workPlace = data[2];
                String position = data[3];
                int workplaceId = Integer.parseInt(data[4]);

                employeeList.add(new Employee(id, name, workPlace, position, workplaceId));
            } catch (NumberFormatException e) {
                System.err.println("Ошибка формата данных: " + String.join(";", data));
            }
        }
        return employeeList;
    }

    public void save(Employee employee) {
        if (employee.getId() == 0) {
            int newId = employees.stream()
                    .mapToInt(Employee::getId)
                    .max()
                    .orElse(0) + 1;
            employee.setId(newId);
        }
        employees.add(employee);
        saveAll();
    }


    public void saveAll() {
        List<String> lines = new ArrayList<>();

        for (Employee employee : employees) {
            String line = String.format("%d;%s;%s;%s;%d",
                    employee.getId(),
                    employee.getName(),
                    employee.getWorkPlace(),
                    employee.getPosition(),
                    employee.getWorkPlaceId());

            lines.add(line);
        }

        FileStorage.writeFile(EMPLOYEE_FILE, lines);
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public List<Employee> findByWorkplace(String workPlace, int workplaceId) {
        return employees.stream()
                .filter(e -> e.getWorkPlace().equals(workPlace) && e.getWorkPlaceId() == workplaceId)
                .collect(Collectors.toList());
    }

    public boolean delete(int employeeId) {
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == employeeId) {
                iterator.remove();
                saveAll();
                return true;
            }
        }
        return false;
    }

}