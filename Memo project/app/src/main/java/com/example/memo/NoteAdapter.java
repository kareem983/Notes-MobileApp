package com.example.memo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Activity context, ArrayList<Note> androidFlavors) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Note currentNote = getItem(position);



        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView title_textView = (TextView) listItemView.findViewById(R.id.Title_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        title_textView.setText(currentNote.getTitle());


        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView content_textView = (TextView) listItemView.findViewById(R.id.content_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        content_textView.setText(currentNote.getContent());


        TextView date_TextView = (TextView) listItemView.findViewById(R.id.date_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        date_TextView.setText(currentNote.getDate());


        ImageView imageView = (ImageView) listItemView.findViewById(R.id.select_Image);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        if(currentNote.hasImage()) {
            imageView.setImageResource(currentNote.getSelect_image_Res_Id());
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.GONE);
        }


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
