package com.elatesoftware.grandcapital.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.MessageChat;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.ConventDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class AdapterForSupportChat extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageChat> mListMessages;
    private Context mContext;

    static final int VIEW_TYPE_USER_FIRST = 0;
    static final int VIEW_TYPE_SUPPORT_FIRST = 1;
    static final int VIEW_TYPE_USER = 2;
    static final int VIEW_TYPE_SUPPORT = 3;
    static final int VIEW_TYPE_TYPING = 4;

    public AdapterForSupportChat(List<MessageChat> list, Context context) {
        this.mListMessages = list;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_USER_FIRST:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false));
            case VIEW_TYPE_SUPPORT_FIRST:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_their_message, parent, false));
            case VIEW_TYPE_USER:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message_second, parent, false));
            case VIEW_TYPE_SUPPORT:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_their_message_second, parent, false));
            case VIEW_TYPE_TYPING:
                return new MessageTypingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_their_typing, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageBaseViewHolder baseHolder;
        final MessageChat message = getMessage(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_FIRST:
            case VIEW_TYPE_USER: {
                baseHolder = (MessageUserViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageUserViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                TextView tvMessage = ((MessageUserViewHolder) holder).tvMessage;
                tvMessage.setText(message.getText());
                tvMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tvMessage.getLineCount();
                        if (lineCount > 1) {
                            ((MessageUserViewHolder) holder).llMessage.setOrientation(LinearLayout.VERTICAL);
                        }
                    }
                });
                ((MessageUserViewHolder) holder).pbLoading.setVisibility(getMessage(position).isLoading() ? View.VISIBLE : View.GONE);
            }
                break;
            case VIEW_TYPE_SUPPORT_FIRST:
            case VIEW_TYPE_SUPPORT: {
                baseHolder = (MessageSupportViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageSupportViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                TextView tvMessage = ((MessageSupportViewHolder) holder).tvMessage;
                tvMessage.setText(message.getText());
                tvMessage.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tvMessage.getLineCount();
                        if (lineCount  > 1) {
                            ((MessageSupportViewHolder) holder).llMessage.setOrientation(LinearLayout.VERTICAL);
                        }
                    }
                });
            }
                break;
            case VIEW_TYPE_TYPING:

                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageChat msg = getMessage(position);
        if(msg.isTyping()) {
            return VIEW_TYPE_TYPING;
        }
        if (msg.isTheir()) {
            if(position > 0 && mListMessages.get(position - 1).isTheir()) {
                return VIEW_TYPE_SUPPORT;
            } else {
                return VIEW_TYPE_SUPPORT_FIRST;
            }
        } else {
            if(position > 0 && !mListMessages.get(position - 1).isTheir()) {
                return VIEW_TYPE_USER;
            } else {
                return VIEW_TYPE_USER_FIRST;
            }
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mListMessages == null ? 0 : mListMessages.size();
    }

    public List<MessageChat> getListMessages() {
        return mListMessages;
    }

    MessageChat getMessage(int position){
        return mListMessages.get(position);
    }

    public void setListMessages(List<MessageChat> list){
        if(mListMessages == null){
            mListMessages = new ArrayList<>();
        }
         mListMessages.clear();
         mListMessages.addAll(list);
    }

    public void deleteTyping() {
        for(int i = mListMessages.size() - 1; i >= 0; i--) {
            if(mListMessages.get(i).isTyping()) {
                mListMessages.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void deleteLoading() {
        for(int i = mListMessages.size() - 1; i >= 0; i--) {
            if(mListMessages.get(i).isLoading()) {
                mListMessages.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void loadedMessages() {
        for(MessageChat msg : mListMessages) {
            if(!msg.isTheir() && msg.isLoading()) {
                msg.setLoading(false);
            }
        }
        notifyDataSetChanged();
    }

    public void addMessage(MessageChat message) {
        MessageChat lastMessage = mListMessages.size() == 0 ? null : getMessage(mListMessages.size() - 1);
        if(lastMessage != null) {
            if(lastMessage.isTyping() && message.isTyping()) {
                return;
            }
            if(lastMessage.isTyping() && message.isTheir()) {
                mListMessages.remove(mListMessages.size() - 1);
            }
            mListMessages.add(message);
            notifyDataSetChanged();
        }
    }

    static class MessageBaseViewHolder extends RecyclerView.ViewHolder {
        public MessageBaseViewHolder(View itemView) {
            super(itemView);
        }
        public void bind(final MessageChat msg, final int position) {
        }
    }

    static class MessageUserViewHolder extends  MessageBaseViewHolder{

        private TextView tvMessage, tvDate;
        private RelativeLayout rlTime;
        private LinearLayout llMessage;
        private ProgressBar pbLoading;

        public MessageUserViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvDate = (TextView) view.findViewById(R.id.tv_time);
            pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
            rlTime = (RelativeLayout) view.findViewById(R.id.rl_time);
            llMessage = (LinearLayout) view.findViewById(R.id.ll_message);
        }
    }

    static class MessageSupportViewHolder extends MessageBaseViewHolder {

        private  TextView tvMessage, tvDate;
        private RelativeLayout rlTime;
        private LinearLayout llMessage;

        public MessageSupportViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvDate = (TextView) view.findViewById(R.id.tv_time);
            rlTime = (RelativeLayout) view.findViewById(R.id.rl_time);
            llMessage = (LinearLayout) view.findViewById(R.id.ll_message);
        }
    }

    static class MessageTypingViewHolder extends MessageBaseViewHolder {

        public MessageTypingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
