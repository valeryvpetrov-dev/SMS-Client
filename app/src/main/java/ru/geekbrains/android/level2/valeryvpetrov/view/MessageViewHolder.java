package ru.geekbrains.android.level2.valeryvpetrov.view;

import android.view.animation.AnimationUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.android.level2.valeryvpetrov.R;
import ru.geekbrains.android.level2.valeryvpetrov.data.model.Message;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageIncomingBinding;
import ru.geekbrains.android.level2.valeryvpetrov.databinding.ItemMessageOutgoingBinding;

@MainThread
public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    protected ViewDataBinding dataBinding;

    public MessageViewHolder(@NonNull ViewDataBinding dataBinding) {
        super(dataBinding.getRoot());
        this.dataBinding = dataBinding;
    }

    public abstract void bind(@NonNull Message message);

    public static class OutgoingMessageViewHolder extends MessageViewHolder {

        public OutgoingMessageViewHolder(@NonNull ViewDataBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(@NonNull Message message) {
            ItemMessageOutgoingBinding dataBinding = (ItemMessageOutgoingBinding) this.dataBinding;
            dataBinding.setMessage(message);
            dataBinding.executePendingBindings();

            itemView.setAnimation(AnimationUtils
                    .loadAnimation(itemView.getContext(), R.anim.item_message_outgoing));
        }
    }

    public static class IncomingMessageViewHolder extends MessageViewHolder {

        public IncomingMessageViewHolder(@NonNull ViewDataBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(@NonNull Message message) {
            ItemMessageIncomingBinding dataBinding = (ItemMessageIncomingBinding) this.dataBinding;
            dataBinding.setMessage(message);
            dataBinding.executePendingBindings();

            itemView.setAnimation(AnimationUtils
                    .loadAnimation(itemView.getContext(), R.anim.item_message_incoming));
        }
    }
}
