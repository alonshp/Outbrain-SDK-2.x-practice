package com.example.alonshprung.outbrainv1;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

public class CustomTabsUtils {

    public static void createCustomTabIntent(Context context, Uri uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, uri);
    }
}
