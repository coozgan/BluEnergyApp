package bluenergyfuel.bluenergy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.login.registration.CardInfo;
import bluenergyfuel.bluenergy.login.registration.PersonalInfo;
import bluenergyfuel.bluenergy.login.registration.SignIn;
import bluenergyfuel.bluenergy.extendables.BaseActivity;
import bluenergyfuel.bluenergy.login.registration.Verification;
import bluenergyfuel.bluenergy.model.CardUser;

public class BluEnergy extends BaseActivity implements PersonalInfo.OnPersonalInfo{

    private static final String TAG = "Signin Activity";
    private static String cellNumber;
    private static String verificationCode;
    private static String cardNumber;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_blu_energy);
        //
        mAuth = FirebaseAuth.getInstance();
        mRef = MyDatabaseUtil.getDatabase().getReference().child("card_users");
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG,"onVerificationCompleted:" + phoneAuthCredential );
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "INVALID REQUEST");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(BluEnergy.this, "Text Cannot be send, Try again later", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Too many Request");
                }
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                hideProgressDialog();
                Log.d(TAG, "onCodeSent: "+s);
                verificationCode = s;
                loadVerification();
            }
        };
        loadSignIn();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void loadBluEnergyDrawer(){
        Intent signinIntent = new Intent(BluEnergy.this, BluEnergyDrawer.class);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signinIntent);
    }
    public void loadVerification(){
        Verification verification = new Verification();
        Bundle args = new Bundle();
        args.putString(Verification.ARGS_VER_ID, verificationCode);
        args.putString(Verification.ARGS_PHONE, cellNumber);
        verification.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bluenergy_signin_fragment, verification).commit();
    }
    public void loadCardInfo(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.bluenergy_signin_fragment, new CardInfo()).commit();
    }
    public void loadPersonalInfo(String cardType){
        mRef.child("cardtype").setValue(cardType);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.bluenergy_signin_fragment, new PersonalInfo()).commit();
    }
    public void loadSignIn(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.bluenergy_signin_fragment, new SignIn()).commit();
    }
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
    public void verifyPhone(String phoneNumber, String card){
        if (isGooglePlayServicesAvailable(BluEnergy.this)){
            showProgressDialog();
            cardNumber =card;
            cellNumber = phoneNumber;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallback
        }else {
            Toast.makeText(BluEnergy.this, "Please Update Google Play Services", Toast.LENGTH_LONG).show();
        }

    }
    public void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgressDialog();
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    String fireID = user.getUid();
                    Log.d("JEjEJE", fireID);
                    mRef = mRef.child(fireID);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CardUser cardUser = dataSnapshot.getValue(CardUser.class);
                            if (cardUser != null){
                                String tempCard = cardUser.getCardnumber();
                                if (!cardNumber.matches(tempCard)){
                                    loadCardInfo();
                                    mRef.child("phone").setValue(cellNumber);
                                    mRef.child("cardnumber").setValue(cardNumber);

                                }else {
                                    loadBluEnergyDrawer();
                                }
                            }else {
                                mRef.child("phone").setValue(cellNumber);
                                mRef.child("cardnumber").setValue(cardNumber);
                                loadCardInfo();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    makeToast("CODE ERROR, Please check the number code");
                }
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onPersonalInfoComplete(String userFirstName, String userLastName, String userBirthday) {
        mRef.child("firstname").setValue(userFirstName);
        mRef.child("lastname").setValue(userLastName);
        mRef.child("birthday").setValue(userBirthday);
        loadBluEnergyDrawer();
    }
}
