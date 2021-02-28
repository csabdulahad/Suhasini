package net.abdulahad.suhasini.helper;

import android.net.Uri;
import android.util.Log;

public class UrlHelper {

    private Uri uri;
    private Uri.Builder builder;

    public UrlHelper(String url) {
        uri = Uri.parse(url);
        builder = uri.buildUpon();
    }

    public void addQueryParam(String key, String value) {
        builder.appendQueryParameter(key, value);
    }

    public void addQueryParam(String key, int value) {
        builder.appendQueryParameter(key, String.valueOf(value));
    }

    public void addQueryParam(String key, double value) {
        builder.appendQueryParameter(key, String.valueOf(value));
    }

    public String getUrl() {
        return builder.build().toString();
    }

}
