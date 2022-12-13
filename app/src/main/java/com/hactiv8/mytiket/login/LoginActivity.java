package com.hactiv8.mytiket.login;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;
import static com.hactiv8.mytiket.databinding.ActivityLoginBinding.inflate;
import static com.hactiv8.mytiket.util.LocalPreference.getInstance;
import static java.lang.String.valueOf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hactiv8.mytiket.MainActivity;
import com.hactiv8.mytiket.R;
import com.hactiv8.mytiket.databinding.ActivityLoginBinding;
import com.hactiv8.mytiket.pojo.Users;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private GoogleSignInClient signInClient;
    private FirebaseAuth auth;
//    private FirebaseFirestore db;
    private ProgressDialog dialog;
//    private boolean isExist = false;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        binding = inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
////        dialog = new ProgressDialog(this);
////        dialog.setMessage("Setup..");
////        dialog.setCancelable(false);
//
//        createRequest();
//
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        binding.btnSignUp.setOnClickListener(v -> signIn());
//    }
//    private void createRequest() {
//        // R.string.default_web_client_id : get client id apter build
//        GoogleSignInOptions gso = new Builder(DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        signInClient = GoogleSignIn.getClient(this, gso);
//    }
//
//    private void signIn() {
//        Intent signInIntent = signInClient.getSignInIntent();
//        startActivityForResult(signInIntent, 100);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser user = auth.getCurrentUser();
//        if (user !=null){
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//        }
//    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == 100) {
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            handleSignInResult(task);
////        }
////    }
////
////    private void handleSignInResult(Task<GoogleSignInAccount> task) {
////        try {
////            dialog.show();
////            GoogleSignInAccount account = task.getResult(ApiException.class);
////            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
////            firebaseAuthWithGoogle(account.getIdToken());
////
////        } catch (ApiException e) {
////            dialog.dismiss();
////            signInClient.signOut();;
////            Log.d(TAG, "Google sign in failed", e);
////            Snackbar.make(binding.getRoot(), "signInResult:failed code=" + e.getStatusCode(),
////                    Snackbar.LENGTH_SHORT).show();
////        }
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) {
//            progressBar(true);
//            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//            if (signInAccountTask.isSuccessful()) {
//                try {
//                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
//                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
//                    auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                progressBar(false);
//                                boolean userExist = checkUser(task.getResult().getUser().getEmail());
//                                if (userExist) {
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                } else {
//                                    updateUi();
//                                }
//
//                            } else {
//                                progressBar(false);
//                                Toast.makeText(LoginActivity.this, "Authentication Failed : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
////    private void firebaseAuthWithGoogle(String idToken) {
////        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
////        auth.signInWithCredential(credential)
////                .addOnCompleteListener(this, task -> {
////                    if (task.isSuccessful()) {
////                        FirebaseUser user = auth.getCurrentUser();
////                        updateUI(user);
////                        signInClient.signOut();
////                    } else {
////                        dialog.dismiss();
////                        signInClient.signOut();
////                        Snackbar.make(binding.getRoot(), "Authentication Failed.",
////                                Snackbar.LENGTH_SHORT).show();
////                    }
////                });
////    }
//
//    private void updateUi() {
//        startActivity(new Intent(this, SetupActivity.class));
//        }
//
//    private void progressBar(boolean state) {
//        if (state) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//        } else {
//            binding.progressBar.setVisibility(View.GONE);
//        }
//    }
//
//    private boolean checkUser(String email) {
//
//        db.collection("user")
//                .whereEqualTo("email", email)
//                .get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        isExist = task.getResult().size() >= 1;
//                    }
//                });
//
//        return isExist;
//    }
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    binding = inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    dialog = new ProgressDialog(this);
    dialog.setMessage("Setup..");
    dialog.setCancelable(false);

    // R.string.default_web_client_id : get client id apter build
    GoogleSignInOptions gso = new Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    auth = FirebaseAuth.getInstance();
    signInClient = GoogleSignIn.getClient(this, gso);
    binding.btnSignUp.setOnClickListener(v -> signIn());
}

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
////        FirebaseUser user = auth.getCurrentUser();
////        if (user !=null){
////            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
////            startActivity(intent);
////        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            dialog.show();
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());

        } catch (ApiException e) {
            dialog.dismiss();
            signInClient.signOut();;
            Log.d(TAG, "Google sign in failed", e);
            Snackbar.make(binding.getRoot(), "signInResult:failed code=" + e.getStatusCode(),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                        signInClient.signOut();
                    } else {
                        dialog.dismiss();
                        signInClient.signOut();
                        Snackbar.make(binding.getRoot(), "Authentication Failed.",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document("users/"+currentUser.getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().getData() != null){
                    Users user = task.getResult().toObject(Users.class);
                    if(user!=null){
                        getInstance(this).getEditor()
                                .putString("uid", user.getUid())
                                .putString("name", user.getName())
                                .putString("email", user.getEmail())
                                .putString("photo", user.getPhotoUrl())
                                .putString("phone", user.getPhoneNumber()).apply();
                    }

                    dialog.dismiss();
                    startActivity(new Intent(this, MainActivity.class)
                            .putExtra("user", user));
                    finish();
                }else {
                    Users user = new Users(currentUser.getUid(), currentUser.getDisplayName(),
                            "", currentUser.getEmail(), valueOf(currentUser.getPhotoUrl()));
                    dialog.dismiss();
                    startActivity(new Intent(this, SetupActivity.class)
                            .putExtra("user", user));
                }
            }
        });
    }
}