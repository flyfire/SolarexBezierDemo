package com.solarexsoft.solarexbezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by houruhou on 06/02/2018.
 */

public class Bezier1 extends View {
    public static final String TAG = Bezier1.class.getSimpleName();
    // http://stackoverflow.com/questions/1734745/how-to-create-circle-with-b%C3%A9zier-curves
    // 圆可以由4段3阶贝塞尔曲线组成，用C来确定控制点的位置
    private static final float C = 0.551915024494f;

    private Paint mPaint;
    private int mCenterX, mCenterY;

    private PointF mCenter = new PointF(0, 0);
    private float mCircleRadius = 200;
    private float mDifference = mCircleRadius * C;

    // 数据点,顺时针记录圆形的4个数据点
    private PointF[] mData = new PointF[4];
    // 控制点，顺时针记录圆形的8个控制点
    private PointF[] mCtrl = new PointF[8];

    private float mDuration = 1000;
    private float mCurrent = 0;
    private float mCount = 100;
    private float mPiece = mDuration / mCount;

    public Bezier1(Context context) {
        this(context, null);
    }

    public Bezier1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Bezier1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);

        mData[0] = new PointF(0, mCircleRadius);
        mData[1] = new PointF(mCircleRadius, 0);
        mData[2] = new PointF(0, -mCircleRadius);
        mData[3] = new PointF(-mCircleRadius, 0);

        // y轴翻转了，和数学上的坐标轴一致
        mCtrl[0] = new PointF(mData[0].x + mDifference, mData[0].y);

        mCtrl[1] = new PointF(mData[1].x, mData[1].y + mDifference);

        mCtrl[2] = new PointF(mData[1].x, mData[1].y - mDifference);

        mCtrl[3] = new PointF(mData[2].x + mDifference, mData[2].y);

        mCtrl[4] = new PointF(mData[2].x - mDifference, mData[2].y);

        mCtrl[5] = new PointF(mData[3].x, mData[3].y - mDifference);

        mCtrl[6] = new PointF(mData[3].x, mData[3].y + mDifference);

        mCtrl[7] = new PointF(mData[0].x - mDifference, mData[0].y);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinateSystem(canvas);//坐标系
        canvas.translate(mCenterX, mCenterY);
        canvas.scale(1, -1);//翻转y轴
        drawAuxiliaryLine(canvas);

        //绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);

        Path path = new Path();
        path.moveTo(mData[0].x, mData[0].y);

        path.cubicTo(mCtrl[0].x, mCtrl[0].y, mCtrl[1].x, mCtrl[1].y, mData[1].x, mData[1].y);
        path.cubicTo(mCtrl[2].x, mCtrl[2].y, mCtrl[3].x, mCtrl[3].y, mData[2].x, mData[2].y);
        path.cubicTo(mCtrl[4].x, mCtrl[4].y, mCtrl[5].x, mCtrl[5].y, mData[3].x, mData[3].y);
        path.cubicTo(mCtrl[6].x, mCtrl[6].y, mCtrl[7].x, mCtrl[7].y, mData[0].x, mData[0].y);

        canvas.drawPath(path, mPaint);

        mCurrent += mPiece;
        if (mCurrent < mDuration) {
            mData[0].y -= 120 / mCount;
            mCtrl[3].y += 80 / mCount;
            mCtrl[4].y += 80 / mCount;
            mCtrl[2].x -= 20 / mCount;
            mCtrl[5].x += 20 / mCount;

            postInvalidateDelayed((long) mPiece);
        }
    }

    //绘制辅助线
    private void drawAuxiliaryLine(Canvas canvas) {
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        for (PointF data : mData) {
            canvas.drawPoint(data.x, data.y, mPaint);
        }
        for (PointF ctrl : mCtrl) {
            canvas.drawPoint(ctrl.x, ctrl.y, mPaint);
        }

        //绘制辅助线
        mPaint.setStrokeWidth(4);
        //顺时针方向
        canvas.drawLine(mData[0].x, mData[0].y, mCtrl[0].x, mCtrl[0].y, mPaint);
        canvas.drawLine(mCtrl[1].x, mCtrl[1].y, mData[1].x, mData[1].y, mPaint);
        canvas.drawLine(mData[1].x, mData[1].y, mCtrl[2].x, mCtrl[2].y, mPaint);
        canvas.drawLine(mCtrl[3].x, mCtrl[3].y, mData[2].x, mData[2].y, mPaint);
        canvas.drawLine(mData[2].x, mData[2].y, mCtrl[4].x, mCtrl[4].y, mPaint);
        canvas.drawLine(mCtrl[5].x, mCtrl[5].y, mData[3].x, mData[3].y, mPaint);
        canvas.drawLine(mData[3].x, mData[3].y, mCtrl[6].x, mCtrl[6].y, mPaint);
        canvas.drawLine(mCtrl[7].x, mCtrl[7].y, mData[0].x, mData[0].y, mPaint);

    }

    //绘制坐标系
    private void drawCoordinateSystem(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.scale(1, -1);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0, -2000, 0, 2000, paint);
        canvas.drawLine(-2000, 0, 2000, 0, paint);

        canvas.restore();
    }
}
