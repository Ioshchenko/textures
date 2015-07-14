package com.textures.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itextpdf.text.DocumentException;
import com.textures.Constants;
import com.textures.R;
import com.textures.component.ImportFileDialog;
import com.textures.services.TesseractService;
import com.textures.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class ProcessImageActivity extends BaseActivity {
	private static final String PDF = "PDF";
	private static final String DOC = "DOC";
    private static String TAG = "ProcessImageActivity";
    private String imagePath;
    private TesseractService tesseractService = new TesseractService();
    private ImportFileDialog importFileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);

        Intent intent = getIntent();
        String imageName = intent
                .getStringExtra(Constants.Parameters.IMAGE_NAME);

        if (imageName != null) {
            imagePath = Constants.IMAGE_FOLDER + "/" + imageName;
            loadImage(imagePath);
        }

        // final String imagePath = Constants.IMAGE_FOLDER +
        // "/IMG_20150511_121643.jpg";

        EditText textViewProcessing = initTextView();
        initProcessingButton(textViewProcessing);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.process_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import:
                importFileDialog();
                break;
            case R.id.action_export_pdf:
                showFileNameDialog(PDF);
                break;
            case R.id.action_export_word:
                showFileNameDialog(DOC);
                break;

        }
        return true;
    }

    private void export(String fileName, String type) {
    	Log.d(TAG, fileName);
        switch (type) {
            case PDF:
                exportToPdf(fileName);
                break;
            case DOC:
                exportToWord(fileName);
                break;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showFileNameDialog(final String type) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_file_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogFileName);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                export(userInput.getText().toString(), type);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

       
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void exportToWord(String fileName) {
        EditText textEdit = (EditText) findViewById(R.id.textEditProcessingText);
        try {
            FileUtils.exportToDoc(fileName, textEdit.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToPdf(String fileName) {
        try {
            EditText textEdit = (EditText) findViewById(R.id.textEditProcessingText);
            FileUtils.exportToPDF(fileName, textEdit.getText().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void importFileDialog() {
        File mPath = new File(Constants.IMAGE_FOLDER);
        importFileDialog = new ImportFileDialog(ProcessImageActivity.this, mPath);
        importFileDialog.setFileEndsWith(Arrays.asList("jpg", "jpeg", "png"));
        importFileDialog
                .addFileListener(new ImportFileDialog.FileSelectedListener() {
                    public void fileSelected(File file) {
                        imagePath = file.getAbsolutePath();
                        loadImage(imagePath);
                    }
                });
        importFileDialog.showDialog();
    }

    private void initProcessingButton(final EditText textViewProcessing) {
        ImageButton buttonProcessing = (ImageButton) findViewById(R.id.buttonStartProcessingImage);
        buttonProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProcessingImageTask(textViewProcessing).execute();
            }
        });
    }

    private EditText initTextView() {
        final EditText textEditProcessing = (EditText) findViewById(R.id.textEditProcessingText);
        textEditProcessing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        return textEditProcessing;
    }

    private void loadImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageProcessing = (ImageView) findViewById(R.id.processingImage);
            imageProcessing.setImageBitmap(bitmap);
        }
    }

    private String tesseractProcessing(String imagePath) {
        return tesseractService.proccesImage(
                BitmapFactory.decodeFile(imagePath), "eng");
    }

    private class ProcessingImageTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private String textProcessing;
        private EditText editText;

        private ProcessingImageTask(EditText editText) {
            this.editText = editText;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ProcessImageActivity.this, "",
                    "Processing. Please wait...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            textProcessing = tesseractProcessing(imagePath);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            editText.setText(textProcessing);
        }
    }

}
