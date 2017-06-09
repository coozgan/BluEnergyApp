package bluenergyfuel.bluenergy.drawer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.NewList;
import bluenergyfuel.bluenergy.viewholder.NewsViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class News extends BaseFragment {

    OnNewsListener mCallback;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<NewList, NewsViewHolder> firebaseRecyclerAdapterStore;
    private DatabaseReference mRef;
    private Query query;

    public News() {
        // Required empty public constructor
    }

    public interface OnNewsListener{
        public void onNewsSelected(String key);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_news, container, false);
        mRef = MyDatabaseUtil.getDatabase().getReference().child("news");
        query = mRef.orderByChild("postID");

        recyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapterStore = new FirebaseRecyclerAdapter<NewList, NewsViewHolder>(
                NewList.class,
                R.layout.drawer_news_list,
                NewsViewHolder.class,
                query
        ){
            @Override
            protected void populateViewHolder(NewsViewHolder viewHolder, NewList model, int position) {

                final String postKey = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getActivity(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onNewsSelected(postKey);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapterStore);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnNewsListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must be Implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        firebaseRecyclerAdapterStore.cleanup();
    }
}
