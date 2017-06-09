package bluenergyfuel.bluenergy.activities;

import android.content.Intent;

import com.android.volley.VolleyError;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.drawer.fragments.News;
import bluenergyfuel.bluenergy.drawer.fragments.NewsPost;
import bluenergyfuel.bluenergy.drawer.fragments.PartnerStore;
import bluenergyfuel.bluenergy.drawer.fragments.PartnerStorePost;
import bluenergyfuel.bluenergy.drawer.fragments.Rewards;
import bluenergyfuel.bluenergy.drawer.fragments.SettingTab;
import bluenergyfuel.bluenergy.drawer.fragments.StationLocator;
import bluenergyfuel.bluenergy.drawer.fragments.Transactions;
import bluenergyfuel.bluenergy.firebase.utils.MyDatabaseUtil;
import bluenergyfuel.bluenergy.model.CardUser;
import bluenergyfuel.bluenergy.volleyservices.VolleyIResult;
import bluenergyfuel.bluenergy.volleyservices.VolleyService;

public class BluEnergyDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        News.OnNewsListener ,
        PartnerStore.OnPartnerListener{
    private static final String TAG ="BluEnergyDrawer";
    private FragmentTransaction transaction;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;
    VolleyIResult mResultCallback = null;
    VolleyService mVolleyService;
    private static String jsonOb;
    private static boolean cardType;
    private static final String REWARDS = "Rewards Card";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_activity_blu_energy_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRef = MyDatabaseUtil.getDatabase().getReference().child("card_users");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userUpdate(firebaseUser);
    }
    private void userUpdate(FirebaseUser currentUser) {
        if (currentUser == null){
            loadBluEnergySignIn();
        }else {
            String uid = currentUser.getUid();
            Log.d("JEJE", uid);
            mRef = mRef.child(uid);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CardUser cardUser = dataSnapshot.getValue(CardUser.class);
                    if (cardUser != null){
                        String temp = cardUser.getCardtype();
                        cardType = temp.matches(REWARDS);
                        jsonOb = cardUser.getTransactions();
                        initVolleyCallback();
                        mVolleyService = new VolleyService(mResultCallback,BluEnergyDrawer.this);
                        mVolleyService.getDataVolley("GETCALL","http://heandsons.dyndns.org:237/PointsService/web/card/"+cardUser.getCardnumber());

                    }else {
                        Log.d(TAG,"NULL");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new Rewards()).commit();
        }
    }
    private void initVolleyCallback() {

        mResultCallback = new VolleyIResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                mRef.child("transactions").setValue(response.toString());
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }
    public void loadBluEnergySignIn(){
        Intent signinIntent = new Intent(BluEnergyDrawer.this, BluEnergy.class);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signinIntent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blu_energy_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut(){
        firebaseAuth.signOut();
        loadBluEnergySignIn();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        int id = item.getItemId();
        if (id == R.id.nav_new) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new News()).commit();
        } else if (id == R.id.nav_rewards) {
            if (cardType){
                fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new Rewards()).commit();
            } else{
                Toast.makeText(BluEnergyDrawer.this, "Not Available for Fleet Card", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_transaction) {
            fragmentManager.popBackStack();
            Transactions transactions = new Transactions();
            Bundle args = new Bundle();
            args.putString(Transactions.ARGS_JSON_OBJ, jsonOb);
            transactions.setArguments(args);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.drawer_main_fragment, transactions).commit();
        } else if (id == R.id.nav_partner_store) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new PartnerStore()).commit();
        } else if (id == R.id.nav_station_locator) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new StationLocator()).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.drawer_main_fragment, new SettingTab()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewsSelected(String key) {
        NewsPost newsPost = new NewsPost();
        Bundle args = new Bundle();
        args.putString(NewsPost.ARGS_KEY, key);
        newsPost.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_main_fragment, newsPost).addToBackStack(null).commit();
    }

    @Override
    public void onPartnerSelected(String key) {
        PartnerStorePost  partnerStore = new PartnerStorePost();
        Bundle args = new Bundle();
        args.putString(PartnerStorePost.ARGS_KEY, key);
        partnerStore.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_main_fragment, partnerStore).addToBackStack(null).commit();
    }

}
