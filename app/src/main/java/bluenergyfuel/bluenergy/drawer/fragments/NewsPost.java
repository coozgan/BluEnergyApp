package bluenergyfuel.bluenergy.drawer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergyDrawer;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.NewList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsPost extends BaseFragment {

    public final static String ARGS_KEY = "myKey";
    private DatabaseReference mRef;
    private ImageView imageView;
    private TextView titleTextView, contentTextView;
    private View view;

    public NewsPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drawer_news_post, container, false);
        imageView = (ImageView) view.findViewById(R.id.news_post_image);
        titleTextView = (TextView) view.findViewById(R.id.news_post_title);
        contentTextView = (TextView) view.findViewById(R.id.news_post_content);
        contentTextView.setMovementMethod(new ScrollingMovementMethod());
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
                NewList n = dataSnapshot.getValue(NewList.class);

                ProgressBar progressBar = null;
                if (view != null) {
                    progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
                    progressBar.setVisibility(View.VISIBLE);
                }
                Picasso.with(getActivity())
                        .load(n.getImage())
                        .error(android.R.drawable.stat_notify_error)
                        .into(imageView, new ImageLoadedCallback(progressBar){
                            @Override
                            public void onSuccess() {
                                Log.d("Result","Success");
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                titleTextView.setText(n.getTitle());
                contentTextView.setText(n.getContents());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                makeToast("Error connecting to Database");
            }
        });
    }

    public void updateDisplay(String postKey){
        if (!postKey.matches("")){
            mRef = MyDatabaseUtil.getDatabase().getReference().child("news").child(postKey);
        }else {
            makeToast("Error in Fetching Data");
        }
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
