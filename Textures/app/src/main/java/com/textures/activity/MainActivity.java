package com.textures.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.textures.Constants;
import com.textures.R;
import com.textures.component.FileListAdapter;
import com.textures.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ListView filesListView;
    private FileListAdapter adapter;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> files = FileUtils.getFiles();
        if (!files.isEmpty()) {
            ImageView imgView = (ImageView) findViewById(R.id.folder);
            imgView.setVisibility(View.GONE);
        }
        filesListView = (ListView) findViewById(R.id.fileList);
        adapter = new FileListAdapter(this, files);
        filesListView.setAdapter(adapter);

        filesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Log.d(TAG, item);
                viewPdf(item);
            }
        });

        Intent intent = new Intent(this, ImageDetectionActivity.class);
    }

    private void viewPdf(String fileName) {
        File pdfFile = new File(Constants.FILE_FOLDER + File.separator + fileName);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(pdfIntent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<String> searchFiles = new ArrayList<>();
        if (query.isEmpty()) {
            searchFiles = FileUtils.getFiles();
        } else {
            for (String file : FileUtils.getFiles()) {
                if (file.startsWith(query))
                    searchFiles.add(file);
            }
        }

        adapter = new FileListAdapter(this, searchFiles);
        filesListView.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        onQueryTextSubmit(newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, ImageDetectionActivity.class);
                intent.putExtra(Constants.Parameters.IMAGE_NAME,Constants.IMAGE_FOLDER+File.separator+"test.jpg");
                startActivity(intent);
                break;
        }
        return true;
    }

}
