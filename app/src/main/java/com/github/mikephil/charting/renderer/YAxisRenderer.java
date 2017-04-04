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
import com.elatesoftware.grandcapital.views.items.chart.limitLines.BaseLimitLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.DealingLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.SocketLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.YDealingLine;
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

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans, yAxis);

        this.mYAxis = yAxis;
        if(mViewPortHandler != null)
            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;

        float[] positions = getTransformedPositions();

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        float xoffset = mYAxis.getXOffset();
        float yoffset = Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();

        AxisDependency dependency = mYAxis.getAxisDependency();
        YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float xPos = 0f;

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

        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

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
        // limitline labels
        List<LimitLine> limitLines = mYAxis.getLimitLines();
        float[] pts = new float[2];
        for (LimitLine l : limitLines) {
            if (l instanceof BaseLimitLine) {
                BaseLimitLine line = (BaseLimitLine) l;
/************************************************** LINE_CURRENT_SOCKET ************************************************************************/
                if (line instanceof SocketLine) {
                    SocketLine lineSocket = (SocketLine) line;
                    lineSocket.setLineWidth(1.0f);
                    lineSocket.setLineColor(Color.WHITE);
                    lineSocket.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);

                    Paint textPaint = mAxisLabelPaint;
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(mAxisLabelPaint.getTextSize() + 3f);
                    textPaint.setPathEffect(null);
                    textPaint.setTypeface(l.getTypeface());
                    textPaint.setStrokeWidth(0.5f);
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

                    Bitmap bitmapLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.whitevert);

                    float paddingVert = Utils.convertDpToPixel(15);
                    float paddingHoriz = Utils.convertDpToPixel(18);
                    float height = Utils.calcTextHeight(textPaint, strLabel);
                    float width = Utils.calcTextWidth(textPaint, strLabel);
                    float posY = pts[1] + height / 2;

                    float height_marker = height + paddingVert;
                    float width_marker = 200f;

                    if (bitmapLabel != null) {
                        bitmapLabel = Bitmap.createScaledBitmap(bitmapLabel, (int) width_marker, (int) height_marker, false);
                        c.drawBitmap(bitmapLabel, fixedPosition - paddingHoriz, posY - height_marker + paddingVert / 2, paint);
                        c.drawText(strLabel, fixedPosition - paddingHoriz / 3, posY, textPaint);
                        if (!lastSymbol.equals("")) {
                            textPaint.setColor(Color.parseColor("#FD3E3C"));
                            c.drawText(lastSymbol, fixedPosition - paddingHoriz / 3 + width, posY, textPaint);
                        }
                    }
/************************************************** LINE_CURRENT_DEALING ************************************************************************/
                } else if (line instanceof DealingLine) {
                    DealingLine lineDealing = (DealingLine) line;
                    lineDealing.setLineWidth(1.0f);
                    lineDealing.setLineColor(line.getLineColor());
                    lineDealing.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(line.getLineColor());
                    paint.setTextSize(mAxisLabelPaint.getTextSize());

                    Paint textPaint = mAxisLabelPaint;
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(mAxisLabelPaint.getTextSize());
                    textPaint.setPathEffect(null);
                    textPaint.setTypeface(l.getTypeface());
                    textPaint.setStrokeWidth(0.5f);
                    textPaint.setStyle(l.getTextStyle());

                    pts[1] = lineDealing.getLimit();
                    mTrans.pointValuesToPixel(pts);
                    String strLabel = lineDealing.getLabel().substring(0, 6);

                    Bitmap bitmapLabel = lineDealing.getmBitmapLabelY();
                    float paddingVert = Utils.convertDpToPixel(8);
                    float paddingHoriz = Utils.convertDpToPixel(10);
                    float height = Utils.calcTextHeight(textPaint, strLabel);
                    float width = Utils.calcTextWidth(textPaint, strLabel);
                    float posY = pts[1] + height / 2;

                    float height_marker = height + paddingVert;
                    float width_marker = width + paddingHoriz*2;

                    if (bitmapLabel != null) {
                        bitmapLabel = Bitmap.createScaledBitmap(bitmapLabel, (int) width_marker, (int) height_marker, false);
                        c.drawBitmap(bitmapLabel, fixedPosition - paddingHoriz/2, posY - height_marker + paddingVert / 2, paint);
                        c.drawText(strLabel, fixedPosition, posY, textPaint);
                    }
/************************************************** LINE_Y_DEALING************************************************************************/
                }else if (line instanceof YDealingLine){
                        YDealingLine lineDealing = (YDealingLine) line;
                        lineDealing.setLineWidth(0.0f);
                        lineDealing.setLineColor(Color.TRANSPARENT);
                        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(mAxisLabelPaint.getTextSize());

                        Paint textPaint = mAxisLabelPaint;
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(mYAxis.getTextSize());
                        textPaint.setPathEffect(null);
                        textPaint.setTypeface(l.getTypeface());
                        textPaint.setStrokeWidth(0.5f);
                        textPaint.setStyle(l.getTextStyle());

                        pts[1] = lineDealing.getLimit();
                        mTrans.pointValuesToPixel(pts);
                        OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                        String strLabelY  = ConventDate.getDifferenceDateToString(Long.valueOf(lineDealing.getmTimer()));

                        Bitmap iconLabelY = lineDealing.getmBitmapLabelY();
                        Bitmap iconClose = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.close_button);
                        Bitmap iconCMD;
                        if(order.getCmd() == 1){
                            iconCMD = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.down);
                        }else{
                            iconCMD = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.up);
                        }
                        float height_markerIconCMD = Utils.convertDpToPixel(12);
                        float width_markerIconCMD = Utils.convertDpToPixel(12);
                        float paddingVertIconLabelY = Utils.convertDpToPixel(12);
                        float paddingHorizIconLabelY = Utils.convertDpToPixel(14);
                        float heightStrLabelY = Utils.calcTextHeight(textPaint, strLabelY);
                        float widthStrLabelY  = Utils.calcTextWidth(textPaint, strLabelY);
                        float height_markerIconLabelY = heightStrLabelY  + paddingVertIconLabelY;
                        float width_markerIconLabelY = widthStrLabelY  + paddingHorizIconLabelY + width_markerIconCMD;

                        float posY = pts[1] + heightStrLabelY / 2;

                    if (iconLabelY != null && iconCMD != null) {
                        if(lineDealing.ismIsAmerican()){
                            float height_markerIconClose = Utils.convertDpToPixel(12);
                            float width_markerIconClose =  Utils.convertDpToPixel(12);
                            iconClose = Bitmap.createScaledBitmap(iconClose, (int) width_markerIconClose, (int) height_markerIconClose, false);
                            paddingHorizIconLabelY = Utils.convertDpToPixel(16);
                            width_markerIconLabelY = widthStrLabelY  + paddingHorizIconLabelY*6/5 + width_markerIconCMD +  width_markerIconClose;
                        }
                        /**create bitmaps*/
                        iconLabelY = Bitmap.createScaledBitmap(iconLabelY, (int) width_markerIconLabelY, (int) height_markerIconLabelY, false);
                        iconCMD = Bitmap.createScaledBitmap(iconCMD, (int) width_markerIconCMD, (int) height_markerIconCMD, false);

                        /**positionBitmap Y*/
                        c.drawBitmap(iconLabelY, fixedPosition - width_markerIconLabelY/2 - paddingHorizIconLabelY/3, posY - height_markerIconLabelY + paddingVertIconLabelY/2, paint);
                        c.drawBitmap(iconCMD, fixedPosition - width_markerIconLabelY/2, posY - height_markerIconLabelY/2, paint);
                        c.drawText(strLabelY, fixedPosition - width_markerIconLabelY/2 + width_markerIconCMD*5/4, posY - heightStrLabelY/7, textPaint);
                        if(lineDealing.ismIsAmerican()){
                            c.drawBitmap(iconClose, fixedPosition - width_markerIconLabelY/2 + width_markerIconCMD*3/2 + widthStrLabelY, posY - height_markerIconLabelY/2, paint);
                        }
                    }
                }
            }
        }
    }

    protected Path mRenderGridLinesPath = new Path();
    @Override
    public void renderGridLines(Canvas c) {

        if (!mYAxis.isEnabled())
            return;

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
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            // c.drawLines(pts, mLimitLinePaint);

            String label = "";     /** убирает текст над линией*/
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
