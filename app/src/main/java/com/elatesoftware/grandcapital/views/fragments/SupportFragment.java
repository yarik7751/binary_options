package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.AdapterForSupportChat;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.MessageChat;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.PollChatAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.SendMessageAnswer;
import com.elatesoftware.grandcapital.services.ChatService;
import com.elatesoftware.grandcapital.services.SignInService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class SupportFragment extends Fragment {

    public static final String TAG = "SupportFragment_LOG";
    private static final int INTERVAL = 3000;

    private ScrollView svMessages;
    private EditText edMessage;
    private LinearLayout llMessages;
    private CircleButton cbSendMessage;
    private RecyclerView rvMessages;

    private String caseId = null;
    private String message;
    private int lastIndex = -1;
    private boolean isChatCreated = false, isMessageLoading = false;

    private GetChatBroadcastReceiver mChatBroadcastReceiver;
    private AdapterForSupportChat adapter;

    private Handler handler = new Handler();
    private Runnable runnablePollChat = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getContext(), ChatService.class);
            intent.putExtra(ChatService.ACTION, ChatService.POLL_CHAT);
            intent.putExtra(ChatService.CASE_ID, caseId);
            getActivity().startService(intent);
            handler.postDelayed(runnablePollChat, INTERVAL);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_support, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_support));
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
            BaseActivity.getToolbar().deselectAll();
        });
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_support));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();

        cbSendMessage = (CircleButton) view.findViewById(R.id.cb_send_message);
        edMessage = (EditText) view.findViewById(R.id.ed_message);
        rvMessages = (RecyclerView) view.findViewById(R.id.rv_messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        cbSendMessage.setOnClickListener(v -> {
            if(!isMessageLoading) {
                isMessageLoading = true;
                edMessage.setText(edMessage.getText().toString().trim());
                if (!TextUtils.isEmpty(edMessage.getText().toString())) {
                    message = edMessage.getText().toString();
                    addYourMessageInView(message, System.currentTimeMillis());
                    Intent intent = new Intent(getContext(), ChatService.class);
                    if (!isChatCreated) {
                        try {
                            MessageChat.deleteAll(MessageChat.class);
                        } catch (SQLiteException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(ChatService.ACTION, ChatService.CREATE_CHAT);
                        intent.putExtra(ChatService.WIDGET_ID, Const.CHART_WIDGET_ID);
                        intent.putExtra(ChatService.VISITOR_MESSAGE, message);
                        isChatCreated = true;
                    } else {
                        intent.putExtra(ChatService.ACTION, ChatService.SEND_MESSAGE_CHAT);
                        intent.putExtra(ChatService.CASE_ID, caseId);
                        intent.putExtra(ChatService.MESSAGE_TYPE, 1);
                        intent.putExtra(ChatService.MESSAGE_BODY, message);
                    }
                    getActivity().startService(intent);
                    edMessage.setText("");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mChatBroadcastReceiver = new GetChatBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ChatService.ACTION_SERVICE_CHAT);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mChatBroadcastReceiver, intentFilter);
        loadChatHistory();
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(runnablePollChat);
        getActivity().unregisterReceiver(mChatBroadcastReceiver);
        if(adapter != null) {
            List<MessageChat> messages = adapter.getListMessages();
            for(MessageChat msg : messages) {
                if(!msg.isLoading() && !msg.isTyping()) {
                    msg.save();
                }
            }
        }
        super.onStop();
    }

    private void addYourMessageInView(String message, long unix) {
        MessageChat messageChat = new MessageChat(message, unix, false);
        if(adapter == null) {
            List<MessageChat> messages = new ArrayList<>();
            messages.add(messageChat);
            adapter = new AdapterForSupportChat(messages, getContext());
            rvMessages.setAdapter(adapter);
        } else {
            adapter.addMessage(messageChat);
        }
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void addTheirMessageInView(String message, long unix) {
        if(message.contains("/") && message.indexOf("/") == 0) {
            return;
        }
        MessageChat messageChat = new MessageChat(message, unix, true);
        adapter.addMessage(messageChat);
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void addTyping() {
        MessageChat messageChat = new MessageChat("", 0, true);
        messageChat.setTyping(true);
        adapter.addMessage(messageChat);
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private boolean isTheirMessageView(View v) {
        return ((boolean)v.getTag(R.string.is_their_message));
    }

    public class GetChatBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SignInService.RESPONSE);
            String action = intent.getStringExtra(ChatService.ACTION);
            switch(action){
                case ChatService.CREATE_CHAT:
                    isMessageLoading = false;
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && ChatCreateAnswer.getInstance() != null){
                        adapter.loadedMessages();
                        caseId = ChatCreateAnswer.getInstance().getCaseId();
                        handler.postDelayed(runnablePollChat, INTERVAL);
                        GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_SUPPORT_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_SEND_MESSAGE, message, null);
                    } else {
                        adapter.deleteLoading();
                        CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
                    }
                    break;
                case ChatService.POLL_CHAT:
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && PollChatAnswer.getInstance() != null &&
                            PollChatAnswer.getInstance() != null && !TextUtils.isEmpty(caseId)) {
                        ArrayList<PollChatAnswer.Message> messages = PollChatAnswer.getInstance().getMessageList();
                        if(messages.size() > 0) {
                            PollChatAnswer.Message lastMessage = messages.get(messages.size() - 1);
                            if(lastIndex < lastMessage.getIndex()) {
                                addTheirMessages(messages);
                                lastIndex = lastMessage.getIndex();
                            }
                        }
                        if(PollChatAnswer.getInstance().isAgentTyping()) {
                            adapter.deleteTyping();
                            addTyping();
                        } else {
                            adapter.deleteTyping();
                        }
                    } else {
                        CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
                    }
                    break;
                case ChatService.SEND_MESSAGE_CHAT:
                    isMessageLoading = false;
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && SendMessageAnswer.getInstance() != null) {
                        adapter.loadedMessages();
                        GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_SUPPORT_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_SEND_MESSAGE, message, null);
                    } else {
                        adapter.deleteLoading();
                        CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void addTheirMessages(ArrayList<PollChatAnswer.Message> messages) {
        for(int i = messages.size() - 1; i >= 0; i--) {
            if(lastIndex < messages.get(i).getIndex()) {
                addTheirMessageInView(messages.get(i).getText(), System.currentTimeMillis());
            } else {
                break;
            }
        }
    }

    private void loadChatHistory() {
        try {
            List<MessageChat> history = MessageChat.listAll(MessageChat.class);
            for (MessageChat msg : history) {
                msg.setLoading(false);
            }
            adapter = new AdapterForSupportChat(history, getContext());
            rvMessages.setAdapter(adapter);
            adapter = null;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}
