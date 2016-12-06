package xyz.christianwijasa.tutorial;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Richard Firdaus on 06/12/2016.
 */

class CustomListAdapter extends ArrayAdapter<Chapters> {


    ArrayList<Chapters> chapters;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<Chapters> chapters) {
        super(context, resource, chapters);
        this.chapters = chapters;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chapter_list, null, true);

        }
        Chapters chapter = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        Picasso.with(context).load(chapter.getImage()).into(imageView);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
        txtTitle.setText(chapter.getChapter_title());

        TextView txtChapterDescription = (TextView) convertView.findViewById(R.id.txt_description);
        txtChapterDescription.setText(chapter.getChapter_description());

        TextView txtName = (TextView) convertView.findViewById(R.id.chapter_id);
        txtName.setText(chapter.getId());

        return convertView;
    }
}