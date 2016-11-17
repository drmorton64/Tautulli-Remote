package com.williamcomartin.plexpyremote.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.williamcomartin.plexpyremote.ApplicationController;
import com.williamcomartin.plexpyremote.Helpers.UrlHelpers;
import com.williamcomartin.plexpyremote.Models.ActivityModels.Activity;
import com.williamcomartin.plexpyremote.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by wcomartin on 2015-11-25.
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    View itemView;
    private SharedPreferences SP;

    public ActivityAdapter() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        protected TextView vTitle;
        protected TextView vSubTitle;
        protected TextView vEpisode;

        protected TextView vStateIcon;
        protected TextView vUserText;
        protected TextView vDecisionText;
        protected TextView vProgressText;

        protected ProgressBar vprogress;

        protected NetworkImageView vImage;
        protected NetworkImageView vImageBlurred;

        public ViewHolder(View itemView) {
            super(itemView);

            vTitle = (TextView) itemView.findViewById(R.id.activity_card_title);
            vSubTitle = (TextView) itemView.findViewById(R.id.activity_card_subtitle);
            vEpisode = (TextView) itemView.findViewById(R.id.activity_card_muted_title);

            vStateIcon = (TextView) itemView.findViewById(R.id.activity_card_state_icon);
            vUserText = (TextView) itemView.findViewById(R.id.activity_card_user_text);
            vDecisionText = (TextView) itemView.findViewById(R.id.activity_card_decision_text);
            vProgressText = (TextView) itemView.findViewById(R.id.activity_card_progress_text);

            vprogress = (ProgressBar) itemView.findViewById(R.id.progressbar);

            vImage = (NetworkImageView) itemView.findViewById(R.id.activity_card_image);
            vImageBlurred = (NetworkImageView) itemView.findViewById(R.id.activity_card_image_blurred);

        }
    }

    private List<Activity> activities;

    // Pass in the contact array into the constructor
    public ActivityAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        itemView = inflater.inflate(R.layout.item_activity, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Context context = ApplicationController.getInstance().getApplicationContext();
        // Get the data model based on position
        Activity activity = activities.get(position);

        // Set item views based on the data model
        String imageUrl;

        if (activity.media_type.equals("episode")) {
            viewHolder.vTitle.setText(activity.grandparent_title);
            viewHolder.vSubTitle.setText(activity.title);
            viewHolder.vEpisode.setText("S" + activity.parent_media_index + " - E" + activity.media_index);
            imageUrl = UrlHelpers.getImageUrl(activity.grandparent_thumb, "600", "400");
            viewHolder.vImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (activity.media_type.equals("track")) {
            viewHolder.vTitle.setText(activity.grandparent_title);
            viewHolder.vSubTitle.setText(activity.title);
            viewHolder.vEpisode.setText(activity.parent_title);
            imageUrl = UrlHelpers.getImageUrl(activity.thumb, "600", "400");
        } else {
            viewHolder.vTitle.setText(activity.title);
            viewHolder.vSubTitle.setVisibility(View.GONE);
            viewHolder.vEpisode.setText(activity.year);
            imageUrl = UrlHelpers.getImageUrl(activity.thumb, "600", "400");
            viewHolder.vImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        if (activity.state.equals("playing")) {
            viewHolder.vStateIcon.setText("{fa-play}");
        } else if (activity.state.equals("paused")) {
            viewHolder.vStateIcon.setText("{fa-pause}");
        } else if (activity.state.equals("buffering")) {
            viewHolder.vStateIcon.setText("{fa-spinner}");
        }

        viewHolder.vUserText.setText(activity.friendly_name);

        if (activity.video_decision.equals("direct play")) {
            viewHolder.vDecisionText.setText("Direct Play");
        } else if (activity.video_decision.equals("copy")) {
            viewHolder.vDecisionText.setText("Direct Stream");
        } else {
            viewHolder.vDecisionText.setText("Transcoding");
        }

        viewHolder.vProgressText.setText(formatSeconds(activity.view_offset) + "/" + formatSeconds(activity.duration));

        viewHolder.vprogress.setProgress(Integer.parseInt(activity.progress_percent));
        viewHolder.vprogress.setSecondaryProgress(Integer.parseInt(activity.transcode_progress));


//        if (!activity.thumb.equals("")) {
//            imageUrl = UrlHelpers.getImageUrl(activity.thumb, "600", "400");
//        } else if (!activity.parent_thumb.equals("")) {
//            imageUrl = UrlHelpers.getImageUrl(activity.parent_thumb, "600", "400");
//        } else {
//            imageUrl = UrlHelpers.getImageUrl(activity.grandparent_thumb, "600", "400");
//        }
        viewHolder.vImage.setImageUrl(imageUrl, ApplicationController.getInstance().getImageLoader());
        viewHolder.vImageBlurred.setImageUrl(imageUrl, ApplicationController.getInstance().getImageLoader());
        viewHolder.vImageBlurred.setAlpha(0.75f);


    }

    private String formatSeconds(String millis){
        return String.format(Locale.US, "%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(millis)),
                TimeUnit.MILLISECONDS.toSeconds(Integer.parseInt(millis)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(millis)))
        );
    }

    @Override
    public int getItemCount() {
        if (activities == null) {
            return 0;
        }
        return activities.size();
    }
}
