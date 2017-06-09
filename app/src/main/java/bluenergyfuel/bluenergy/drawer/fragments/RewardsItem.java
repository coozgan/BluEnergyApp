package bluenergyfuel.bluenergy.drawer.fragments;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.ItemsList;
import bluenergyfuel.bluenergy.viewholder.ItemsViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsItem extends BaseFragment {
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<ItemsList, ItemsViewHolder> firebaseRecyclerAdapterStore;
    private DatabaseReference mRef;
    private Query query;


    public RewardsItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_rewards_item, container, false);
        mRef = MyDatabaseUtil.getDatabase().getReference().child("posts").child("rewards");
        query = mRef.orderByChild("title");
        recyclerView = (RecyclerView) view.findViewById(R.id.rewards_item_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapterStore = new FirebaseRecyclerAdapter<ItemsList, ItemsViewHolder>(
                ItemsList.class,
                R.layout.drawer_rewards_item_list,
                ItemsViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ItemsViewHolder viewHolder, ItemsList model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getPoints());
                viewHolder.setImage(getActivity() ,model.getImage());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapterStore);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        firebaseRecyclerAdapterStore.cleanup();
    }
    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context,R.drawable.line_divider);
        }
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
