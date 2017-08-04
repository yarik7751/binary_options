package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.services.QuestionsService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QuestionFragment";

    private String mHeader;
    private String mContent;
    private String promotionsJsonData;
    private int numberQuestion = -1;

    public static String NUMBER_QUESTION = "NUMBER_QUESTION";
    public static String PROMOTIONS_JSON_DATA = "PROMOTIONS_JSON_DATA";
    public static String HEADER_TEXT = "header_text_question_fragment";
    public static String CONTENT_TEXT = "content_text_question_fragment";

    private GetResponseQuestionsBroadcastReceiver mQuestionsBroadcastReceiver;

    private static QuestionFragment fragment = null;
    public static QuestionFragment getInstance() {
        if (fragment == null) {
            fragment = new QuestionFragment();
        }
        return fragment;
    }

    private LinearLayout llProgress;
    private TextView contentText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "QuestionFragmen onCreateView");
        mHeader = getArguments().getString(HEADER_TEXT);
        mContent = getArguments().getString(CONTENT_TEXT);
        numberQuestion = getArguments().getInt(NUMBER_QUESTION, -1);
        promotionsJsonData = getArguments().getString(PROMOTIONS_JSON_DATA, null);
        mContent = ConventString.getContentQuestions(mContent);
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().setPageTitle("");
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
            BaseActivity.getToolbar().deselectAll();
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED_ACTIVITY);
        });
        BaseActivity.getToolbar().setPageTitle("");
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED_ACTIVITY);

        llProgress = (LinearLayout) getView().findViewById(R.id.layout_progress_bar);
        contentText  = (TextView) view.findViewById(R.id.fragment_question_content);
        //contentText.setText(mContent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mQuestionsBroadcastReceiver = new GetResponseQuestionsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(QuestionsService.ACTION_SERVICE_QUESTIONS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mQuestionsBroadcastReceiver, intentFilter);

        if(promotionsJsonData == null) {
            Intent pageIntent = new Intent(getContext(), QuestionsService.class);
            pageIntent.putExtra(QuestionsService.PAGE, 1);
            getActivity().startService(pageIntent);
            llProgress.setVisibility(View.VISIBLE);
        } else {
            JSONObject elem = null;
            String name = BinaryOptionAnswer.getInstance().getElements().get(numberQuestion).getShortDescriptionEn().toUpperCase();
            String longDescription = BinaryOptionAnswer.getInstance().getElements().get(numberQuestion).getLongDescriptionEn();
            try {
                elem = new JSONObject(promotionsJsonData);
                String language = ConventString.getLanguageForPromotions();
                String nameParam = "short_description_" + language;
                String longDescriptionParam = "long_description_" + language;

                name = TextUtils.isEmpty(elem.getString(nameParam)) ? name : elem.getString(nameParam);
                name = name.toUpperCase();
                longDescription = TextUtils.isEmpty(elem.getString(longDescriptionParam)) ? longDescription : elem.getString(longDescriptionParam);
                BaseActivity.getToolbar().setPageTitle(name);
                contentText.setText(longDescription);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mQuestionsBroadcastReceiver);
        super.onStop();
    }

    public class GetResponseQuestionsBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(QuestionsService.RESPONSE);
            llProgress.setVisibility(View.GONE);
            if(response != null && response.equals(Const.RESPONSE_CODE_SUCCESS)) {
                mHeader = QuestionsAnswer.getInstance().getQuestions().get(numberQuestion).getQuestion();
                mContent = QuestionsAnswer.getInstance().getQuestions().get(numberQuestion).getAnswer();
                mContent = ConventString.getContentQuestions(mContent);
                BaseActivity.getToolbar().setPageTitle(mHeader);
                contentText.setText(mContent);
            } else {
                CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
            }
        }
    }
}
