package ru.geekbrains.android.level2.valeryvpetrov.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageIncomingBinding;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageOutgoingBinding;

@MainThread
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_OUTGOING_MESSAGE = 0;
    private static final int VIEW_TYPE_INCOMING_MESSAGE = 1;

    @Nullable
    private List<Message> messages;

    public MessageAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());
        ViewDataBinding itemDataBinding;
        if (viewType == VIEW_TYPE_OUTGOING_MESSAGE) {   // outgoing message
            itemDataBinding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_message_outgoing, parent, false);
        } else {                                        // incoming message
            itemDataBinding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_message_incoming, parent, false);
        }
        return new ViewHolder(itemDataBinding, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
            // TODO check if phone number belongs to user
            if (message.getRecipient().getPhoneNumber().equals("0"))
                return VIEW_TYPE_INCOMING_MESSAGE;
            if (message.getRecipient().getPhoneNumber().equals("1"))
                return VIEW_TYPE_OUTGOING_MESSAGE;
        }
        return -1;
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

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private ViewDataBinding dataBinding;
        private int viewType;

        public ViewHolder(@NonNull ViewDataBinding dataBinding, int viewType) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            this.viewType = viewType;
        }

        public void bind(@NonNull Message message) {
            if (viewType == VIEW_TYPE_OUTGOING_MESSAGE) {
                bindOutgoingMessage(((ItemMessageOutgoingBinding) dataBinding), message);
            }
            if (viewType == VIEW_TYPE_INCOMING_MESSAGE) {
                bindIncomingMessage(((ItemMessageIncomingBinding) dataBinding), message);
            }
        }

        private void bindOutgoingMessage(@NonNull ItemMessageOutgoingBinding dataBinding,
                                         @NonNull Message message) {
            dataBinding.setMessage(message);
            dataBinding.executePendingBindings();

            itemView.setAnimation(AnimationUtils
                    .loadAnimation(itemView.getContext(), R.anim.item_message_outgoing));
        }

        private void bindIncomingMessage(@NonNull ItemMessageIncomingBinding dataBinding,
                                         @NonNull Message message) {
            dataBinding.setMessage(message);
            dataBinding.executePendingBindings();

            itemView.setAnimation(AnimationUtils
                    .loadAnimation(itemView.getContext(), R.anim.item_message_incoming));
        }
    }

}
