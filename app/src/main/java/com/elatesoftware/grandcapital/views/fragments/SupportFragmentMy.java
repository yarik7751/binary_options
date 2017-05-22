package com.elatesoftware.grandcapital.views.fragments;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class SupportFragmentMy /*extends Fragment */{
/*
    public static final String TAG = "SupportFragment_LOG";
    private static final int INTERVAL = 4000;

    private EditText edMessage;
    private CircleButton btnSend;

    private String caseId = null;
    private int lastIndex = -1;
    private boolean isChatCreated = false;
    private List<MessageChat> mListMessageHistory =  new ArrayList<>();

    private AdapterForSupportChat mAdapter;
    private RecyclerView recyclerMessages;

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
        View v = inflater.inflate(R.layout.chart, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_support));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        TerminalFragment.getInstance().setEnabled(false);

        btnSend = (CircleButton) view.findViewById(R.id.btSend);
        edMessage = (EditText) view.findViewById(R.id.etMessage);
        recyclerMessages = (RecyclerView) view.findViewById(R.id.rvChat);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setStackFromEnd(true);
        recyclerMessages.setLayoutManager(mLayoutManager);
        btnSend.setOnClickListener(v -> {
            edMessage.setText(edMessage.getText().toString().trim());
            if(!TextUtils.isEmpty(edMessage.getText().toString())) {
                MessageChat historyMessage = new MessageChat(false, edMessage.getText().toString(), System.currentTimeMillis());
                Intent intent = new Intent(getContext(), ChatService.class);
                if (!isChatCreated) {
                    mListMessageHistory.clear();
                    mAdapter.setListMessages(mListMessageHistory);
                    mAdapter.notifyDataSetChanged();
                    MessageChat.deleteAll(MessageChat.class);
                    intent.putExtra(ChatService.ACTION, ChatService.CREATE_CHAT);
                    intent.putExtra(ChatService.WIDGET_ID, Const.CHART_WIDGET_ID);
                    intent.putExtra(ChatService.VISITOR_MESSAGE, historyMessage.getText());
                    isChatCreated = true;
                } else {
                    intent.putExtra(ChatService.ACTION, ChatService.SEND_MESSAGE_CHAT);
                    intent.putExtra(ChatService.CASE_ID, caseId);
                    intent.putExtra(ChatService.MESSAGE_TYPE, 1);
                    intent.putExtra(ChatService.MESSAGE_BODY, historyMessage.getText());
                }
                getActivity().startService(intent);
                edMessage.setText("");
        }
        mAdapter = new AdapterForSupportChat(mListMessageHistory, getContext());
        recyclerMessages.setAdapter(mAdapter);
        });
        List<MessageChat> history = MessageChat.listAll(MessageChat.class);
        mAdapter.setListMessages(history);
        mAdapter.notifyDataSetChanged();
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
        TerminalFragment.getInstance().setEnabled(true);
        super.onStop();
    }

    public class GetChatBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            String response = intent.getStringExtra(SignInService.RESPONSE);
            String action = intent.getStringExtra(ChatService.ACTION);
            switch (action) {
                case ChatService.CREATE_CHAT:
                    if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && ChatCreateAnswer.getInstance() != null) {

                        caseId = ChatCreateAnswer.getInstance().getCaseId();
                        handler.postDelayed(runnablePollChat, INTERVAL);
                        //GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_SUPPORT_SCREEN, Const.ANALYTICS_BUTTON_SEND_MESSAGE, message, null);
                    } else {
                        CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
                    }
                    break;
                case ChatService.POLL_CHAT:

                    break;
                case ChatService.SEND_MESSAGE_CHAT:

                    break;
                default:
                    break;
            }
        }
    }*/
}
