
package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.elatesoftware.grandcapital.views.items.chart.limit_lines.CustomBaseLimitLine;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.util.List;

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;
    float[] mLimitLineSegmentsBuffer = new float[4];
    private Path mLimitLinePath = new Path();
    protected Path mRenderGridLinesPath = new Path();
    protected float[] mRenderGridLinesBuffer = new float[2];
    protected RectF mGridClippingRect = new RectF();
    protected float[] mRenderLimitLinesBuffer = new float[2];
    protected RectF mLimitLineClippingRect = new RectF();

    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, trans, xAxis);

        this.mXAxis = xAxis;

        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextAlign(Align.CENTER);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }

    protected void setupGridPaint() {
        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());
    }

    @Override
    public void computeAxis(float min, float max, boolean inverted) {

        // calculate the starting and entry point of the y-labels (depending on
        // zoom / contentrect bounds)
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutX()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentRight(), mViewPortHandler.contentTop());

            if (inverted) {

                min = (float) p2.x;
                max = (float) p1.x;
            } else {

                min = (float) p1.x;
                max = (float) p2.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        computeAxisValues(min, max);
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);
        computeSize();
    }

    protected void computeSize() {

        String longest = mXAxis.getLongestLabel();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, longest);

        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");

        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(
                labelWidth,
                labelHeight,
                mXAxis.getLabelRotationAngle());


        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        FSize.recycleInstance(labelRotatedSize);
        FSize.recycleInstance(labelSize);
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yoffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0, 0);
        if (mXAxis.getPosition() == XAxisPosition.TOP) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() - yoffset, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() + yoffset + mXAxis.mLabelRotatedHeight, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF);

        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() - yoffset - mXAxis.mLabelRotatedHeight, pointF);

        } else { // BOTH SIDED
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mViewPortHandler.contentTop() - yoffset, pointF);
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF);
        }
        MPPointF.recycleInstance(pointF);
    }

    @Override
    public void renderAxisLine(Canvas c) {

        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        if (mXAxis.getPosition() == XAxisPosition.TOP
                || mXAxis.getPosition() == XAxisPosition.TOP_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentTop(), mAxisLinePaint);
        }

        if (mXAxis.getPosition() == XAxisPosition.BOTTOM
                || mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE
                || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            c.drawLine(mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();
        float[] positions = new float[mXAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }
        mTrans.pointValuesToPixel(positions);
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPortHandler.isInBoundsX(x)) {
                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
                    // avoid clipping of the last
                    if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        if (width > mViewPortHandler.offsetRight() * 2 && x + width > mViewPortHandler.getChartWidth()) {
                            x -= width / 2;
                        }
                        // avoid clipping of the first
                    } else if (i == 0) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        x += width / 2;
                    }
                }
                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);

                List<LimitLine> limitLines = mXAxis.getLimitLines();
                float[] pts = new float[2];

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);

                Paint textPaint = mAxisLabelPaint;
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(mAxisLabelPaint.getTextSize());
                textPaint.setPathEffect(null);
                textPaint.setStrokeWidth(0.5f);

                for (LimitLine l : limitLines) {
                    if (l instanceof CustomBaseLimitLine) {
                        CustomBaseLimitLine line = (CustomBaseLimitLine) l;
                        OrderAnswer orderAnswer = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                        Bitmap iconLabel = line.getmBitmap();

                        line.setLineWidth(1.0f);
                        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);

                        pts[0] = l.getLimit();
                        mTrans.pointValuesToPixel(pts);
                        String strLabel = String.valueOf(ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(line.getLimit())));

                        float paddingVert = Utils.convertDpToPixel(35);
                        float paddingHoriz = Utils.convertDpToPixel(5);
                        float height = Utils.calcTextHeight(textPaint, strLabel);
                        float width = Utils.calcTextWidth(textPaint, strLabel);
                        float posX = pts[0];
                        float height_marker = height + paddingVert;
                        float width_marker = width + paddingHoriz;

                        if (line.getTypeLimitLine().equals(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS)) {

                        }else if(line.getTypeLimitLine().equals(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE)){

                        }
                        if (iconLabel != null) {
                            iconLabel = Bitmap.createScaledBitmap(iconLabel, (int) width_marker, (int) height_marker, false);
                            c.drawBitmap(iconLabel, posX - width_marker / 2, pos - height_marker / 4, paint);
                            c.drawText(strLabel, posX, pos + paddingVert/4, textPaint);
                        }
                    }
                }
            }
        }
    }

    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        Utils.drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }

    @Override
    public void renderGridLines(Canvas c) {

        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        int clipRestoreCount = c.save();
        c.clipRect(getGridClippingRect());

        if (mRenderGridLinesBuffer.length != mAxis.mEntryCount * 2) {
            mRenderGridLinesBuffer = new float[mXAxis.mEntryCount * 2];
        }
        float[] positions = mRenderGridLinesBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);

        setupGridPaint();

        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        for (int i = 0; i < positions.length; i += 2) {

            drawGridLine(c, positions[i], positions[i + 1], gridLinePath);
        }

        c.restoreToCount(clipRestoreCount);
    }

    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }

    /**
     * Draws the grid line at the specified position using the provided path.
     *
     * @param c
     * @param x
     * @param y
     * @param gridLinePath
     */
    protected void drawGridLine(Canvas c, float x, float y, Path gridLinePath) {

        gridLinePath.moveTo(x, mViewPortHandler.contentBottom());
        gridLinePath.lineTo(x, mViewPortHandler.contentTop());

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint);

        gridLinePath.reset();
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = mXAxis.getLimitLines();
        if (limitLines == null || limitLines.size() <= 0)
            return;
        float[] position = mRenderLimitLinesBuffer;
        position[0] = 0;
        position[1] = 0;

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled())
                continue;

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(-l.getLineWidth(), 0.f);
            c.clipRect(mLimitLineClippingRect);

            position[0] = l.getLimit();
            position[1] = 0.f;

            mTrans.pointValuesToPixel(position);

            renderLimitLineLine(c, l, position);
            renderLimitLineLabel(c, l, position, 2.f + l.getYOffset());

            c.restoreToCount(clipRestoreCount);
        }
    }

    public void renderLimitLineLine(Canvas c, LimitLine limitLine, float[] position) {
        mLimitLineSegmentsBuffer[0] = position[0];
        mLimitLineSegmentsBuffer[1] = mViewPortHandler.contentTop();
        mLimitLineSegmentsBuffer[2] = position[0];
        mLimitLineSegmentsBuffer[3] = mViewPortHandler.contentBottom();

        mLimitLinePath.reset();
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1]);
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3]);
        mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
        mLimitLinePaint.setStyle(Paint.Style.STROKE);

        mLimitLinePaint.setColor(limitLine.getLineColor());
/*
        CustomBaseLimitLine line = (CustomBaseLimitLine) limitLine;
        OrderAnswer orderAnswer = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
        if(orderAnswer.getCmd() == 0 && orderAnswer.getOpenPrice() < TerminalFragment.getInstance().mCurrentValueY ||
                orderAnswer.getCmd() == 1 && orderAnswer.getOpenPrice() > TerminalFragment.getInstance().mCurrentValueY ){
            mLimitLinePaint.setColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green));
            line.setLineColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green));
        }else{
            mLimitLinePaint.setColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart));
            line.setLineColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart));
        }
*/
        c.drawPath(mLimitLinePath, mLimitLinePaint);
    }

    public void renderLimitLineLabel(Canvas c, LimitLine limitLine, float[] position, float yOffset) {
        String label = limitLine.getLabel();
        // if drawing the limit-value label is enabled
        if (label != null && !label.equals("")) {
            mLimitLinePaint.setStyle(limitLine.getTextStyle());
            mLimitLinePaint.setPathEffect(null);
            mLimitLinePaint.setColor(limitLine.getTextColor());
            mLimitLinePaint.setStrokeWidth(0.5f);
            mLimitLinePaint.setTextSize(limitLine.getTextSize());

            float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();
            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
/*
            CustomBaseLimitLine line = (CustomBaseLimitLine) limitLine;
            OrderAnswer orderAnswer = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
            Bitmap iconLabel = null;
            if(orderAnswer.getCmd() == 0 && orderAnswer.getOpenPrice() < line.getCurrentY() ||
                    orderAnswer.getCmd() == 1 && orderAnswer.getOpenPrice() > line.getCurrentY() ){
                iconLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_vert);
                line.setLineColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green));
            }else{
                iconLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_vert);
                line.setLineColor(GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart));
            }
            line.setLineWidth(1.0f);
            line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);

            Paint textPaint = mAxisLabelPaint;
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(mAxisLabelPaint.getTextSize());
            textPaint.setPathEffect(null);
            textPaint.setStrokeWidth(0.5f);
            float[] pts = new float[2];
            pts[0] = limitLine.getLimit();
            mTrans.pointValuesToPixel(pts);
            String strLabel = String.valueOf(ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(line.getLimit())));

            float paddingVert = Utils.convertDpToPixel(35);
            float paddingHoriz = Utils.convertDpToPixel(5);
            float height = Utils.calcTextHeight(textPaint, strLabel);
            float width = Utils.calcTextWidth(textPaint, strLabel);
            float posX = pts[0];
            float height_marker = height + paddingVert;
            float width_marker = width + paddingHoriz;

            if (line.getTypeLimitLine().equals(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS)) {

            }else if(line.getTypeLimitLine().equals(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE)){

            }
            if (iconLabel != null) {
                iconLabel = Bitmap.createScaledBitmap(iconLabel, (int) width_marker, (int) height_marker, false);
                c.drawBitmap(iconLabel, posX - width_marker / 2, yOffset - height_marker / 4, paint);
                c.drawText(strLabel, posX, yOffset + paddingVert/4, textPaint);
            }*/
/*
           if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                mLimitLinePaint.setTextAlign(Align.LEFT);
                c.drawText(label, position[0] + xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint.setTextAlign(Align.LEFT);
                c.drawText(label, position[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                mLimitLinePaint.setTextAlign(Align.RIGHT);
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else {
                mLimitLinePaint.setTextAlign(Align.RIGHT);
                c.drawText(label, position[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            }*/
        }
    }
}
