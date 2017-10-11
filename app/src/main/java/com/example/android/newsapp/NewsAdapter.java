package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Noura on 9/15/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> newsInfo) {
        super(context, 0, newsInfo);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        TextView nTitle = (TextView) rootView.findViewById(R.id.title);
        TextView nSection = (TextView) rootView.findViewById(R.id.section);
        TextView nUrl = (TextView) rootView.findViewById(R.id.url);
        TextView nDate = (TextView) rootView.findViewById(R.id.date);
        TextView nAuthor = (TextView) rootView.findViewById(R.id.author);


        News currentNews = getItem(position);


        nTitle.setText(currentNews.getnTitle());
        nSection.setText(currentNews.getnSection());
        nUrl.setText(currentNews.getnUrl());
        nAuthor.setText(currentNews.getnAuthor());

        String strDate = currentNews.getnDate();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.US);

        if (!(nDate.equals("Unknown"))) {

            try {
                Date date = dateFormat1.parse(strDate);

                strDate = dateFormat2.format(date);

            } catch (ParseException e) {
            }

        }

        nDate.setText(strDate);

        return rootView;
    }
}
