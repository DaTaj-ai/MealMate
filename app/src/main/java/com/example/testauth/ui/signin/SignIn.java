package com.example.testauth.ui.signin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testauth.HomeActivity;
import com.example.testauth.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignIn extends Fragment {

    private static final String TAG = "SignIn";
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    ShapeableImageView imageView;

    EditText loginEmail, loginPassword;
    TextView name, mail;

    TextView goToSignUp, goToGuest;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
                                Toast.makeText(getContext(), "Signed in successfully!", Toast.LENGTH_SHORT).show();
                                auth.getCurrentUser().getEmail();

                                String userUsername = auth.getCurrentUser().getEmail();
                                String userPassword = auth.getCurrentUser().getDisplayName();
                                String nameFromDB = auth.getCurrentUser().getDisplayName();

                                //save data to shared Preferences
                                saveUserDataSharedPreferences(getView(), userUsername, userPassword, nameFromDB);

                                // Transaction
                                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                                startActivity(intent);
                                requireActivity().finish();

                            } else {
                                Toast.makeText(getContext(), "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public SignIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginEmail = view.findViewById(R.id.editTextTextEmailAddressSginin);
        loginPassword = view.findViewById(R.id.editTextPasswordSignin);

        goToGuest = view.findViewById(R.id.guestClickable);
        goToGuest.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isQuest", true);
                editor.apply();

                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
                Snackbar.make(view, "welcome to Guest Mode \n Login to get full access", Snackbar.LENGTH_LONG).show();
            }
        });

        goToSignUp = view.findViewById(R.id.SignupConsttext);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_signIn2_to_signUpFragment);
//                SignUpFragment signUpFragment = new SignUpFragment();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_content_main, signUpFragment);
//                transaction.commit();
            }
        });

        Button signInBtn = view.findViewById(R.id.signUpBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser(v);

            }
        });

        FirebaseApp.initializeApp(getContext());


        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), options);

        auth = FirebaseAuth.getInstance();

        SignInButton signInWithGoodle = view.findViewById(R.id.signInWithGoogle);

        signInWithGoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });


    }


    public void checkUser(View view) {
        String userUsername = loginEmail.getText().toString();
        String userPassword = loginPassword.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(userUsername, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                String userEmail = user.getEmail();

                                // Save user data
                                saveUserDataSharedPreferences(view, userEmail, userPassword, user.getDisplayName());

                                // Show success message
                                Snackbar.make(view, "Sign in successful!", Snackbar.LENGTH_LONG).show();

                                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                                startActivity(intent);
                                requireActivity().finish();

                            }
                        } else {
                            Snackbar.make(view, "Authentication failed: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

    }

    void saveUserDataSharedPreferences(View view, String email, String password, String name ) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("username", name);
        editor.putBoolean("isQuest", false);
        editor.apply();
    }




}



