package bluenergyfuel.bluenergy.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bluenergyfuel.bluenergy.R;

/**
 * Created by jockinjc0 on 5/28/17.
 */

public class PartnerStoreViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    public PartnerStoreViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setStore(String store ){
        TextView storeName = (TextView) mView.findViewById(R.id.partner_store);
        storeName.setText(store);
    }
    public void setSubData(String subData){
        TextView descName = (TextView) mView.findViewById(R.id.partner_post_desc);
        if (subData.length() <= 30){
            for (int x=0; x <= 99; x++){
                subData = subData + " "; //adding white spaces
            }
            descName.setText(subData);
        }else {
            descName.setText(subData);
        }
    }
    public void setImage(Context context, String image){
        ImageView postImage = (ImageView) mView.findViewById(R.id.partner_post_image);
        Picasso.with(context)
                .load(image)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.drawable.ic_blu_logo)
                .fit()
                .into(postImage);
    }
}
