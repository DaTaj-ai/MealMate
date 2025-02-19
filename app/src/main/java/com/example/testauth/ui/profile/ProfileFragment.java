package com.example.testauth.ui.profile;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testauth.HomeActivity;
import com.example.testauth.MainActivity;
import com.example.testauth.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    ShapeableImageView profileImage;
    TextView profileName;
    TextView profileEmailName, logoutBtn;
    FirebaseAuth auth;


    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profileName);
        profileEmailName = view.findViewById(R.id.profileEmailName);
        logoutBtn = view.findViewById(R.id.logoutBackupBtn);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        Boolean isQuest = sharedPreferences.getBoolean("isQuest", false);

        if (isQuest) {
            profileName.setText("Guest");
            profileEmailName.setText("Guest@gmail.com");
            logoutBtn.setVisibility(View.GONE);
        } else {
            auth = FirebaseAuth.getInstance();
            Glide.with(getContext()).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(profileImage);
            profileName.setText(auth.getCurrentUser().getDisplayName());
            profileEmailName.setText(auth.getCurrentUser().getEmail());
            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            });

          }




    }
}