package com.example.alonshprung.outbrainv1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Outbrain;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecAdapter extends ArrayAdapter<OBRecommendation> {

    private final ArrayList<OBRecommendation> items;
    private final Context context;

    public RecAdapter(Context context, ArrayList<OBRecommendation> items) {
        super(context, R.layout.outbrain_list_item);
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public OBRecommendation getItem(int position) {
        if (items == null || items.size() == 0) {
            return null;
        }
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View recParentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outbrain_list_item, null);

        final OBRecommendation rec = getItem(position);

        String title = rec.getContent();
        ((TextView) recParentView.findViewById(R.id.outbrain_layouts_title_text_label)).setText(title);


        String source = rec.getSourceName();
        ((TextView) recParentView.findViewById(R.id.outbrain_layouts_author_text_label)).setText(source);


        if (rec.getThumbnail() != null) {
            String imageURL = rec.getThumbnail().getUrl();
            ImageView recImageView = recParentView.findViewById(R.id.outbrain_rec_image_view);
            Picasso.with(context).load(imageURL).into(recImageView);
        }

        ImageView disclosureImageView = recParentView.findViewById(R.id.outbrain_rec_disclosure_image_view);

        if (rec.isPaid() && rec.isRTB()) {
            disclosureImageView.setVisibility(View.VISIBLE);
            Picasso.with(context).load(rec.getDisclosure().getIconUrl()).into(disclosureImageView);
            disclosureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomTabsUtils.createCustomTabIntent(parent.getContext(),Uri.parse(rec.getDisclosure().getClickUrl()));
                }
            });
        } else {
            disclosureImageView.setVisibility(View.GONE);
        }

        return recParentView;
    }
}
