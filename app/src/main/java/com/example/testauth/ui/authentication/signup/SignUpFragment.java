package com.example.testauth.ui.authentication.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.testauth.HomeActivity;
import com.example.testauth.Models.UserDto;
import com.example.testauth.R;
import com.example.testauth.ui.authentication.signin.SignIn;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends Fragment {

    public static final String TAG = "SignUpFragment";

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText name ,email ,password ,confirmPassword ;
    MaterialButton signUpBtn ;
    ImageButton signInWithGoogle ;

    TextView sginIN ;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database =  FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        name  = view.findViewById(R.id.editTextTextName);
        email = view.findViewById(R.id.editTextTextEmailAddressSginin);
        password  = view.findViewById(R.id.editTextPasswordSignin);
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        signUpBtn = view.findViewById(R.id.signUpBtnlocalSignUpV);

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                String nameStr = name.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();

                auth.createUserWithEmailAndPassword(emailStr, passwordStr)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Get the unique UID from Firebase Authentication
                                String userId = auth.getCurrentUser().getUid();

                                UserDto userDto = new UserDto(nameStr, emailStr, passwordStr);

                                reference.child(userId).setValue(userDto)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {

                                                Snackbar snackbar = Snackbar.make(v, "Sign up successfully", Snackbar.LENGTH_LONG);
                                                snackbar.show();

                                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("email",  emailStr);
                                                editor.putString("password", passwordStr);
                                                editor.putString("username", nameStr);
                                                editor.putBoolean("isQuest", false);
                                                editor.apply();
                                                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                                                startActivity(intent);
                                                requireActivity().finish();

                                            } else {
                                                Snackbar snackbar = Snackbar.make(v, task1.getException().getMessage(), Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        });
                            } else {
                                Snackbar snackbar = Snackbar.make(v, task.getException().getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        });

            }

        });



    }
}