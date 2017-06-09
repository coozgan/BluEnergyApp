package bluenergyfuel.bluenergy.login.registration;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergy;
import bluenergyfuel.bluenergy.extendables.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfo extends BaseFragment implements AdapterView.OnItemSelectedListener {

    OnPersonalInfo mCallback;
    public interface OnPersonalInfo{
        void onPersonalInfoComplete(String userFirstName,
                                    String userLastName,
                                    String userBirthday);
    }
    private Button nextButton;
    private Spinner spinner;
    private EditText firstText, lastText, dayText, yearText;
    private String tempMonth;

    public PersonalInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bluenergy_signin_personal_info, container, false);
        //
        firstText = (EditText) view.findViewById(R.id.editFirst);
        lastText = (EditText) view.findViewById(R.id.editLast);
        dayText = (EditText) view.findViewById(R.id.date_day);
        yearText = (EditText) view.findViewById(R.id.date_year);
        //My button
        nextButton = (Button) view.findViewById(R.id.personal_info_button);
        buttonDisabled(nextButton);
        //Dropdown Spinner
        spinner = (Spinner) view.findViewById(R.id.date_spinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        //All Capital Filter
        firstText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        lastText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        firstText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfallAreFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lastText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfallAreFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfallAreFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        yearText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int calendarYear;
                Calendar c = Calendar.getInstance();
                calendarYear = c.get(Calendar.YEAR);
                if (s.toString().length() == 4 && Integer.parseInt(s.toString()) > calendarYear - 17){
                    Toast.makeText(getActivity(), "Please Input A Valid Birthdate", Toast.LENGTH_SHORT).show();
                }else {
                    checkIfallAreFilled();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dateValidity(tempMonth + " "+dayText.getText()+", "+yearText.getText())){
                    makeToast("Please Fill In The Info Correctly");
                }
                else {
                    String firstname = firstText.getText().toString().trim();
                    String lastname = lastText.getText().toString().trim();
                    String birthday = tempMonth+ " "+dayText.getText().toString() + ", "+yearText.getText().toString();
                    mCallback.onPersonalInfoComplete(firstname, lastname, birthday);
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnPersonalInfo) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must be Implemented");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        tempMonth = textView.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void checkIfallAreFilled(){
        boolean boolLastName = lastText.getText().length() >=1;
        boolean boolFirstName = firstText.getText().length() >= 1;
        boolean boolDayText = dayText.getText().length() >= 1;
        boolean boolYearText = yearText.getText().length() >= 1;

        if (boolLastName && boolFirstName && boolDayText && boolYearText){
            buttonEnabled(nextButton);
        }else {
            buttonDisabled(nextButton);
        }
    }
    public boolean dateValidity(String myDate){
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
            dateFormat.setLenient(false);
            dateFormat.parse(myDate);
            return  true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
