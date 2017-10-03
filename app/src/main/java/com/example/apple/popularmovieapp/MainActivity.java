package com.example.apple.popularmovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener {

    private RecyclerView rvMovieList;
    private ProgressBar progressBar;


    private MovieListAdapter mAdapter;

    private ArrayList<BeanMovie> mMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovieList = (RecyclerView) findViewById(R.id.rvMovieList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAdapter = new MovieListAdapter(this, mMovieList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        rvMovieList.setLayoutManager(layoutManager);
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(mAdapter);


        URL url = NetworkUtil.buildFetchMoviewURL(this, getResources().getString(R.string.popular_movie));

        loadMovies(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        URL url = null;

        switch (item.getItemId()) {
            case R.id.menu_popular:
                url = NetworkUtil.buildFetchMoviewURL(this, getResources().getString(R.string.popular_movie));
                break;

            case R.id.menu_top_rated:
                url = NetworkUtil.buildFetchMoviewURL(this, getResources().getString(R.string.top_rated_movie));
                break;
        }

        if (url != null) {
            loadMovies(url);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(URL url) {

        if (NetworkUtil.isNetworkAvailable(this)) {
            new LoadMovieAsync().execute(url);
        } else {
            Toast.makeText(this, getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int position) {
        BeanMovie beanMovie = mMovieList.get(position);

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_INTENT, beanMovie);
        startActivity(intent);

    }


    private class LoadMovieAsync extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            rvMovieList.setVisibility(View.GONE);
        }


        @Override
        protected String doInBackground(URL... urls) {
            String response = null;

            try {
                URL url = urls[0];
                response = NetworkUtil.getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            rvMovieList.setVisibility(View.VISIBLE);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray(getResources().getString(R.string.key_results));
                String strResult = results.toString();

                ArrayList<BeanMovie> list = new Gson().fromJson(strResult, new TypeToken<List<BeanMovie>>() {
                }.getType());

                mMovieList.clear();
                mMovieList.addAll(list);

                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
