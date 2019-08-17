package ru.geekbrains.android.level2.valeryvpetrov;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SmsApplication extends Application {

    public static final String REALM_DB_NAME = SmsApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(REALM_DB_NAME)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

}
