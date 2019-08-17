package ru.geekbrains.android.level2.valeryvpetrov.data.model;

import java.io.Serializable;

import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor()
@NoArgsConstructor
@Data
public class Recipient
        extends RealmObject
        implements Serializable {

    private String phoneNumber;

    public boolean isLogicValid() {
        return phoneNumber != null && phoneNumber.trim().length() > 0;
    }

}
