package bluenergyfuel.bluenergy.login.registration;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergy;
import bluenergyfuel.bluenergy.extendables.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardInfo extends BaseFragment {
    private Button nextButton;
    private RadioGroup radioGroup;
    private RadioButton radioButton;


    public CardInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.bluenergy_signin_card_info, container, false);
        //next Button
        nextButton = (Button) view.findViewById(R.id.card_info_button);
        buttonDisabled(nextButton);
        //radio group
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                buttonEnabled(nextButton);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)view.findViewById(selectedID);
                if (radioButton!= null){
                    BluEnergy b = (BluEnergy) getActivity();
                    b.loadPersonalInfo(radioButton.getText().toString());
                }else {
                    Toast.makeText(getActivity(), "Clicked Your Type of Card", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
