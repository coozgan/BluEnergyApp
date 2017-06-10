package bluenergyfuel.bluenergy.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;

import bluenergyfuel.bluenergy.R;

/**
 * Created by jockinjc0 on 5/2/17.
 */

public class ItemsViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ItemsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.rewards_post_title);
        StringTokenizer tokenizer = new StringTokenizer(title,".");
        String dump = tokenizer.nextToken();
        title = tokenizer.nextToken();
        post_title.setText(title);
    }

    public  void setDesc(String points){
        TextView post_points = (TextView) mView.findViewById(R.id.rewards_post_points);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            post_points.setTextColor(Color.parseColor("#00ACED"));
        }
        post_points.setText(points + " Pts.");
    }
    public void setImage(Context context, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.rewards_post_image);
        Picasso.with(context).load(image).error(android.R.drawable.stat_notify_error).placeholder(R.drawable.ic_blu_logo).resize(1000,1000).centerCrop().into(post_image);

    }

}
