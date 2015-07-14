package com.textures.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.textures.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FileUtils {
    public static final String TAG = "FileUtils";
    private static String FOLDER = "Textures";

    public static File getOutputMediaFile() {

        File mediaStorageDir = createFolder(Constants.IMAGE_FOLDER);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public static void exportToPDF(String fileName, String documentText) throws FileNotFoundException, DocumentException {
        File directory = createFolder(Constants.FILE_FOLDER);
        File pdfFile = new File(directory.getPath() + File.separator + fileName + ".pdf");
        Log.i(TAG, pdfFile.toString());
        Document document = new Document();
        PdfWriter.getInstance(document,
                new FileOutputStream(pdfFile.getAbsoluteFile()));
        document.open();
        document.add(new Paragraph(documentText));
        document.close();
    }

    private static File createFolder(String path) {
        File mediaStorageDir = new File(path);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
            }
        }
        return mediaStorageDir;
    }

    public static Uri getOutputMediaFileUri() {
        File file = getOutputMediaFile();
        if (file != null) {
            return Uri.fromFile(getOutputMediaFile());
        } else {
            Log.e(TAG, "File not created");
            return null;
        }
    }

    public static List<String> getFiles() {
        List<String> files = new ArrayList<String>();
        File folder = createFolder(Constants.FILE_FOLDER);
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                files.add(file.getName());
            }
        }
        return files;
    }

    public static void exportToDoc(String fileName, String content) throws FileNotFoundException {

    }
}
