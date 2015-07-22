package com.textures.services;


import android.content.Context;
import android.util.Log;

import com.textures.Constants;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenCVService {
    private static final String TAG = "OpenCVService";

    private Context context;

    public OpenCVService(Context context) {
        this.context = context;
    }

    public void initial() {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.d(TAG, "OpenCV loaded successfully");
                        break;
                    }
                    default: {
                        super.onManagerConnected(status);
                        Log.d(TAG, "OpenCV loaded error");
                        break;
                    }
                }
            }
        };

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public String detectPaper(String imagePath) throws IOException {

        String mockImageResult = Constants.IMAGE_FOLDER + File.separator + "result.jpg";
        String mockImageResultCor = Constants.IMAGE_FOLDER + File.separator + "resultCor.jpg";
        Mat imgSource = Imgcodecs.imread(imagePath);
        List<Point> corners = findPoints(imgSource);

        Mat untouched = Imgcodecs.imread(imagePath);
        Imgproc.circle(untouched, corners.get(0), 20, new Scalar(255, 0, 0), 5);//blue
        Imgproc.circle(untouched, corners.get(1), 20, new Scalar(0, 0, 255), 5);//red
        Imgproc.circle(untouched, corners.get(2), 20, new Scalar(0, 255, 0), 5);//green
        Imgproc.circle(untouched, corners.get(3), 20, new Scalar(0, 0, 0), 5);//black
        Imgcodecs.imwrite(mockImageResultCor, untouched);


        Mat startM = Converters.vector_Point2f_to_Mat(corners);
        Mat result = warp(Imgcodecs.imread(imagePath), startM);


        Imgcodecs.imwrite(mockImageResult, result);


        return mockImageResult;

    }


    private List<Point> findPoints(Mat image) {
        Mat imgSource = prepareImage(image);

        return detectRectangle(imgSource);
    }


    private List<Point> detectRectangle(Mat imgSource) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint temp_contour = contours.get(0);
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        int maxAreaIdx = -1;
        double maxArea = -1;

        List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();
        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(temp_contour);
            if (contourarea > maxArea) {
                MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
                int contourSize = (int) temp_contour.total();
                Imgproc.approxPolyDP(new_mat, approxCurve, contourSize * 0.05, true);
                if (approxCurve.total() == 4) {
                    maxArea = contourarea;
                    largest_contours.add(temp_contour);
                    maxAreaIdx = idx;
                }
            }
        }

        String mockImageResult = Constants.IMAGE_FOLDER + File.separator + "test1.jpg";
        Mat imread = Imgcodecs.imread(mockImageResult);
        Imgproc.drawContours(imread, contours, maxAreaIdx, new Scalar(0, 255, 0), 10);
        Imgcodecs.imwrite(Constants.IMAGE_FOLDER + File.separator + "contour.jpg", imread);

        double[] temp_double;
        temp_double = approxCurve.get(0, 0);
        Point p1 = new Point(temp_double[0], temp_double[1]);
        temp_double = approxCurve.get(1, 0);
        Point p2 = new Point(temp_double[0], temp_double[1]);
        temp_double = approxCurve.get(2, 0);
        Point p3 = new Point(temp_double[0], temp_double[1]);
        temp_double = approxCurve.get(3, 0);
        Point p4 = new Point(temp_double[0], temp_double[1]);

        return Arrays.asList(new Point[]{p1, p2, p3, p4});
    }

    private Mat prepareImage(Mat image) {
        Mat imgSource = image;

        Imgproc.cvtColor(imgSource, imgSource, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(imgSource, imgSource, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);

        Imgproc.Canny(imgSource, imgSource, 10, 20, 3, true);

        Imgproc.GaussianBlur(imgSource, imgSource, new Size(5, 5), 5);
        return imgSource;
    }

    private Mat warp(Mat imageSource, Mat startM) {
        int resultWidth = 1280;
        int resultHeight = 680;

        Point ocvPOut4 = new Point(0, 0);
        Point ocvPOut1 = new Point(0, resultHeight);
        Point ocvPOut2 = new Point(resultWidth, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, 0);

        if (imageSource.height() > imageSource.width()) {
            // int temp = resultWidth;
            // resultWidth = resultHeight;
            // resultHeight = temp;

            ocvPOut3 = new Point(0, 0);
            ocvPOut4 = new Point(0, resultHeight);
            ocvPOut1 = new Point(resultWidth, resultHeight);
            ocvPOut2 = new Point(resultWidth, 0);
        }

        Mat outputMat = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);

        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);

        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(imageSource, outputMat, perspectiveTransform, new Size(resultWidth, resultHeight), Imgproc.INTER_CUBIC);

        return outputMat;
    }


}
