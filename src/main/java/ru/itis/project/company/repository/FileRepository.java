package ru.itis.project.company.repository;

import java.util.List;

public interface FileRepository<T> {

    List<T> findAll();

    T findById(int id);

    List<T> loadAll();
}
