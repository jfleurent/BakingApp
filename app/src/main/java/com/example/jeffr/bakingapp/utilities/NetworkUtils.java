package com.example.jeffr.bakingapp.utilities;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class NetworkUtils {
    private static final String RECIPE_SITE = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static URL buildRecipesUrl() {
        Timber.d("Start buildRecipesUrl");
        URL url = null;
        try {
            url = new URL(RECIPE_SITE);
        } catch (MalformedURLException e) {
            Timber.e(e,"MalformedURLException in buildRecipesUrl");
        }
        Timber.d("Built URL: "+url);
        Timber.d("End buildRecipesUrl");
        return url;
    }


    public static String getResponseFromHttpUrl() throws IOException {
        Timber.d("Start getResponseFromHttpUrl");
        HttpURLConnection urlConnection = (HttpURLConnection) buildRecipesUrl().openConnection();
        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
            Timber.d("End getResponseFromHttpUrl");
        }
    }
}
