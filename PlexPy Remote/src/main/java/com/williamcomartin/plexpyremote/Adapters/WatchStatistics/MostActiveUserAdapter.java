package com.williamcomartin.plexpyremote.Adapters.WatchStatistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.williamcomartin.plexpyremote.ApplicationController;
import com.williamcomartin.plexpyremote.Helpers.VolleyHelpers.ImageCacheManager;
import com.williamcomartin.plexpyremote.Models.StatisticsModels;
import com.williamcomartin.plexpyremote.R;

import java.util.List;

/**
 * Created by wcomartin on 16-05-20.
 */
public class MostActiveUserAdapter extends RecyclerView.Adapter<MostActiveUserAdapter.ViewHolder> {

    private final SharedPreferences SP;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView vImage;
        private final TextView vBadge;
        private final TextView vTitle;
        private final TextView vQuantity;
        private final TextView vQuantifier;

        public ViewHolder(View itemView) {
            super(itemView);

            vImage = (NetworkImageView) itemView.findViewById(R.id.standard_statistics_card_image);
            vBadge = (TextView) itemView.findViewById(R.id.standard_statistics_card_badge);
            vTitle = (TextView) itemView.findViewById(R.id.standard_statistics_card_title);
            vQuantity = (TextView) itemView.findViewById(R.id.standard_statistics_card_quantity);
            vQuantifier = (TextView) itemView.findViewById(R.id.standard_statistics_card_quantifier);
        }
    }

    private List<StatisticsModels.StatisticsRow> statisticsItems;

    // Pass in the contact array into the constructor
    public MostActiveUserAdapter(List<StatisticsModels.StatisticsRow> statisticsItems) {
        this.statisticsItems = statisticsItems;
        SP = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getInstance().getApplicationContext());
    }

    @Override
    public MostActiveUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_standard_statistics, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MostActiveUserAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        StatisticsModels.StatisticsRow item = statisticsItems.get(position);

        viewHolder.vImage.setImageUrl(item.user_thumb, ImageCacheManager.getInstance().getImageLoader());

        viewHolder.vBadge.setText(((Integer) (position + 1)).toString());
        viewHolder.vTitle.setText(item.friendly_name);
        viewHolder.vQuantity.setText(item.total_plays.toString());

        viewHolder.vQuantifier.setText("plays");
    }

    @Override
    public int getItemCount() {
        return statisticsItems.size();
    }
}
