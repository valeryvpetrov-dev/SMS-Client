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
public class Message
        extends RealmObject
        implements Serializable {

    private Recipient recipient;
    private Sender sender;
    private String text;

    public boolean isLogicValid() {
        return recipient.isLogicValid() &&
                sender.isLogicValid() &&
                text != null && text.trim().length() > 0;
    }

}
