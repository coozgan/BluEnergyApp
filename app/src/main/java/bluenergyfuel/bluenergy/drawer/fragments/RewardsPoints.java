package bluenergyfuel.bluenergy.drawer.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.CardUser;

public class RewardsPoints extends BaseFragment {
    private static final String TAG = "REWARDS POINTS";
    private TextView statusTextView, rewTotalPoints;
    private ImageView statusButton;
    private double totalPoints, currentVol;
    private boolean insuranceQualified;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    public RewardsPoints() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_rewards_points, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = MyDatabaseUtil.getDatabase().getReference().child("card_users");
        rewTotalPoints = (TextView) view.findViewById(R.id.rewards_total_points);
        statusTextView = (TextView) view.findViewById(R.id.points_inusance_status_textview);
        statusButton = (ImageView) view.findViewById(R.id.points_inusance_status_logo);
        ImageButton insuranceButton = (ImageButton) view.findViewById(R.id.insurance_btn);

        insuranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast("Show some dialog here");
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            String uid = mAuth.getCurrentUser().getUid();
            mRef = mRef.child(uid);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CardUser cardUser = dataSnapshot.getValue(CardUser.class);
                    if (cardUser!=null){
                        dataProcessing(cardUser.getTransactions());
                    }else {
                        Log.d(TAG, "NULL");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void dataProcessing(String json){
        try {
            JSONObject response = new JSONObject(json);
            totalPoints = response.getDouble("TotalPoints");
            insuranceQualified = response.getDouble("PreviousMonthVolume") >= 200;
            currentVol = response.getDouble("CurrentMonthVolume");
            setViews();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setViews(){
        rewTotalPoints.setText(String.format(Locale.US,"%.2f", totalPoints));
        if (!insuranceQualified){
            statusTextView.setTextColor(Color.parseColor("#e1464b"));
            statusButton.setImageResource(R.drawable.ic_partial_checked);
            if (currentVol <= 100.00 && currentVol >= 1){
                statusTextView.setText(String.format(Locale.US, "You have fueled %.2f litres this month", currentVol));
            }else if (currentVol <= 0){
                statusTextView.setTextColor(Color.parseColor("#00ACED"));
                statusTextView.setText("Start fueling this month to avail our \nInsurance");
            } else {
                statusTextView.setText(String.format(Locale.US,"You need %.2f to qualify for \nInsurance", 200-currentVol));
            }
        }
    }
}
