package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.HistoryMessage;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.PollChatAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.SendMessageAnswer;
import com.elatesoftware.grandcapital.services.ChatService;
import com.elatesoftware.grandcapital.services.SignInService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;

import at.markushi.ui.CircleButton;

public class SupportFragment extends Fragment {

    public static final String TAG = "SupportFragment_LOG";
    public static final String WIDGET_ID = "0a9ecd18-54cc-4ecc-9b36-fabce04aa3b8";
    private static final int INTERVAL = 5000;

    private ScrollView svMessages;
    private EditText edMessage;
    private LinearLayout llMessages;

    private String caseId = null;
    private int lastIndex = -1;
    private boolean isChatCreated = false;
    private LinkedList<HistoryMessage> historyChat = new LinkedList<>();

    private GetChatBroadcastReceiver mChatBroadcastReceiver;

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
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_support));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();

        svMessages = (ScrollView) view.findViewById(R.id.sv_messages);
        CircleButton cbSendMessage = (CircleButton) view.findViewById(R.id.cb_send_message);
        edMessage = (EditText) view.findViewById(R.id.ed_message);
        llMessages = (LinearLayout) view.findViewById(R.id.ll_messages);

        cbSendMessage.setOnClickListener(v -> {
            Log.d(TAG, "cbSendMessage click");
            String message = edMessage.getText().toString();
            Intent intent = new Intent(getContext(), ChatService.class);
            if(!isChatCreated) {
                llMessages.removeAllViews();
                intent.putExtra(ChatService.ACTION, ChatService.CREATE_CHAT);
                intent.putExtra(ChatService.WIDGET_ID, WIDGET_ID);
                intent.putExtra(ChatService.VISITOR_MESSAGE, message);
                isChatCreated = true;
            } else {
                intent.putExtra(ChatService.ACTION, ChatService.SEND_MESSAGE_CHAT);
                intent.putExtra(ChatService.CASE_ID, caseId);
                intent.putExtra(ChatService.MESSAGE_TYPE, 1);
                intent.putExtra(ChatService.MESSAGE_BODY, message);
            }
            getActivity().startService(intent);
            addYourMessageInView(message, System.currentTimeMillis(), true);
            edMessage.setText("");
        });
        loadChatHistory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatBroadcastReceiver = new GetChatBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ChatService.ACTION_SERVICE_CHAT);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mChatBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(runnablePollChat);
        getActivity().unregisterReceiver(mChatBroadcastReceiver);
        super.onStop();
        if(!isChatCreated) {
            return;
        }
        Gson gson = new Gson();
        String historyStr = gson.toJson(historyChat, new TypeToken<LinkedList<HistoryMessage>>(){}.getType());
        CustomSharedPreferences.saveChatHistory(getContext(), historyStr);
    }

    private void addYourMessageInView(String message, long unix, boolean addInHistory) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_your_message, null);
        ((TextView) v.findViewById(R.id.tv_message)).setText(message);
        ((TextView) v.findViewById(R.id.tv_time)).setText(ConventDate.getChatDateByUnix(getContext(), unix));
        if(llMessages.getChildCount() > 0 && !isTheirMessageView(llMessages.getChildAt(llMessages.getChildCount() - 1))) {
            v.findViewById(R.id.img_logo).setVisibility(View.INVISIBLE);
        }
        v.setTag(R.string.is_their_message, false);
        llMessages.addView(v);
        svMessages.post(() -> svMessages.fullScroll(ScrollView.FOCUS_DOWN));
        if(addInHistory) {
            HistoryMessage historyMessage = new HistoryMessage(false, message, System.currentTimeMillis());
            historyChat.add(historyMessage);
        }
    }

    private void addTheirMessageInView(String message, long unix, boolean addInHistory) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_their_message, null);
        ((TextView) v.findViewById(R.id.tv_message)).setText(message);
        ((TextView) v.findViewById(R.id.tv_time)).setText(ConventDate.getChatDateByUnix(getContext(), unix));
        if(isTheirMessageView(llMessages.getChildAt(llMessages.getChildCount() - 1))) {
            v.findViewById(R.id.img_logo).setVisibility(View.INVISIBLE);
        }
        v.setTag(R.string.is_their_message, true);
        llMessages.addView(v);
        svMessages.post(() -> svMessages.fullScroll(ScrollView.FOCUS_DOWN));
        if(addInHistory) {
            HistoryMessage historyMessage = new HistoryMessage(true, message, System.currentTimeMillis());
            historyChat.add(historyMessage);
        }
    }

    private boolean isTheirMessageView(View v) {
        return ((boolean)v.getTag(R.string.is_their_message));
    }

    public class GetChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            String response = intent.getStringExtra(SignInService.RESPONSE);
            String action = intent.getStringExtra(ChatService.ACTION);
            switch(action){
                case ChatService.CREATE_CHAT:
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && ChatCreateAnswer.getInstance() != null){
                        Log.d(TAG, "ChatCreateAnswer: " + ChatCreateAnswer.getInstance());
                        caseId = ChatCreateAnswer.getInstance().getCaseId();
                        handler.postDelayed(runnablePollChat, INTERVAL);
                    } else {
                        CustomDialog.showDialogInfo(getActivity(),
                                getString(R.string.request_error_title),
                                getString(R.string.request_error_text));
                    }
                    break;
                case ChatService.POLL_CHAT:
                    Log.d(TAG, "POLL_CHAT");
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
                    } else {
                        CustomDialog.showDialogInfo(getActivity(),
                                getString(R.string.request_error_title),
                                getString(R.string.request_error_text));
                    }
                    break;
                case ChatService.SEND_MESSAGE_CHAT:
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && SendMessageAnswer.getInstance() != null) {
                        Log.d(TAG, SendMessageAnswer.getInstance() + "");
                    } else {
                        CustomDialog.showDialogInfo(getActivity(),
                                getString(R.string.request_error_title),
                                getString(R.string.request_error_text));
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
                addTheirMessageInView(messages.get(i).getText(), System.currentTimeMillis(), true);
            } else {
                break;
            }
        }
    }

    private void loadChatHistory() {
        Gson gson = new Gson();
        String historyStr = CustomSharedPreferences.getChatHistory(getContext());
        if(TextUtils.isEmpty(historyStr)) {
            return;
        }
        LinkedList<HistoryMessage> history = gson.fromJson(historyStr, new TypeToken<LinkedList<HistoryMessage>>(){}.getType());
        for(HistoryMessage msg : history) {
            if(msg.isTheir) {
                addTheirMessageInView(msg.text, msg.time, false);
            } else {
                addYourMessageInView(msg.text, msg.time, false);
            }
        }
    }
}
