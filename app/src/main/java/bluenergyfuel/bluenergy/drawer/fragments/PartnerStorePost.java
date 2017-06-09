package bluenergyfuel.bluenergy.drawer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.PartnersList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerStorePost extends BaseFragment {
    public final static String ARGS_KEY = "myKey";
    private DatabaseReference mRef;
    private ImageView imageView;
    private TextView storeTextview, storeDesc;

    public PartnerStorePost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_partner_store_post, container, false);

        imageView = (ImageView) view.findViewById(R.id.partner_store_post_image);
        storeDesc = (TextView) view.findViewById(R.id.partner_store_post_description);
        storeTextview = (TextView) view.findViewById(R.id.partner_store_post_store);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null){
            updateDisplay(args.getString(ARGS_KEY));
        }

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PartnersList p = dataSnapshot.getValue(PartnersList.class);
                assert p != null;
                Picasso.with(getActivity())
                        .load(p.getImage())
                        .error(android.R.drawable.stat_notify_error)
                        .placeholder(R.drawable.ic_blu_logo)
                        .into(imageView);
                storeTextview.setText(p.getPartnerStore());
                storeDesc.setText(p.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updateDisplay(String postKey){
        if (!postKey.matches("")){
            mRef = MyDatabaseUtil.getDatabase().getReference().child("partners").child(postKey);
        }else {
            makeToast("Error in Fetching Data");
        }
    }
}
