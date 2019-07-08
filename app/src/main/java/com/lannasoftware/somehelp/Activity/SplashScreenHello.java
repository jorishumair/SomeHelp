package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lannasoftware.somehelp.Activity.Authentification.SignInActivity;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.Arrays;
import java.util.List;

public class SplashScreenHello extends AppCompatActivity {

    Context mContext;

    DAOUser cDaoUser;
    User currentUserSQLite;

    private static int SPLASH_TIME_OUT = 3000;
    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    long iNbUsersInSQLite;

    TextView txt_message;

    Boolean bOneUserInSQLite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_hello);

        mContext = this;

        txt_message = (TextView) findViewById(R.id.txt_message);

        HelperApp.SetFont(mContext, txt_message,"Roboto-Light.ttf");

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        iNbUsersInSQLite = cDaoUser.CountProfiles();

        if(iNbUsersInSQLite == 1)
            bOneUserInSQLite = true;

        if(bOneUserInSQLite)
            currentUserSQLite = cDaoUser.GetUserById(1);

        if(currentUserSQLite != null && !HelperApp.IsNullOrEmpty(currentUserSQLite.getsFirstname()))
        {
            txt_message.setText(getResources().getString(R.string.bonjour) + " " + currentUserSQLite.getsFirstname());
            HelperApp.RunAnimation(this, txt_message);
        }


        cDaoUser.Close();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(HelperApp.IsConnectedInternet(mContext))
        {
            if(firebaseUser == null)
                CreateSignInIntent();
            else
                ContinueWithFirebaseUser(firebaseUser);
        }
        else
            Toast.makeText(mContext, "No active network", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if(firebaseUser != null)
                    ContinueWithFirebaseUser(firebaseUser);

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void CreateSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);

    }

    private void ContinueWithFirebaseUser(FirebaseUser firebaseUser)
    {
        // Empty SQLite
        if(bOneUserInSQLite)
            LaunchHomeActivity();
        else
            LaunchSignInActivity();
    }

    private void LaunchSignInActivity()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenHello.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void LaunchHomeActivity()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenHello.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void TryRecreateSQLiteUserFromFirebase(FirebaseUser firebaseUser)
    {

    }
}
