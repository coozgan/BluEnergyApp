package bluenergyfuel.bluenergy.drawer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.PartnersList;
import bluenergyfuel.bluenergy.viewholder.PartnerStoreViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerStore extends BaseFragment {
    OnPartnerListener mCallback;

    public interface OnPartnerListener{
        void onPartnerSelected(String key);
    }
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<PartnersList, PartnerStoreViewHolder> firebaseRecyclerAdapter;
    private DatabaseReference mRef;

    public PartnerStore() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_partner_store, container, false);

        mRef = MyDatabaseUtil.getDatabase().getReference().child("partners");

        recyclerView = (RecyclerView) view.findViewById(R.id.partner_store_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PartnersList, PartnerStoreViewHolder>(
                PartnersList.class,
                R.layout.drawer_partner_store_list,
                PartnerStoreViewHolder.class,
                mRef
        ){
            @Override
            protected void populateViewHolder(PartnerStoreViewHolder viewHolder, PartnersList model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setStore(model.getPartnerStore());
                viewHolder.setSubData(model.getSubData());
                viewHolder.setImage(getActivity() ,model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onPartnerSelected(postKey);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PartnerStore.OnPartnerListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must be Implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        firebaseRecyclerAdapter.cleanup();
    }
}
