package bluenergyfuel.bluenergy.login.registration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergy;
import bluenergyfuel.bluenergy.extendables.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Verification extends BaseFragment {
    public static final String ARGS_VER_ID = "verificationID";
    public static final String ARGS_PHONE = "userPhoneNumbah";
    private static final String LETTER_SPACING = " ";
    private String myPreviousText;
    private EditText otpCode;
    private TextView phoneNumberText;
    private Button button;


    public Verification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bluenergy_signin_verification, container, false);
        final String verCode = getArguments().getString(ARGS_VER_ID);
        final String digits = getArguments().getString(ARGS_PHONE);

        otpCode = (EditText) view.findViewById(R.id.verification_code);
        phoneNumberText = (TextView) view.findViewById(R.id.verification_phone);
        phoneNumberText.setText(digits);
        button = (Button) view.findViewById(R.id.verification_btn);
        buttonDisabled(button);

        otpCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                addSpaces(s.toString());
                if (otpCode.getText().length() > 10){
                    buttonEnabled(button);
                }else {
                    buttonDisabled(button);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpValue = otpCode.getText().toString();
                otpValue = otpValue.replaceAll(" ", "");
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verCode,otpValue);
                BluEnergy b = (BluEnergy) getActivity();
                b.signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        });


        return view;
    }

    private void addSpaces(String text) {
        // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
        if (!text.equals(myPreviousText)) {
            // Remove spaces
            text = text.replace(" ", "");

            // Add space between each character
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i == text.length() - 1) {
                    // Do not add a space after the last character -> Allow user to delete last character
                    newText.append(Character.toUpperCase(text.charAt(text.length() - 1)));
                }
                else {
                    newText.append(Character.toUpperCase(text.charAt(i)) + LETTER_SPACING);
                }
            }

            myPreviousText = newText.toString();
            // Update the text with spaces and place the cursor at the end
            otpCode.setText(newText);
            otpCode.setSelection(newText.length());
        }
    }

}
