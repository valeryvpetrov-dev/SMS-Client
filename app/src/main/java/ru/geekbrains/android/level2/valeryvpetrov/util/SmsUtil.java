package ru.geekbrains.android.level2.valeryvpetrov.util;

import android.annotation.SuppressLint;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;

import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Recipient;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Sender;

public class SmsUtil {

    private static final String USER_PHONE_NUMBER = "userPhoneNumber";

    private static SmsUtil instance;

    private SmsUtil() {
    }

    @NonNull
    public static SmsUtil getInstance() {
        if (instance == null) {
            instance = new SmsUtil();
        }
        return instance;
    }

    public void sendSms(@NonNull Message message) {
        SmsManager.getDefault()
                .sendTextMessage(message.getRecipient().getPhoneNumber(),
                        null,
                        message.getText(),
                        null,
                        null);
    }

    @NonNull
    public Message receiveSms(@NonNull Object[] pdus) {
        return createFromPdu(pdus);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @NonNull
    public String getUserPhoneNumber() {
        // returns null or blank string
//        TelephonyManager telephonyManager =
//                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        userPhoneNumber = telephonyManager != null ?
//                telephonyManager.getLine1Number() :
//                null;
        return USER_PHONE_NUMBER;
    }

    @NonNull
    private Message createFromPdu(@NonNull Object[] pdus) {
        SmsMessage[] smsMessages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        String smsFrom = smsMessages[0].getDisplayOriginatingAddress();
        StringBuilder smsBodyBuilder = new StringBuilder();
        for (SmsMessage smsMessage : smsMessages) {
            smsBodyBuilder.append(smsMessage.getMessageBody());
        }
        String smsBodyText = smsBodyBuilder.toString();
        return new Message(new Recipient(getUserPhoneNumber()),
                new Sender(smsFrom),
                smsBodyText);
    }
}
