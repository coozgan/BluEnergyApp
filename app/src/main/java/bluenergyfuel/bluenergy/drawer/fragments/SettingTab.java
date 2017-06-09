package bluenergyfuel.bluenergy.drawer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergyDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingTab extends Fragment {


    public SettingTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_settings, container, false);
        Button button = (Button) view.findViewById(R.id.signout_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluEnergyDrawer bluEnergyDrawer = (BluEnergyDrawer) getActivity();
                bluEnergyDrawer.signOut();
            }
        });
        return view;
    }

}
