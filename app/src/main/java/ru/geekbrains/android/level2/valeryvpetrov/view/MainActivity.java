package ru.geekbrains.android.level2.valeryvpetrov.view;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.realm.Realm;
import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Recipient;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ActivityMainBinding;
import ru.geekbrains.android.level2.valeryvpetrov.viewmodel.MainActivityViewModel;

@MainThread
public class MainActivity extends AppCompatActivity {

    private static final String SAVE_INSTANCE_STATE_KEY_RECIPIENT_PHONE_NUMBER = "recipientPhoneNumber";
    private static final String SAVE_INSTANCE_STATE_KEY_MESSAGE_TEXT = "messageText";

    private MainActivityViewModel viewModel;
    private ActivityMainBinding dataBinding;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearDB();

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        dataBinding.setViewModel(viewModel);

        dataBinding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        messageAdapter = new MessageAdapter();
        dataBinding.recyclerViewMessages.setAdapter(messageAdapter);

        viewModel.getMessages().observe(this, messages -> {
            messageAdapter.update(messages);
        });
    }

    public void onClickSend(@NonNull View view) {
        Message message = new Message(
                new Recipient(dataBinding.recipientPhoneNumber.getText().toString()),
                dataBinding.messageText.getText().toString());

        if (message.isLogicValid()) {
            dataBinding.layoutMessage
                    .startAnimation(AnimationUtils
                            .loadAnimation(this, R.anim.send_message));
            viewModel.sendMessage(message);
            messageAdapter
                    .notifyItemRangeChanged(messageAdapter.getItemCount() - 1,
                            messageAdapter.getItemCount());
        } else {
            dataBinding.layoutMessage
                    .startAnimation(AnimationUtils
                            .loadAnimation(this, R.anim.shake_message));
        }
        resetInputMessage();
    }

    private void resetInputMessage() {
        dataBinding.recipientPhoneNumber.setText("");
        dataBinding.messageText.setText("");
    }

    private void clearDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(_realm -> {
            _realm.deleteAll();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVE_INSTANCE_STATE_KEY_RECIPIENT_PHONE_NUMBER,
                dataBinding.recipientPhoneNumber.getText().toString().trim());
        outState.putString(SAVE_INSTANCE_STATE_KEY_MESSAGE_TEXT,
                dataBinding.messageText.getText().toString().trim());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        dataBinding.recipientPhoneNumber
                .setText(savedInstanceState
                        .getString(SAVE_INSTANCE_STATE_KEY_RECIPIENT_PHONE_NUMBER));
        dataBinding.messageText
                .setText(savedInstanceState
                        .getString(SAVE_INSTANCE_STATE_KEY_MESSAGE_TEXT));
        super.onRestoreInstanceState(savedInstanceState);
    }

}
