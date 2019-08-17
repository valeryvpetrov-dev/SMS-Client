package ru.geekbrains.android.level2.valeryvpetrov.data;

import java.util.List;

public interface IRepository<T> {

    void create(T item);

    List<T> readAll();

}
