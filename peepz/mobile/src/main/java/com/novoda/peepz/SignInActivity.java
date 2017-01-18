package com.novoda.peepz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements AuthenticationCallbacks {

    private static final int REQUEST_CODE_SIGN_IN = 1;

    private FirebaseWrapper firebaseWrapper;
    private GoogleApiClient googleApiClient;
    private TextView statusTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseWrapper = new FirebaseWrapper(FirebaseAuth.getInstance());
        googleApiClient = new GoogleApiClientFactory(BuildConfig.GOOGLE_WEB_CLIENT_ID, this).createGoogleApiClient(this);

        statusTextView = ButterKnife.findById(this, R.id.status_text);
        Button signInButton = ButterKnife.findById(this, R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
            }
        });

        Button signOutButton = ButterKnife.findById(this, R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseWrapper.signOut();
                update(statusTextView);
            }
        });

        update(statusTextView);
    }

    private void update(TextView statusTextView) {
        FirebaseUser signedInUser = firebaseWrapper.getSignedInUser();
        statusTextView.setText(signedInUser == null ? "signed out" : "signed in: " + signedInUser.getEmail());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                firebaseWrapper.getFirebaseUser(googleSignInAccount, this);

            } else {
                onAuthenticationFailure();
            }
        }
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        toast("signed in as " + firebaseUser.getDisplayName() + " (" + firebaseUser.getEmail() + ")");
        update(statusTextView);
    }

    @Override
    public void onAuthenticationFailure() {
        toast("error authenticating");
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.d("!!!toast", text);
    }
}
