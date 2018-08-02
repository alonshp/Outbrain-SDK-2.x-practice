package com.example.alonshprung.outbrainv1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;
import com.outbrain.OBSDK.FetchRecommendations.OBRequest;
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.OutbrainException;
import com.outbrain.OBSDK.Viewability.OBTextView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    private ListView listView;
    private RecAdapter recAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.outbrain_main_list_view);

        try {
            Outbrain.register(getApplicationContext(), "AndroidSampleApp2014");
        }
        catch (OutbrainException ex)
        {
            // handle exception
            Log.e(TAG, "Error while register Outbrain");
        }

        Outbrain.setTestMode(true);

        OBRequest request = new OBRequest("http://mobile-demo.outbrain.com/2014/01/26/how-to-use-social-media-like-the-best-smb-marketers", "SDK_1");

        Outbrain.fetchRecommendations(request, new RecommendationsListener() {
            @Override
            public void onOutbrainRecommendationsSuccess(OBRecommendationsResponse recommendations) {
                recAdapter = new RecAdapter(getApplicationContext(), recommendations.getAll());
                listView.setAdapter(recAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        OBRecommendation rec = recAdapter.getItem(position);

                        CustomTabsUtils.createCustomTabIntent(MainActivity.this, Uri.parse(Outbrain.getUrl(rec)));
                    }
                });
            }

            @Override
            public void onOutbrainRecommendationsFailure(Exception ex) {
                //Handle failure (write to log, hide the UI component, etc...)
                Log.e(TAG, ex.getMessage());
            }
        });

        OBTextView obTextView = (OBTextView) findViewById(R.id.widget_title_view);
        Outbrain.registerOBTextView(obTextView, request.getWidgetId(), request.getUrl());

        FrameLayout OBLogo = (FrameLayout) findViewById(R.id.recommended_by_wrapper);
        OBLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsUtils.createCustomTabIntent(MainActivity.this, Uri.parse(Outbrain.getOutbrainAboutURL(MainActivity.this)));
            }
        });
    }
}
