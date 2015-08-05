package com.textures.component;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CanvasImageView extends ImageView {
    public static final int ERROR = 100;
    public static final int RADIUS = 15;
    public static final int STROKE_WIDTH = 5;
    public static int COLOR = Color.rgb(149, 229, 213);
    private Paint paint;

    private Point topLeftPoint;
    private Point topRightPoint;
    private Point bottomLeftPoint;
    private Point bottomRightPoint;


    public CanvasImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setupPaint(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(COLOR);
            paint.setStrokeWidth(STROKE_WIDTH);
            topLeftPoint = new Point(RADIUS, RADIUS);
            topRightPoint = new Point(canvas.getWidth() - RADIUS, RADIUS);
            bottomLeftPoint = new Point(RADIUS, canvas.getHeight() - RADIUS);
            bottomRightPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupPaint(canvas);

        canvas.drawCircle(topLeftPoint.x, topLeftPoint.y, RADIUS, paint);
        canvas.drawCircle(topRightPoint.x, topRightPoint.y, RADIUS, paint);
        canvas.drawCircle(bottomLeftPoint.x, bottomLeftPoint.y, RADIUS, paint);
        canvas.drawCircle(bottomRightPoint.x, bottomRightPoint.y, RADIUS, paint);

        canvas.drawLine(topLeftPoint.x, topLeftPoint.y, topRightPoint.x, topRightPoint.y, paint);
        canvas.drawLine(bottomLeftPoint.x, bottomLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y, paint);

        canvas.drawLine(topLeftPoint.x, topLeftPoint.y, bottomLeftPoint.x, bottomLeftPoint.y, paint);
        canvas.drawLine(topRightPoint.x, topRightPoint.y, bottomRightPoint.x, bottomRightPoint.y, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        analyzePoints(event.getX(), event.getY());
        postInvalidate();
        return true;
    }

    private void analyzePoints(float x, float y) {
        if (isThisPoint(topLeftPoint, x, y)) {
            topLeftPoint.x = x;
            topLeftPoint.y = y;
        }
        if (isThisPoint(topRightPoint, x, y)) {
            topRightPoint.x = x;
            topRightPoint.y = y;
        }
        if (isThisPoint(bottomLeftPoint, x, y)) {
            bottomLeftPoint.x = x;
            bottomLeftPoint.y = y;
        }
        if (isThisPoint(bottomRightPoint, x, y)) {
            bottomRightPoint.x = x;
            bottomRightPoint.y = y;
        }

    }

    private boolean isThisPoint(Point point, float x, float y) {
        if ((point.x + ERROR > x && point.x - ERROR < x) && (point.y + ERROR > y && point.y - ERROR < y)) {
            return true;
        } else {
            return false;
        }
    }


    class Point {
        float x;
        float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
