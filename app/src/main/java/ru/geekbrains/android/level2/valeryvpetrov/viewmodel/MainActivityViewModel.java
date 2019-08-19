package ru.geekbrains.android.level2.valeryvpetrov.viewmodel;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.data.MessageRepository;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.util.SmsUtil;

@MainThread
public class MainActivityViewModel
        extends ViewModel {

    private MutableLiveData<List<Message>> messages;

    @NonNull
    private MessageRepository messageRepository;

    @NonNull
    private SmsUtil smsUtil;

    public MainActivityViewModel() {
        super();
        messageRepository = MessageRepository.getInstance();
        smsUtil = SmsUtil.getInstance();
    }

    @NonNull
    public LiveData<List<Message>> getMessages() {
        if (messages == null) {
            messages = new MutableLiveData<>();
            messages.setValue(messageRepository.readAll());
        }
        return messages;
    }

    @NonNull
    public String getUserMobilePhone() {
        return smsUtil.getUserPhoneNumber();
    }

    /**
     * Sends message to recipient.
     *
     * @param message - validated message (message.isLogicValid() == true)
     */
    public void sendMessage(@NonNull Message message) {
        messageRepository.create(message);
        if (messages.getValue() != null) {
            messages.getValue().add(message);
            smsUtil.sendSms(message);
        }
    }

    public void receiveSms(@NonNull Message message) {
        messageRepository.create(message);
        if (messages.getValue() != null) {
            messages.getValue().add(message);
        }
    }

    public void deleteSms(@NonNull Message message) {
        messageRepository.delete(message);
        if (messages.getValue() != null) {
            messages.getValue().remove(message);
        }
    }
}
