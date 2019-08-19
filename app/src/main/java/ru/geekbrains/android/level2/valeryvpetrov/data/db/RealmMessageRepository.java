package ru.geekbrains.android.level2.valeryvpetrov.data.db;

import androidx.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import ru.geekbrains.android.level2.valeryvpetrov.data.IRepository;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;

public class RealmMessageRepository implements IRepository<Message> {

    private static RealmMessageRepository instance;

    @NonNull
    private Realm realm;

    private RealmMessageRepository() {
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    public static RealmMessageRepository getInstance() {
        if (instance == null) {
            instance = new RealmMessageRepository();
        }
        return instance;
    }

    @Override
    public void create(@NonNull Message item) {
        realm.executeTransaction(_realm -> {
            _realm.insertOrUpdate(item);
        });
    }

    @NonNull
    @Override
    public List<Message> readAll() {
        return realm.copyFromRealm(realm
                .where(Message.class)
                .findAll());
    }

    @Override
    public void delete(@NonNull Message item) {
        realm.executeTransaction(_realm -> {
            Message itemToDelete = _realm
                    .where(Message.class)
                    .findFirst();
            if (itemToDelete != null)
                itemToDelete.deleteFromRealm();
        });
    }
}
