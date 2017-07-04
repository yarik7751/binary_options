package com.elatesoftware.grandcapital.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.MessageChat;
import com.elatesoftware.grandcapital.utils.ConventDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class AdapterForSupportChat extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageChat> mListMessages;
    private Context mContext;

    private static final int VIEW_TYPE_USER_FIRST = 0;
    private static final int VIEW_TYPE_SUPPORT_FIRST = 1;
    private static final int VIEW_TYPE_USER_SECOND = 2;
    private static final int VIEW_TYPE_SUPPORT_SECOND = 3;
    private static final int VIEW_TYPE_TYPING = 4;
    private static final int VIEW_TYPE_USER_FIRST_ONE_LINE = 5;
    private static final int VIEW_TYPE_USER_SECOND_ONE_LINE = 6;
    private static final int VIEW_TYPE_SUPPORT_FIRST_ONE_LINE = 7;
    private static final int VIEW_TYPE_SUPPORT_SECOND_ONE_LINE = 8;

    public AdapterForSupportChat(List<MessageChat> list, Context context) {
        this.mListMessages = list;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_USER_FIRST:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user_first, parent, false));
            case VIEW_TYPE_USER_FIRST_ONE_LINE:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user_first_one_line, parent, false));
            case VIEW_TYPE_USER_SECOND:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user_second, parent, false));
            case VIEW_TYPE_USER_SECOND_ONE_LINE:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user_second_one_line, parent, false));
            case VIEW_TYPE_SUPPORT_FIRST:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_support_first, parent, false));
            case VIEW_TYPE_SUPPORT_FIRST_ONE_LINE:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_support_first_one_line, parent, false));
            case VIEW_TYPE_SUPPORT_SECOND:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_support_second, parent, false));
            case VIEW_TYPE_SUPPORT_SECOND_ONE_LINE:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_support_second_one_line, parent, false));
            case VIEW_TYPE_TYPING:
                return new MessageTypingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_typing, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageBaseViewHolder baseHolder;
        final MessageChat message = getMessage(position);
        holder.setIsRecyclable(false); //TODO запрещает использовать элементы повторно.!
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_FIRST_ONE_LINE:
            case VIEW_TYPE_USER_SECOND_ONE_LINE:
            case VIEW_TYPE_USER_FIRST:
            case VIEW_TYPE_USER_SECOND: {
                baseHolder = (MessageUserViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageUserViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                ((MessageUserViewHolder) holder).tvMessage.setText(message.getText());
                ((MessageUserViewHolder) holder).pbLoading.setVisibility(getMessage(position).isLoading() ? View.VISIBLE : View.GONE);
                ((MessageUserViewHolder) holder).tvDate.setVisibility(getMessage(position).isLoading() ? View.GONE : View.VISIBLE);
            }
                break;
            case VIEW_TYPE_SUPPORT_FIRST_ONE_LINE:
            case VIEW_TYPE_SUPPORT_SECOND_ONE_LINE:
            case VIEW_TYPE_SUPPORT_FIRST:
            case VIEW_TYPE_SUPPORT_SECOND: {
                baseHolder = (MessageSupportViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageSupportViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                ((MessageSupportViewHolder) holder).tvMessage.setText(message.getText());
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
                if(msg.getText() != null && (msg.getText().length() >= 30 || msg.getText().contains("\n"))){
                    return VIEW_TYPE_SUPPORT_SECOND;
                }else{
                    return VIEW_TYPE_SUPPORT_SECOND_ONE_LINE;
                }
            } else {
                if(msg.getText() != null && (msg.getText().length() >= 30 || msg.getText().contains("\n"))){
                    return VIEW_TYPE_SUPPORT_FIRST;
                }else{
                    return VIEW_TYPE_SUPPORT_FIRST_ONE_LINE;
                }
            }
        } else {
            if(position > 0 && !mListMessages.get(position - 1).isTheir()) {
                if(msg.getText() != null && (msg.getText().length() >= 30 || msg.getText().contains("\n"))){
                    return VIEW_TYPE_USER_SECOND;
                }else{
                    return VIEW_TYPE_USER_SECOND_ONE_LINE;
                }
            } else {
                if(msg.getText() != null && (msg.getText().length() >= 30 || msg.getText().contains("\n"))){
                    return VIEW_TYPE_USER_FIRST;
                }else{
                    return VIEW_TYPE_USER_FIRST_ONE_LINE;
                }
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

    private MessageChat getMessage(int position){
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
         MessageBaseViewHolder(View itemView) {
            super(itemView);
        }
         void bind(final MessageChat msg, final int position) {
        }
    }

   private static class MessageUserViewHolder extends  MessageBaseViewHolder{

        private TextView tvMessage, tvDate;
        private ProgressBar pbLoading;

         MessageUserViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tvContentMessage);
            tvDate = (TextView) view.findViewById(R.id.tvDateMessage);
            pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        }
    }

   private static class MessageSupportViewHolder extends MessageBaseViewHolder {

        private  TextView tvMessage, tvDate;

        MessageSupportViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tvContentMessage);
            tvDate = (TextView) view.findViewById(R.id.tvDateMessage);
        }
    }

   private static class MessageTypingViewHolder extends MessageBaseViewHolder {

        MessageTypingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
