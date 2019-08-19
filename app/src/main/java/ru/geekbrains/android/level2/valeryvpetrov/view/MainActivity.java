package ru.geekbrains.android.level2.valeryvpetrov.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Recipient;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Sender;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ActivityMainBinding;
import ru.geekbrains.android.level2.valeryvpetrov.viewmodel.MainActivityViewModel;

@MainThread
public class MainActivity extends AppCompatActivity {

    public static final String INTENT_ACTION_RECEIVE_SMS = "mainActivityReceiveSMS";
    public static final String INTENT_EXTRA_RECEIVE_SMS = "message";

    private static final String SAVE_INSTANCE_STATE_KEY_RECIPIENT_PHONE_NUMBER = "recipientPhoneNumber";
    private static final String SAVE_INSTANCE_STATE_KEY_MESSAGE_TEXT = "messageText";

    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 123;

    private static boolean isActive = true;

    private MainActivityViewModel viewModel;
    private ActivityMainBinding dataBinding;

    private MessageAdapter messageAdapter;

    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null &&
                    intent.getAction() != null &&
                    intent.getAction().equals(INTENT_ACTION_RECEIVE_SMS) &&
                    intent.getExtras() != null) {
                Message message = (Message) intent.getExtras().getSerializable(INTENT_EXTRA_RECEIVE_SMS);
                if (message != null) {
                    viewModel.receiveSms(message);
                    notifyLastMessageChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        dataBinding.setViewModel(viewModel);

        initViewMessages();
        viewModel.getMessages().observe(this, messages -> {
            messageAdapter.update(messages);
            scrollToTheEnd();
        });
        checkSmsClientPermissions();
        registerReceiver(smsReceiver, new IntentFilter(INTENT_ACTION_RECEIVE_SMS));
    }

    @Override
    protected void onDestroy() {
        isActive = false;

        unregisterReceiver(smsReceiver);
        super.onDestroy();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_PHONE_STATE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dataBinding.send.setEnabled(true);
            } else {
                dataBinding.send.setEnabled(false);
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.error_no_permission_sms),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    public static boolean isActive() {
        return isActive;
    }

    private void initViewMessages() {
        dataBinding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        messageAdapter = new MessageAdapter(viewModel.getUserMobilePhone());
        dataBinding.recyclerViewMessages.setAdapter(messageAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Message message = messageAdapter.getMessage(viewHolder.getAdapterPosition());
                if (message != null) {
                    viewModel.deleteSms(message);
                    messageAdapter.notifyDataSetChanged();
                    scrollToTheEnd();
                }
            }
        }).attachToRecyclerView(dataBinding.recyclerViewMessages);
    }

    private void checkSmsClientPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.SEND_SMS},
                        REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
            }
        }
    }

    public void onClickSend(@NonNull View view) {
        Message message = new Message(new Recipient(dataBinding.recipientPhoneNumber.getText().toString()),
                new Sender(viewModel.getUserMobilePhone()),
                dataBinding.messageText.getText().toString());

        if (message.isLogicValid()) {
            dataBinding.layoutMessage
                    .startAnimation(AnimationUtils
                            .loadAnimation(this, R.anim.send_message));
            viewModel.sendMessage(message);
            notifyLastMessageChanged();
        } else {
            dataBinding.layoutMessage
                    .startAnimation(AnimationUtils
                            .loadAnimation(this, R.anim.shake_message));
        }
        resetInputMessage();
    }

    private void notifyLastMessageChanged() {
        if (messageAdapter.getItemCount() > 0) {
            messageAdapter
                    .notifyItemRangeChanged(messageAdapter.getItemCount() - 1,
                            messageAdapter.getItemCount());
            scrollToTheEnd();
        }
    }

    private void resetInputMessage() {
        dataBinding.recipientPhoneNumber.setText("");
        dataBinding.messageText.setText("");
    }

    private void scrollToTheEnd() {
        if (messageAdapter.getItemCount() > 0) {
            dataBinding.recyclerViewMessages
                    .smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }

}
