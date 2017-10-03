package com.example.apple.popularmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by apple on 28/09/17.
 */

public class DetailActivity extends AppCompatActivity {

    private ActionBar mActionBar;

    private TextView tvOverview;
    private TextView tvMovieDate;
    private TextView tvMovieTitle;
    private TextView tvMovieRating;

    private ImageView ivMoviePoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(getResources().getString(R.string.title_Detail));
        }

        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvMovieDate = (TextView) findViewById(R.id.tvMovieDate);
        tvMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        tvMovieRating = (TextView) findViewById(R.id.tvMovieRating);

        ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);

        Intent intent = getIntent();

        BeanMovie beanMovie = (BeanMovie) intent.getSerializableExtra(Intent.EXTRA_INTENT);

        if (beanMovie != null) {
            tvOverview.setText(String.valueOf(beanMovie.getOverview()));
            tvMovieDate.setText(String.valueOf(beanMovie.getReleaseDate()));
            tvMovieTitle.setText(String.valueOf(beanMovie.getOriginalTitle()));
            tvMovieRating.setText(String.valueOf(beanMovie.getVoteAverage()) + "/" + getResources().getString(R.string.text_10));

            String posterPath = beanMovie.getPosterPath();
            String imageSize = getResources().getString(R.string.size_w185);

            String imagePath = NetworkUtil.buildFetchMoviePosterURL(this, posterPath, imageSize).toString();
            Picasso.with(this).load(imagePath).into(ivMoviePoster);

        } else {
            Toast.makeText(this, getResources().getString(R.string.text_no_date), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
