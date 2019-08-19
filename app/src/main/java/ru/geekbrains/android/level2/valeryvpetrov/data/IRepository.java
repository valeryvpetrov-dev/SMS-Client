package ru.geekbrains.android.level2.valeryvpetrov.data;

import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;

public interface IRepository<T> {

    void create(T item);

    List<T> readAll();

    void delete(T message);
}
