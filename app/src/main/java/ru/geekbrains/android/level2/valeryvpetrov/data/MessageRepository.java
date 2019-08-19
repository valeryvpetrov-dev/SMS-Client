package ru.geekbrains.android.level2.valeryvpetrov.data;

import androidx.annotation.NonNull;

import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.data.db.RealmMessageRepository;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;

public class MessageRepository implements IRepository<Message> {

    private static MessageRepository instance;

    @NonNull
    private RealmMessageRepository realmMessageRepository;

    private MessageRepository(@NonNull RealmMessageRepository realmMessageRepository) {
        this.realmMessageRepository = realmMessageRepository;
    }

    @NonNull
    public static MessageRepository getInstance() {
        if (instance == null) {
            instance = new MessageRepository(RealmMessageRepository.getInstance());
        }
        return instance;
    }

    @Override
    public void create(@NonNull Message item) {
        realmMessageRepository.create(item);
    }

    @NonNull
    @Override
    public List<Message> readAll() {
        return realmMessageRepository.readAll();
    }

    @Override
    public void delete(Message message) {
        realmMessageRepository.delete(message);
    }

}
