package com.example.apple.popularmovieapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by apple on 29/09/17.
 */

public class NetworkUtil {

    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner sn = new Scanner(inputStream);
            sn.useDelimiter("\\A");

            boolean hasInput = sn.hasNext();
            if (hasInput) {
                return sn.next();
            } else {
                return null;
            }

        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static URL buildFetchMoviewURL(Context context, String path) {

        Resources r = context.getResources();

        Uri uri = Uri
                .parse(r.getString(R.string.base_get_movie_url))
                .buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(
                        r.getString(R.string.text_api_key),
                        r.getString(R.string.api_key_the_movie_db))
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildFetchMoviePosterURL(Context context, String path, String size) {
        URL url = null;

        Resources r = context.getResources();

        Uri uri = Uri
                .parse(r.getString(R.string.base_fetch_movie_url))
                .buildUpon()
                .appendEncodedPath(size)
                .appendEncodedPath(path)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
