package ru.geekbrains.android.level2.valeryvpetrov.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageIncomingBinding;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageOutgoingBinding;

@MainThread
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_OUTGOING_MESSAGE = 0;
    private static final int VIEW_TYPE_INCOMING_MESSAGE = 1;

    @Nullable
    private List<Message> messages;

    @NonNull
    private String userPhoneNumber;

    public MessageAdapter(@NonNull String userMobilePhone) {
        this.userPhoneNumber = userMobilePhone;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());

        if (viewType == VIEW_TYPE_OUTGOING_MESSAGE) {   // outgoing message
            ItemMessageOutgoingBinding itemDataBinding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_message_outgoing, parent, false);
            return new MessageViewHolder.OutgoingMessageViewHolder(itemDataBinding);
        } else {                                        // incoming message
            ItemMessageIncomingBinding itemDataBinding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_message_incoming, parent, false);
            return new MessageViewHolder.IncomingMessageViewHolder(itemDataBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messages != null && messages.size() > position)
            holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages != null && messages.size() > position) {
            Message message = messages.get(position);
            if (message.getRecipient().getPhoneNumber().equals(userPhoneNumber))
                return VIEW_TYPE_INCOMING_MESSAGE;
            else
                return VIEW_TYPE_OUTGOING_MESSAGE;
        }
        return super.getItemViewType(position);
    }

    public void update(@NonNull List<Message> messages) {
        if (this.messages == null) {
            this.messages = messages;
            notifyItemRangeChanged(0, messages.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return MessageAdapter.this.messages.size();
                }

                @Override
                public int getNewListSize() {
                    return messages.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return MessageAdapter.this.messages.get(oldItemPosition)
                            .equals(messages.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return areItemsTheSame(oldItemPosition, newItemPosition);
                }
            });
            this.messages = messages;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Nullable
    public Message getMessage(int position) {
        if (this.messages == null) return null;
        return this.messages.get(position);
    }
}
