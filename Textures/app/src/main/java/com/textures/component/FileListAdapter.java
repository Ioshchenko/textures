package com.textures.component;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.textures.R;

import java.util.List;

public class FileListAdapter extends ArrayAdapter<Object> {

    private Activity context;
    private List<String> files;

    public FileListAdapter(Activity context, List<String> files) {
        super(context, R.layout.row_file_list , files.toArray());
        this.context = context;
       this.files = files;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_file_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


        String fileName = files.get(position);
        txtTitle.setText(fileName);

        if(fileName.endsWith(".pdf")){
            imageView.setImageResource(R.drawable.pdf);
        }
        else if(fileName.endsWith("doc") || fileName.endsWith("docx") ){
            imageView.setImageResource(R.drawable.word);
        }

        return rowView;

    }

    public List<String> getFiles() {
        return files;
    }
}