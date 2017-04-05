package com.elatesoftware.grandcapital.adapters.dealing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.elatesoftware.grandcapital.R;

class FragmentDealingViewHolder extends RecyclerView.ViewHolder {

    TextView mFirstColumn;
    TextView mSecondColumn;
    TextView mThirdColumn;
    TextView mFourthColumn;
    TextView mFifthColumn;
    ImageView mArrow, imgCloseDealing;
    LinearLayout llCloseDealing;
    SwipeLayout slDealing;


    FragmentDealingViewHolder(View v) {
        super(v);
        mFirstColumn = (TextView)itemView.findViewById(R.id.tv_dealing_header_col1_active);
        mSecondColumn = (TextView)itemView.findViewById(R.id.tv_dealing_header_col2_open);
        mThirdColumn = (TextView)itemView.findViewById(R.id.tv_dealing_col3_win);
        mFourthColumn = (TextView)itemView.findViewById(R.id.tv_dealing_col4_invest);
        mFifthColumn = (TextView)itemView.findViewById(R.id.tv_dealing_col5_income);
        mArrow = (ImageView) itemView.findViewById(R.id.imgv_dealing_header_col1_active);
        imgCloseDealing = (ImageView) itemView.findViewById(R.id.img_close_dealing);
        slDealing = (SwipeLayout) itemView.findViewById(R.id.sl_dealing);
        llCloseDealing = (LinearLayout) itemView.findViewById(R.id.ll_close_dealing);
    }
}
