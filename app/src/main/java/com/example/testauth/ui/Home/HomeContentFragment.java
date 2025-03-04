package com.example.testauth.ui.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testauth.Models.helper.CountryFlag;
import com.example.testauth.Models.MealDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeContentFragment extends Fragment implements IHomeConentView {

    MealDto GlobalinspirationMealDto;
    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerViewCategory, recyclerViewArea ;
    HomeViewAdapter myAdapterCategoty , myAdapterArea ;
    ChipGroup CategoryChipGroup, ingredientsChipGroup, areaChipGroup;

    ImageView InspricationCardImage , inspirationFlag;
    TextView mealNameInspiration, categotyInspiration;
    HomeContentPresenter presenter ;
    LottieAnimationView lottieAnimationView;

    private static final String TAG = "HomeContentFragment";

    Boolean IS_LOADED = false;



    public HomeContentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        presenter = new HomeContentPresenter(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));
        IS_LOADED = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CategoryChipGroup = view.findViewById(R.id.categoryCard);
        ingredientsChipGroup = view.findViewById(R.id.ingredientsChipGroup);
        areaChipGroup = view.findViewById(R.id.AreasChipGroup);
        lottieAnimationView = view.findViewById(R.id.loadingAnimaion);
        InspricationCardImage = view.findViewById(R.id.inspirationCardImage);
        categotyInspiration = view.findViewById(R.id.categoryInspiration);
        mealNameInspiration = view.findViewById(R.id.mealNameInspirationCard);
        inspirationFlag = view.findViewById(R.id.flagImageMealDetails);

        InspricationCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeContentFragmentDirections.ActionHomeContentFragmentToMealDetails action = HomeContentFragmentDirections.actionHomeContentFragmentToMealDetails(GlobalinspirationMealDto);

                Navigation.findNavController(view).navigate(action);
            }
        });


        // Category Adapter
        myAdapterCategoty = new HomeViewAdapter(getContext(), GloblaMealList);
        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategory.setAdapter(myAdapterCategoty);

        // Area Adapter
        myAdapterArea = new HomeViewAdapter(getContext(), GloblaMealList);
        recyclerViewArea = (RecyclerView) view.findViewById(R.id.recyclerViewArea);
        recyclerViewArea.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerArea = new LinearLayoutManager(getContext());
        linearLayoutManagerArea.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewArea.setLayoutManager(linearLayoutManagerArea);
        recyclerViewArea.setItemAnimator(new DefaultItemAnimator());
        recyclerViewArea.setAdapter(myAdapterArea);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        Boolean isQuest = sharedPreferences.getBoolean("isQuest", false);

        if (isNetworkConnected()) {
            Snackbar.make(getView(), "internet", Snackbar.LENGTH_LONG);

            Log.i(TAG, "onViewCreated: " + isQuest);
            if (isQuest) {
//                recyclerViewCategory.setVisibility(View.GONE);
                if (isAdded() && getActivity() != null && !isDetached()) {
                    presenter.getInspricarionMeal();
                    presenter.getCategotyList();
                    presenter.getMeals("g");
                    presenter.getAreasList();
                }
            } else {
                if (isAdded() && getActivity() != null && !isDetached()) {
                    presenter.getInspricarionMeal();
                    presenter.getMeals("g");
                    presenter.getCategotyList();
                    presenter.getAreasList();
                }
            }
        } else {
            Snackbar.make(getView(), "No internet", Snackbar.LENGTH_LONG);
            lottieAnimationView.setAnimation(R.raw.no_internet);
        }
    }



    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void lodingAnimationChangeState(Boolean state) {
        if (state) {
            lottieAnimationView.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(() -> {
                lottieAnimationView.setVisibility(View.GONE);
            }, 2000);
        }
    }

    @Override
    public void showCategory(List<String> items) {
        populateChipGroupCategory (CategoryChipGroup, items);
    }

    @Override
    public void showInspricarionMeal(MealDto mealDto) {
        GlobalinspirationMealDto = mealDto;
        Glide.with(getContext()).load(mealDto.getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(InspricationCardImage);
        mealNameInspiration.setText(mealDto.getStrMeal());
        Glide.with(getContext()).load(CountryFlag.getFlagUrl(mealDto.getStrArea())).placeholder(R.drawable.ic_launcher_foreground).into(inspirationFlag);
        categotyInspiration.setText(mealDto.getStrCategory());
    }

    @Override
    public void showInsperationMeal(List<MealDto> meals) {

    }

    @Override
    public void showIngrdients(List<String> items) {
        populateChipGroup(ingredientsChipGroup, items);

    }


    @Override
    public void showCategoryMeals(List<MealDto> meals) {
        GloblaMealList = meals;
        myAdapterCategoty.notifyItemChanged(GloblaMealList);
        myAdapterCategoty.notifyDataSetChanged();
        List<MealDto> reversedList = new ArrayList<>(meals);
        Collections.reverse(reversedList);
        myAdapterArea.notifyItemChanged(reversedList);
        myAdapterArea.notifyDataSetChanged();

    }

    @Override
    public void showArea(List<String> items) {
        populateChipGroup(areaChipGroup, items);
    }

    private void populateChipGroup(ChipGroup chipGroup, List<String> items) {
        for (String item : items) {
            Chip chip = new Chip(getContext());

            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextAppearance(R.style.ChipTextStyle);
            chip.setCheckable(true);
            chip.setCheckedIcon(null); // Remove checkmark

            chip.setText(item);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Handle filter selection
                    Log.d("Filter", "Selected: " + item);
                } else {
                    // Handle filter removal
                    Log.d("Filter", "Deselected: " + item);
                }
            });

            chipGroup.addView(chip);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void populateChipGroupCategory(ChipGroup chipGroup, List<String> items) {
        for (String item : items) {
            Chip chip = new Chip(getContext());

            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextAppearance(R.style.ChipTextStyle);
            chip.setCheckable(true);
            chip.setCheckedIcon(null);
            Glide.with(getContext())
                    .asDrawable()
                    .load(CountryFlag.getFlagUrl(item))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            chip.setChipIcon(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            chip.setChipIcon(placeholder);
                        }
                    });

            chip.setText(item);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.PrimaryColor);
                    chip.setTextColor(R.color.white);
                    Log.d("Filter", "Selected: Cantury" + item);
                    presenter.filterByCategory(item).doOnNext(mealDtoList -> {
                        myAdapterCategoty.notifyItemChanged(mealDtoList.getMeals());
                        myAdapterCategoty.notifyDataSetChanged();
                    }).subscribe();
                } else {
                    chip.setTextAppearance(R.style.ChipTextStyle);
                    chip.setChipBackgroundColorResource(R.color.chip_background_color);
                }
            });

            chipGroup.addView(chip);
        }
    }



}