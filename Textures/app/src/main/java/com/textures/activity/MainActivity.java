package com.textures.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.textures.R;
import com.textures.component.FileListAdapter;
import com.textures.utils.FileUtils;

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
        if(!files.isEmpty()){
            ImageView imgView = (ImageView)findViewById(R.id.folder);
            imgView .setVisibility(View.GONE);
        }
        filesListView = (ListView) findViewById(R.id.fileList);
        adapter = new FileListAdapter(this, files);
        filesListView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {    	
    	List<String> searchFiles = new ArrayList<>();
        if(query.isEmpty()){
        	searchFiles = FileUtils.getFiles();
        }
        else{
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

}
