package bluenergyfuel.bluenergy.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bluenergyfuel.bluenergy.R;

/**
 * Created by jockinjc0 on 5/30/17.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public NewsViewHolder(View itemView) {
        super(itemView);
        mView =itemView;
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.news_title);
        post_title.setText(title);
    }

    public void setImage(Context context , String image){
        ProgressBar progressBar = null;
        if (mView != null) {
            progressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        ImageView post_image = (ImageView) mView.findViewById(R.id.news_image);
        Picasso.with(context)
                .load(image)
                .error(android.R.drawable.stat_notify_error)
                .fit()
                .into(post_image,  new ImageLoadedCallback(progressBar) {
                    @Override
                    public void onSuccess() {
                        if (this.progressBar != null) {
                            this.progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public  ImageLoadedCallback(ProgressBar progBar){
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }
}