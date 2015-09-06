package com.textures.component;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.textures.models.Point;

public class CanvasImageView extends ImageView {

    public static final int POINT_RADIUS = 15;

    private static final int ERROR = 100;
    private static final int STROKE_WIDTH = 5;
    private static int COLOR = Color.rgb(149, 229, 213);

    private Paint paint;
    private double scale;

    private Point topLeftPoint;
    private Point topRightPoint;
    private Point bottomLeftPoint;
    private Point bottomRightPoint;

    public CanvasImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        analyzePoints(event.getX(), event.getY());
        postInvalidate();
        return true;
    }

    private void setupPaint(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(COLOR);
            paint.setStrokeWidth(STROKE_WIDTH);
            topLeftPoint = new Point(POINT_RADIUS, POINT_RADIUS);
            topRightPoint = new Point(getWidth() - POINT_RADIUS, POINT_RADIUS);
            bottomLeftPoint = new Point(POINT_RADIUS, getHeight() - POINT_RADIUS);
            bottomRightPoint = new Point(getWidth() - POINT_RADIUS, getHeight() - POINT_RADIUS);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupPaint(canvas);

        canvas.drawCircle(topLeftPoint.getX(), topLeftPoint.getY(), POINT_RADIUS, paint);
        canvas.drawCircle(topRightPoint.getX(), topRightPoint.getY(), POINT_RADIUS, paint);
        canvas.drawCircle(bottomLeftPoint.getX(), bottomLeftPoint.getY(), POINT_RADIUS, paint);
        canvas.drawCircle(bottomRightPoint.getX(), bottomRightPoint.getY(), POINT_RADIUS, paint);

        canvas.drawLine(topLeftPoint.getX(), topLeftPoint.getY(), topRightPoint.getX(), topRightPoint.getY(), paint);
        canvas.drawLine(bottomLeftPoint.getX(), bottomLeftPoint.getY(), bottomRightPoint.getX(), bottomRightPoint.getY(), paint);

        canvas.drawLine(topLeftPoint.getX(), topLeftPoint.getY(), bottomLeftPoint.getX(), bottomLeftPoint.getY(), paint);
        canvas.drawLine(topRightPoint.getX(), topRightPoint.getY(), bottomRightPoint.getX(), bottomRightPoint.getY(), paint);

    }

    private void analyzePoints(float x, float y) {
        if (isThisPoint(topLeftPoint, x, y)) {
            topLeftPoint.setX(x);
            topLeftPoint.setY(y);
        }
        if (isThisPoint(topRightPoint, x, y)) {
            topRightPoint.setX(x);
            topRightPoint.setY(y);
        }
        if (isThisPoint(bottomLeftPoint, x, y)) {
            bottomLeftPoint.setX(x);
            bottomLeftPoint.setY(y);
        }
        if (isThisPoint(bottomRightPoint, x, y)) {
            bottomRightPoint.setX(x);
            bottomRightPoint.setY(y);
        }

    }

    private boolean isThisPoint(Point point, float x, float y) {
        if ((point.getX() + ERROR > x && point.getX() - ERROR < x) && (point.getY() + ERROR > y && point.getY() - ERROR < y)) {
            return true;
        } else {
            return false;
        }
    }

    public Point getTopLeftPoint() {
        return topLeftPoint;
    }

    public Point getTopRightPoint() {
        return topRightPoint;
    }

    public Point getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public Point getBottomRightPoint() {
        return bottomRightPoint;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
