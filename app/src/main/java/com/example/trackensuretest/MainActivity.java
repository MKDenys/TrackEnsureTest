package com.example.trackensuretest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.example.trackensuretest.adapters.SectionsPagerAdapter;
import com.example.trackensuretest.database.GasStationRepository;
import com.example.trackensuretest.database.RefuelRepository;
import com.example.trackensuretest.models.GasStation;
import com.example.trackensuretest.models.Refuel;
import com.example.trackensuretest.services.SyncDBService;
import com.example.trackensuretest.utils.InternetStatusChangeReceiver;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String MENU_ITEM_SIGN_OUT = "Sign Out";
    public static final String MENU_ITEM_SIGN_IN = "Sign In";
    public static final String OK = "OK";
    private InternetStatusChangeReceiver receiver;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";
    private GoogleSignInClient client;
    private FirebaseAuth firebaseAuth;
    private RefuelRepository refuelRepository;
    private GasStationRepository gasStationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        refuelRepository = new RefuelRepository();
        gasStationRepository = new GasStationRepository();
        initFirebase();
        if (checkFirebaseAuth()){
            observeToChangedData();
        }

        receiver = new InternetStatusChangeReceiver();
        registerConnectionReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        menu.clear();
        setOptionsMenuItem(menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setOptionsMenuItem(menu);
        return true;
    }

    private void setOptionsMenuItem(Menu menu){
        if (checkFirebaseAuth()){
            menu.add(MENU_ITEM_SIGN_OUT);
        } else {
            menu.add(MENU_ITEM_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void initFirebase(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerConnectionReceiver(BroadcastReceiver receiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
    }

    private boolean checkFirebaseAuth(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getTitle() == MENU_ITEM_SIGN_OUT){
                signOut();
            } if (item.getTitle() == MENU_ITEM_SIGN_IN){
                showSignInDialog();
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(this.getIntent().hasExtra(Constants.OPEN_SIGN_IN_DIALOG_KEY)){
            this.getIntent().removeExtra(Constants.OPEN_SIGN_IN_DIALOG_KEY);
            showSignInDialog();
        }
    }

    private void showSignInDialog(){
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showResultDialog("Sing Out", "Sign out is successful");
                        removeObservers();
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            showResultDialog("Sign In", "Sign in is successful");
                            observeToChangedData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showResultDialog("Sign In", "Sign in is failed. Please, try again");
                        }
                    }
                });
    }

    private void showResultDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(Constants.POSITIVE_BUTTON_TITLE, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Observer<List<Refuel>> notSyncedRefuelsObserver = new Observer<List<Refuel>>() {
        @Override
        public void onChanged(List<Refuel> refuelList) {
            if(!refuelList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, SyncDBService.class);
                intent.putParcelableArrayListExtra(Constants.REFUELS_KEY, (ArrayList<Refuel>) refuelList);
                startService(intent);
            }
        }
    };

    private Observer<List<GasStation>> notSyncedGasStationsObserver = new Observer<List<GasStation>>() {
        @Override
        public void onChanged(List<GasStation> gasStationList) {
            if(!gasStationList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, SyncDBService.class);
                intent.putParcelableArrayListExtra(Constants.GAS_STATIONS_KEY, (ArrayList<GasStation>) gasStationList);
                startService(intent);
            }
        }
    };

    private Observer<List<Refuel>> deletedRefuelsObserver = new Observer<List<Refuel>>() {
        @Override
        public void onChanged(List<Refuel> refuelList) {
            if(!refuelList.isEmpty()){
                Intent intent = new Intent(MainActivity.this, SyncDBService.class);
                intent.putParcelableArrayListExtra(Constants.DELETED_REFUELS_KEY, (ArrayList<Refuel>) refuelList);
                startService(intent);
            }
        }
    };

    private void observeToChangedData(){
        refuelRepository.getNotSyncedTask().observe(this, notSyncedRefuelsObserver);
        gasStationRepository.getNotSyncedTask().observe(this, notSyncedGasStationsObserver);
        refuelRepository.getDeletedTask().observe(this, deletedRefuelsObserver);
    }

    private void removeObservers(){
        refuelRepository.getNotSyncedTask().removeObserver(notSyncedRefuelsObserver);
        gasStationRepository.getNotSyncedTask().removeObserver(notSyncedGasStationsObserver);
        refuelRepository.getDeletedTask().removeObserver(deletedRefuelsObserver);
    }
}