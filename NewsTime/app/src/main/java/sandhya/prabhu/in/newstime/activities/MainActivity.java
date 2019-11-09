package sandhya.prabhu.in.newstime.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.connection.ConnectionCheck;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    public static final String USER_NAME = "username";
    public static final String PHOTO_URL = "photo_url";
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 222;
    private String username;
    private Uri profilePicUrl;

    @BindView(R.id.googleSignIn)
    SignInButton googleSignInButton;

    @BindView(R.id.globeImage)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageView.startAnimation(rotation);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternetConnected = ConnectionCheck.checkConnection(MainActivity.this);
                if (isInternetConnected) {
                    signIntoGoogle();
                }
            }
        });


    }

    private void signIntoGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, R.string.conn_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, getString(R.string.auth_google) + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, getString(R.string.sign_in_success));
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, getString(R.string.signin_failure), task.getException());
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        username = firebaseUser.getDisplayName();
        profilePicUrl = firebaseUser.getPhotoUrl();
        Intent intent = new Intent(this, CountrySelectionActivity.class);
        intent.putExtra(USER_NAME, username);
        intent.putExtra(PHOTO_URL, profilePicUrl.toString());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            Toast.makeText(this, R.string.login_suc_msg, Toast.LENGTH_SHORT).show();
            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
            updateUI(firebaseUser);
        }
    }

    public void gotoCountrySelection(View view) {
        Intent intent = new Intent(this, CountrySelectionActivity.class);
        startActivity(intent);
    }
}