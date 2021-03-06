package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.items.limitLines.ActiveDealingLine;
import com.elatesoftware.grandcapital.views.items.limitLines.BaseLimitLine;
import com.elatesoftware.grandcapital.views.items.limitLines.SocketLine;
import com.elatesoftware.grandcapital.views.items.limitLines.YDealingLine;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.util.List;

public class YAxisRenderer extends AxisRenderer {

    protected YAxis mYAxis;
    protected Paint mZeroLinePaint;

    private static Paint paintLine;

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans, yAxis);
        this.mYAxis = yAxis;
        if(mViewPortHandler != null){
            mAxisLabelPaint.setColor(Color.BLACK);
        }
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);

            paintLine = new Paint();
            paintLine.setStyle(Paint.Style.FILL);
            paintLine.setTextSize(mYAxis.getTextSize());
        }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled()){
            return;
        }
        float[] positions = getTransformedPositions();

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        float xoffset = mYAxis.getXOffset();
        float yoffset = Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();

        AxisDependency dependency = mYAxis.getAxisDependency();
        YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float xPos;
        if (dependency == AxisDependency.LEFT) {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.offsetLeft() - xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.offsetLeft() + xoffset;
            }
        } else {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.contentRight() + xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.contentRight() - xoffset;
            }
        }
        drawYLabels(c, xPos, positions, yoffset);
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled()){
            return;
        }
        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        } else {
            c.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(),
                    mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        for (int i = 0; i < mYAxis.mEntryCount; i++) {
            String text = mYAxis.getFormattedLabel(i);
            if (!mYAxis.isDrawTopYLabelEntryEnabled() && i >= mYAxis.mEntryCount - 1) {
                return;
            }
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
        /** limitline labels*/
        List<LimitLine> limitLines = mYAxis.getLimitLines();
        float[] pts = new float[2];

        SocketLine socketLine = null;
        YDealingLine yDealingLine = null;
        ActiveDealingLine dealingLine = null;

        for(LimitLine lineLimit: limitLines){
            if(lineLimit instanceof SocketLine){
                socketLine = (SocketLine) lineLimit;
            }else if(lineLimit instanceof ActiveDealingLine){
                dealingLine = (ActiveDealingLine) lineLimit;
            }else if(lineLimit instanceof YDealingLine && ((YDealingLine) lineLimit).ismIsActive()){
                yDealingLine = (YDealingLine) lineLimit;
            }
        }
        /*********remove****************/
        if(socketLine != null){
            limitLines.remove(socketLine);
        }
        if(dealingLine != null){
            limitLines.remove(dealingLine);
        }
        if(yDealingLine != null){
            limitLines.remove(yDealingLine);
        }
        /********add****************/
        if(socketLine != null){
            limitLines.add(socketLine);
        }
        if(dealingLine != null){
            limitLines.add(dealingLine);
        }
        if(yDealingLine != null){
            limitLines.add(yDealingLine);
        }
        /********draw***************/
        for (LimitLine l : limitLines) {
            if (l instanceof BaseLimitLine) {
                BaseLimitLine line = (BaseLimitLine) l;
/************************************************** LINE_CURRENT_SOCKET ************************************************************************/
                if (line instanceof SocketLine) {
                    SocketLine lineSocket = (SocketLine) line;
                    lineSocket.setLineWidth(1.0f);
                    lineSocket.setLineColor(Color.WHITE);
                    lineSocket.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    paintLine.setColor(Color.WHITE);

                    Paint textPaint = mAxisLabelPaint;
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(mYAxis.getTextSize() + 2f);
                    textPaint.setPathEffect(null);
                    textPaint.setTypeface(l.getTypeface());
                    textPaint.setStrokeWidth(1f);
                    textPaint.setStyle(l.getTextStyle());

                    pts[1] = lineSocket.getLimit();
                    mTrans.pointValuesToPixel(pts);
                    String strLabel = lineSocket.getLabel();
                    String lastSymbol = "";
                    if (strLabel.length() > 3) {
                        if(strLabel.length() > 7){
                            strLabel = strLabel.substring(0, 6);
                        }
                        lastSymbol = strLabel.substring(strLabel.length() - 1, strLabel.length());
                        strLabel = strLabel.substring(0, strLabel.length() - 1);
                    }

                    Bitmap bitmapLabel = BaseLimitLine.bitmapLabel;

                    float paddingVert = Utils.convertDpToPixel(15);
                    float paddingHoriz = Utils.convertDpToPixel(18);
                    float height = Utils.calcTextHeight(textPaint, strLabel);
                    float width = Utils.calcTextWidth(textPaint, strLabel);
                    float posY = pts[1] + height / 2;

                    float height_marker = height + paddingVert;
                    float width_marker = 200f;

                    if (bitmapLabel != null) {
                        bitmapLabel = Bitmap.createScaledBitmap(bitmapLabel, (int) width_marker, (int) height_marker, false);
                        c.drawBitmap(bitmapLabel, fixedPosition - paddingHoriz, posY - height_marker + paddingVert / 2, paintLine);
                        c.drawText(strLabel, fixedPosition - paddingHoriz / 3, posY, textPaint);
                        if (!lastSymbol.equals("")) {
                            textPaint.setColor(Color.parseColor("#FD3E3C"));
                            c.drawText(lastSymbol, fixedPosition - paddingHoriz / 3 + width, posY, textPaint);
                        }
                        lineSocket.setMaxWeightCanvasLabel(0);
                    }
/************************************************** LINE_CURRENT_DEALING ************************************************************************/
                } else if (line instanceof ActiveDealingLine) {
                    ActiveDealingLine lineDealing = (ActiveDealingLine) line;
                    lineDealing.setLineWidth(1.0f);
                    lineDealing.setLineColor(line.getLineColor());
                    lineDealing.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    paintLine.setColor(line.getLineColor());

                    Paint textPaint = mAxisLabelPaint;
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(mYAxis.getTextSize());
                    textPaint.setPathEffect(null);
                    textPaint.setTypeface(l.getTypeface());
                    textPaint.setStrokeWidth(0.5f);
                    textPaint.setStyle(l.getTextStyle());

                    pts[1] = lineDealing.getLimit();
                    mTrans.pointValuesToPixel(pts);
                    String strLabel = lineDealing.getLabel();
                    if(strLabel != null && strLabel.length() > 7){
                        strLabel = strLabel.substring(0, 6);
                    }
                    Bitmap bitmapLabel = lineDealing.getmBitmapLabelY();
                    float paddingVert = Utils.convertDpToPixel(8);
                    float paddingHoriz = Utils.convertDpToPixel(10);
                    float height = Utils.calcTextHeight(textPaint, strLabel);
                    float width = Utils.calcTextWidth(textPaint, strLabel);
                    float posY = pts[1] + height / 2;

                    float height_marker = height + paddingVert;
                    float width_marker = width + paddingHoriz*2;

                    if (bitmapLabel != null && strLabel != null) {
                        bitmapLabel = Bitmap.createScaledBitmap(bitmapLabel, (int) width_marker, (int) height_marker, false);
                        c.drawBitmap(bitmapLabel, fixedPosition - paddingHoriz/2, posY - height_marker + paddingVert / 2, paintLine);
                        c.drawText(strLabel, fixedPosition, posY, textPaint);
                        lineDealing.setMaxWeightCanvasLabel(0);
                    }
/************************************************** LINE_Y_DEALING************************************************************************/
                }else if (line instanceof YDealingLine){
                        YDealingLine lineDealing = (YDealingLine) line;
                        lineDealing.setLineWidth(0.0f);
                        lineDealing.setLineColor(Color.TRANSPARENT);
                        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                        paintLine.setColor(Color.WHITE);

                        Paint textPaint = mAxisLabelPaint;
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(mYAxis.getTextSize());
                        textPaint.setPathEffect(null);
                        textPaint.setTypeface(l.getTypeface());
                        textPaint.setStrokeWidth(0.5f);
                        textPaint.setStyle(l.getTextStyle());

                        pts[1] = lineDealing.getLimit();
                        mTrans.pointValuesToPixel(pts);
                        Bitmap iconLabelY = lineDealing.getBitmapY();

                        float posY = pts[1];
                        if (iconLabelY != null) {
                            /**positionBitmap Y*/
                            c.drawBitmap(iconLabelY, fixedPosition - iconLabelY.getWidth() * 2/3, posY - iconLabelY.getHeight()/2, paintLine);
                            lineDealing.setMaxWeightCanvasLabel(c.getMaximumBitmapWidth());
                        }
                }
            }
        }
    }

    protected Path mRenderGridLinesPath = new Path();

    @Override
    public void renderGridLines(Canvas c) {
        if (!mYAxis.isEnabled()) {
            return;
        }
        if (mYAxis.isDrawGridLinesEnabled()) {
            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());
            float[] positions = getTransformedPositions();

            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();
            // draw the grid
            for (int i = 0; i < positions.length; i += 2) {
                // draw a path because lines don't support dashing on lower android versions
                c.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                gridLinePath.reset();
            }
            c.restoreToCount(clipRestoreCount);
        }
        if (mYAxis.isDrawZeroLineEnabled()) {
            drawZeroLine(c);
        }
    }
    protected RectF mGridClippingRect = new RectF();
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(0.f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }
    /**
     * Calculates the path for a grid line.
     *
     * @param p
     * @param i
     * @param positions
     * @return
     */
    protected Path linePath(Path p, int i, float[] positions) {
        p.moveTo(mViewPortHandler.offsetLeft(), positions[i + 1]);
        p.lineTo(mViewPortHandler.contentRight(), positions[i + 1]);
        return p;
    }

    protected float[] mGetTransformedPositionsBuffer = new float[2];
    /**
     * Transforms the values contained in the axis entries to screen pixels and returns them in form of a float array
     * of x- and y-coordinates.
     *
     * @return
     */
    protected float[] getTransformedPositions() {
        if(mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2){
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }
        float[] positions = mGetTransformedPositionsBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            // only fill y values, x values are not needed for y-labels
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

    protected Path mDrawZeroLinePath = new Path();
    protected RectF mZeroLineClippingRect = new RectF();

    /**
     * Draws the zero line.
     */
    protected void drawZeroLine(Canvas c) {

        int clipRestoreCount = c.save();
        mZeroLineClippingRect.set(mViewPortHandler.getContentRect());
        mZeroLineClippingRect.inset(0.f, -mYAxis.getZeroLineWidth());
        c.clipRect(mZeroLineClippingRect);

        // draw zero line
        MPPointD pos = mTrans.getPixelForValues(0f, 0f);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());

        Path zeroLinePath = mDrawZeroLinePath;
        zeroLinePath.reset();

        zeroLinePath.moveTo(mViewPortHandler.contentLeft(), (float) pos.y);
        zeroLinePath.lineTo(mViewPortHandler.contentRight(), (float) pos.y);

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint);

        c.restoreToCount(clipRestoreCount);
    }

    protected Path mRenderLimitLines = new Path();
    protected float[] mRenderLimitLinesBuffer = new float[2];
    protected RectF mLimitLineClippingRect = new RectF();
    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = mYAxis.getLimitLines();
        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = mRenderLimitLinesBuffer;
        pts[0] = 0;
        pts[1] = 0;
        Path limitLinePath = mRenderLimitLines;
        limitLinePath.reset();

        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine l = limitLines.get(i);
            if (!l.isEnabled())
                continue;

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(0.f, -l.getLineWidth());
            c.clipRect(mLimitLineClippingRect);

            mLimitLinePaint.setStrokeWidth(1.0f);
            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            if(l instanceof YDealingLine){
                mLimitLinePaint.setColor(Color.TRANSPARENT);
            }else{
                mLimitLinePaint.setColor(l.getLineColor());
            }

            mLimitLinePaint.setStrokeWidth(l.getLineWidth());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            // c.drawLines(pts, mLimitLinePaint);

            String label = "";
            // if drawing the limit-value label is enabled
            if (label != null && !label.equals("")) {

                mLimitLinePaint.setStyle(l.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(l.getTextColor());
                mLimitLinePaint.setTypeface(l.getTypeface());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(l.getTextSize());

                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                float xOffset = Utils.convertDpToPixel(4f) + l.getXOffset();
                float yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset();

                final LimitLine.LimitLabelPosition position = l.getLabelPosition();

                if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    c.drawText(label,
                            mViewPortHandler.contentRight() - xOffset,
                            pts[1] - yOffset + labelLineHeight, mLimitLinePaint);

                } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    c.drawText(label,
                            mViewPortHandler.contentRight() - xOffset,
                            pts[1] + yOffset, mLimitLinePaint);

                } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    c.drawText(label,
                            mViewPortHandler.contentLeft() + xOffset,
                            pts[1] - yOffset + labelLineHeight, mLimitLinePaint);
                } else {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    c.drawText(label,
                            mViewPortHandler.offsetLeft() + xOffset,
                            pts[1] + yOffset, mLimitLinePaint);
                }
            }
            c.restoreToCount(clipRestoreCount);
        }
    }
}
