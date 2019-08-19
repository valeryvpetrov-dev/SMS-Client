package ru.geekbrains.android.level2.valeryvpetrov.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.util.SmsUtil;
import ru.geekbrains.android.level2.valeryvpetrov.view.MainActivity;

public class SmsReceiver extends BroadcastReceiver {

    private static final String CHANNEL_INCOMING_SMS_ID = "channelIncomingSMSID";

    private int notificationId = 1;

    @NonNull
    private SmsUtil smsUtil;

    public SmsReceiver() {
        super();
        smsUtil = SmsUtil.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getExtras() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus != null) {
                Message message = smsUtil.receiveSms(pdus);
                if (MainActivity.isActive()) {
                    notifyActivity(context, message);
                } else {
                    createNotificationChannel(context);
                    createNotification(context, message);
                }
                abortBroadcast(); // work on versions less or equal to 4.4
            }
        }
    }

    /**
     * Sends intent to running activity to handle sms received action
     */
    private void notifyActivity(@NonNull Context context, @NonNull Message message) {
        Intent intent = new Intent(MainActivity.INTENT_ACTION_RECEIVE_SMS);
        intent.putExtra(MainActivity.INTENT_EXTRA_RECEIVE_SMS, message);
        context.sendBroadcast(intent);
    }

    private void createNotification(@NonNull Context context, @NonNull Message message) {
        Intent newLaunchInfo = new Intent(context, MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(newLaunchInfo);
        PendingIntent pendingIntentLoadLatestPhotos = taskStackBuilder
                .getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);

        String contentTitle = String.format("Incoming message from %s.",
                message.getSender().getPhoneNumber());
        String contentText = message.getText();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_INCOMING_SMS_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_action_show_sms,
                        context.getString(R.string.action_show_sms),
                        pendingIntentLoadLatestPhotos);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(notificationId++, builder.build()); // suppress multiple notifications send}
    }

    private void createNotificationChannel(@NonNull Context context) {
        // Notification channel is used only on API 26+
        // It is safe to rerun this code because existing channel won't be recreated
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            CharSequence channelName = context.getString(R.string.channel_incoming_sms_name);
            String channelDescription = context.getString(R.string.channel_incoming_sms_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_INCOMING_SMS_ID,
                    channelName,
                    channelImportance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
}
