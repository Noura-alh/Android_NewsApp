package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {


    private static final String NEWS_REQUEST_URL =
            "http://content.guardianapis.com/search?&=";
    private static final int LOADER_ID = 1;
    private static final String API = "api-key";

    private static final String KEY = "59d0bf63-14c5-4f77-b80d-cbd12411469f";
    private NewsAdapter nAdapter;
    private TextView no_news_text;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.list_view);
        no_news_text = (TextView) findViewById(R.id.empty_text_view);
        listView.setEmptyView(no_news_text);


        nAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(nAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                News currentNews = nAdapter.getItem(i);

                Uri newsUri = Uri.parse(currentNews.getnUrl());

                Intent internetIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(internetIntent);
            }
        });


        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            Toast toast = Toast.makeText(getApplicationContext(), R.string.noConnection, Toast.LENGTH_LONG);
            toast.show();

        }

    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String searchBy = sharedPreferences.getString(getString(R.string.search_by),
                getString(R.string.search_for_politics));

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchBy);
        uriBuilder.appendQueryParameter(API, KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {


        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        no_news_text.setText(R.string.noNewsFound);

        nAdapter.clear();

        if (!newses.isEmpty() && newses != null) {
            nAdapter.addAll(newses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        nAdapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}












