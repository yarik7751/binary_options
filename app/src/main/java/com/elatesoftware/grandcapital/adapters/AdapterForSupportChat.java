package com.elatesoftware.grandcapital.adapters;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class AdapterForSupportChat /*extends  RecyclerView.Adapter<RecyclerView.ViewHolder>*/ {
/*
    private List<MessageChat> mListMessages;
    private Context mContext;

    static final int VIEW_TYPE_USER = 0;
    static final int VIEW_TYPE_SUPPORT = 1;

    public AdapterForSupportChat(List<MessageChat> list, Context context) {
        this.mListMessages = list;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_USER:
                return new MessageUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false));
            case VIEW_TYPE_SUPPORT:
                return new MessageSupportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_support_message, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageBaseViewHolder baseHolder;
        final MessageChat message = getMessage(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER:
                baseHolder = (MessageUserViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageUserViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                ((MessageUserViewHolder) holder).tvMessage.setText(message.getText());
                break;
            case VIEW_TYPE_SUPPORT:
                baseHolder = (MessageSupportViewHolder) holder;
                baseHolder.bind(message, position);
                ((MessageSupportViewHolder) holder).tvDate.setText(ConventDate.getChatDateByUnix(mContext, message.getTime()));
                ((MessageSupportViewHolder) holder).tvMessage.setText(message.getText());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageChat msg = getMessage(position);
        if (msg.isTheir() == true) {
            return VIEW_TYPE_USER;
        }else {
            return VIEW_TYPE_SUPPORT;
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mListMessages.size();
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
    static class MessageBaseViewHolder extends RecyclerView.ViewHolder {
        public MessageBaseViewHolder(View itemView) {
            super(itemView);
        }
        public void bind(final MessageChat msg, final int position) {
        }
    }
    static class MessageUserViewHolder extends  MessageBaseViewHolder{

        private TextView tvMessage, tvDate;
        public MessageUserViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvDate = (TextView) view.findViewById(R.id.tv_time);
        }
    }
    static class MessageSupportViewHolder extends MessageBaseViewHolder {

        private  TextView tvMessage, tvDate;

        public MessageSupportViewHolder(View view) {
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvDate = (TextView) view.findViewById(R.id.tv_time);
        }
    }*/
}
