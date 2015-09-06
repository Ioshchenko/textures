package com.textures.convertors;


import com.textures.component.CanvasImageView;
import com.textures.models.Point;

public class CanvasPointPixelConvertor {
    private CanvasImageView canvasImageView;

    public CanvasPointPixelConvertor(CanvasImageView canvasImageView) {
        this.canvasImageView = canvasImageView;
    }

    public int getHeight() {
        return (int) (getBottomLeftPoint()[1] - getTopLeftPoint()[1]);
    }

    public int getWidth() {
        return (int) (getTopRightPoint()[0] - getTopLeftPoint()[0]);
    }

    public double[] getTopLeftPoint() {
        return scalePointCoordinates(canvasImageView.getTopLeftPoint());
    }

    public double[] getTopRightPoint() {
        return scalePointCoordinates(canvasImageView.getTopRightPoint());
    }

    public double[] getBottomLeftPoint() {
        return scalePointCoordinates(canvasImageView.getBottomLeftPoint());
    }

    public double[] getBottomRightPoint() {
        return scalePointCoordinates(canvasImageView.getBottomRightPoint());
    }

    private double[] scalePointCoordinates(Point point) {
        double scale = canvasImageView.getScale();
        return new double[]{point.getX() * scale, point.getY() * scale};
    }
}
