package bluenergyfuel.bluenergy.login.registration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.activities.BluEnergy;
import bluenergyfuel.bluenergy.extendables.BaseFragment;

public class SignIn extends BaseFragment {
    private static final String TAG = "SIGN IN";
    private EditText lastDigit, phoneNumber;
    private Button signInBtn;
    private static final String preCardNumber = "01000 1135 00000 ";
    private static final String prePhone = "+63";

    public SignIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bluenergy_sign_in, container, false);

        signInBtn = (Button) view.findViewById(R.id.signin_btn);
        lastDigit = (EditText) view.findViewById(R.id.lastDigit);
        phoneNumber = (EditText) view.findViewById(R.id.phone_number);
        TextView termsTextView  = (TextView) view.findViewById(R.id.terms_textview);

        //Setting Button Disabled it also supports API VERSION < 21 ...
        buttonDisabled(signInBtn);

        //
        phoneNumber.setSelection(phoneNumber.getText().length());

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "YOU SUCK GHAADD", Toast.LENGTH_SHORT).show();
                TermsAndCondition dialog = new TermsAndCondition();
                dialog.show(getFragmentManager(), null);
            }
        });

        lastDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastDigit.setSelection(lastDigit.getText().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputChar = s.toString();
                if (inputChar.length() >= 17 && inputChar.substring(0,17).matches(preCardNumber)) {
                    Log.d(TAG, inputChar);
                }else {
                    lastDigit.setText(preCardNumber);
                    lastDigit.setSelection(lastDigit.getText().length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (phoneNumber.getText().length() == 13 && lastDigit.getText().length() == 23){
                    buttonEnabled(signInBtn);
                }
                else {
                    buttonDisabled(signInBtn);
                }
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputChar = s.toString();
                if (inputChar.length() >= 3 && inputChar.substring(1,3).matches("63")) {
                    if (inputChar.length() == 13){
                        phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_verified, 0);
                    }
                    else{
                        phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_not_verified, 0);
                    }
                }else {
                    phoneNumber.setText(prePhone);
                    phoneNumber.setSelection(phoneNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneNumber.getText().length() == 13 && lastDigit.getText().length() == 23){
                    buttonEnabled(signInBtn);
                }else {
                    buttonDisabled(signInBtn);
                }

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = lastDigit.getText().toString().replace(preCardNumber, "");
                BluEnergy b = (BluEnergy) getActivity();
                b.verifyPhone(phoneNumber.getText().toString(),temp);

            }
        });

        return view;
    }

   public static class TermsAndCondition extends DialogFragment{
       @NonNull
       @SuppressLint("InflateParams")
       @Override
       public Dialog onCreateDialog(Bundle savedInstanceState) {
           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
           // Get the layout inflater
           LayoutInflater inflater = getActivity().getLayoutInflater();
           // Inflate and set the layout for the dialog
           // Pass null as the parent view because its going in the dialog layout
           builder.setView(inflater.inflate(R.layout.terms_condition_dialog, null))
                   // Add action buttons
                   .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.dismiss();
                       }
                   });
           return builder.create();
       }
   }

}
