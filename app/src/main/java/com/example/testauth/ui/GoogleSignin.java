package com.example.testauth.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testauth.MainActivity;
import com.example.testauth.R;
import com.example.testauth.ui.Favorites.FavoriteFragment;
import com.example.testauth.ui.Home.HomeFragment;
import com.example.testauth.ui.mailditails.MealDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class GoogleSignin extends Fragment {

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    ShapeableImageView imageView;
    TextView name, mail;
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
                                Glide.with(getContext()).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
                                name.setText(auth.getCurrentUser().getDisplayName());
                                mail.setText(auth.getCurrentUser().getEmail());
                                Toast.makeText(getContext(), "Signed in successfully!", Toast.LENGTH_SHORT).show();
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



    public GoogleSignin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_signin, container, false);
    }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Button gohome = view.findViewById(R.id.gohome);
            Button goFavorite = view.findViewById(R.id.gofavorite);
            Button goToMeal = view.findViewById(R.id.gotoMeal);

            gohome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main, homeFragment);
                    transaction.commit();
                }
            });
            goToMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MealDetails mealDetails = new MealDetails();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main, mealDetails);
                    transaction.commit();
                }
            });

            goFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_main, favoriteFragment);
                    transaction.commit();
                }
            });

            FirebaseApp.initializeApp(getContext());

            imageView = view.findViewById(R.id.profileImage);
            name = view.findViewById(R.id.nameTV);
            mail = view.findViewById(R.id.mailTV);

            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(getContext(), options);

            auth = FirebaseAuth.getInstance();

            SignInButton signInButton = view.findViewById(R.id.signIn);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = googleSignInClient.getSignInIntent();
                    activityResultLauncher.launch(intent);
                }
            });



            MaterialButton signOut = view.findViewById(R.id.signout);
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if (firebaseAuth.getCurrentUser() == null) {
                                googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                });
                            }
                        }
                    });
                    FirebaseAuth.getInstance().signOut();
                }
            });

            if (auth.getCurrentUser() != null) {
                Glide.with(getContext()).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
                name.setText(auth.getCurrentUser().getDisplayName());
                mail.setText(auth.getCurrentUser().getEmail());
            }

        }


    }
