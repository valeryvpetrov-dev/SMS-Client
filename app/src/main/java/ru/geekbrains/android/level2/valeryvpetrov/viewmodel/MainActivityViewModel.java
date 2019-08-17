package ru.geekbrains.android.level2.valeryvpetrov.viewmodel;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.data.IRepository;
import ru.geekbrains.android.level2.valeryvpetrov.data.MessageRepository;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;

@MainThread
public class MainActivityViewModel
        extends ViewModel {

    private MutableLiveData<List<Message>> messages;

    @NonNull
    private IRepository<Message> messageRepository;

    public MainActivityViewModel() {
        super();
        messageRepository = MessageRepository.getInstance();
    }

    @NonNull
    public LiveData<List<Message>> getMessages() {
        if (messages == null) {
            messages = new MutableLiveData<>();
            getMessagesFromDB();
        }
        return messages;
    }

    /**
     * Sends message to recipient.
     *
     * @param message - validated message (message.isLogicValid() == true)
     */
    public void sendMessage(@NonNull Message message) {
        if (messages.getValue() != null) {
            messages.getValue().add(message);
            addMessageToDB(message);
        }
    }

    private void getMessagesFromDB() {
        messages.setValue(messageRepository.readAll());
    }

    private void addMessageToDB(@NonNull Message message) {
        messageRepository.create(message);
    }
}
