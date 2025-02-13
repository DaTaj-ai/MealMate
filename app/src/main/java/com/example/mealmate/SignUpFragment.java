package com.example.testauth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testauth.Models.UserDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.inappmessaging.model.Button;


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
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();

                // Query the database to check if the email exists
                Query emailQuery = reference.orderByChild("email").equalTo(emailStr);
                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Email already exists;
                            Snackbar snackbar = Snackbar.make(v, "Email already exists!", Snackbar.LENGTH_LONG);
                            email.setError("Invalid Credentials");
                            email.requestFocus();
                            snackbar.show();
                        } else {
                            UserDto userDto = new UserDto(nameStr, emailStr, passwordStr);
                            reference.child(emailStr.replace(".", ",")).setValue(userDto)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar snackbar = Snackbar.make(v, "Sign up successfully", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_signIn2);
                                            } else {
                                                Snackbar snackbar = Snackbar.make(v, task.getException().getMessage(), Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar snackbar = Snackbar.make(v, "Check your connection: ", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });



    }
}