package com.example.testauth.ui.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.testauth.helper.CountryFlag;
import com.example.testauth.Models.MealDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class HomeContentFragment extends Fragment implements IHomeConentView {

    MealDto GlobalinspirationMealDto;
    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerView , recyclerViewArea ;
    MyAdapter myAdapterCategoty , myAdapterArea ;
    ChipGroup CategoryChipGroup, ingredientsChipGroup, areaChipGroup;

    ImageView InspricationCardImage , inspirationFlag;
    TextView mealNameInspiration, locationInspiration;
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

        locationInspiration = view.findViewById(R.id.varLocationInspiration);
        InspricationCardImage = view.findViewById(R.id.inspirationCardImage);
        mealNameInspiration = view.findViewById(R.id.mealNameInspirationCard);
        inspirationFlag = view.findViewById(R.id.flagImageMealDetails);

        InspricationCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeContentFragmentDirections.ActionHomeContentFragmentToMealDetails action = HomeContentFragmentDirections.actionHomeContentFragmentToMealDetails(GlobalinspirationMealDto);

                Navigation.findNavController(view).navigate(action);
            }
        });



        Log.i(TAG, "onViewCreated: ");
        myAdapterCategoty = new MyAdapter(getContext(), GloblaMealList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapterCategoty);


        myAdapterArea = new MyAdapter(getContext(), GloblaMealList);
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
                recyclerView.setVisibility(View.GONE);
                if (isAdded() && getActivity() != null && !isDetached()) {
                    presenter.getInspricarionMeal();
                    presenter.getCategotyList();
                    presenter.getAreasList();
                }
            } else {
                if (isAdded() && getActivity() != null && !isDetached()) {
                    presenter.getInspricarionMeal();
                    presenter.getMeals();
                    presenter.getCategotyList();
                    presenter.getAreasList();
                }
            }
        } else {
            Snackbar.make(getView(), "No internet", Snackbar.LENGTH_LONG);
            lottieAnimationView.setAnimation(R.raw.no_internet);
        }
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final Context context;
        private List<MealDto> MealDtoList;

        public MyAdapter(Context _context, List<MealDto> users) {
            super();
            context = _context;
            MealDtoList = users;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.fragment_inspiration_card, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.nameTxt.setText(MealDtoList.get(position).getStrMeal());
            Glide.with(context).load(MealDtoList.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
            holder.location.setText(MealDtoList.get(position).getStrArea());
            holder.category.setText(MealDtoList.get(position).getStrCategory());
            Glide.with(context).load(CountryFlag.getFlagUrl(MealDtoList.get(position).getStrArea())).placeholder(R.drawable.ic_launcher_foreground).into(holder.flagImage);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeContentFragmentDirections.ActionHomeContentFragmentToMealDetails action = HomeContentFragmentDirections.actionHomeContentFragmentToMealDetails(MealDtoList.get(position));
                    Log.i(TAG, "onClick: " + MealDtoList.get(position).getStrMeal());
                    Navigation.findNavController(view).navigate(action);
                }
            });

        }

        @Override
        public int getItemCount() {
            return MealDtoList.size();
        }

        public void notifyItemChanged(List<MealDto> MealDtos) {
            this.MealDtoList = MealDtos;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout constraintLayout;
            View layout;
            TextView nameTxt, location, category;
            ImageView image;
            ImageView flagImage;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                nameTxt = layout.findViewById(R.id.mealNameInspirationCard);
                image = layout.findViewById(R.id.inspirationCardImage);
                location = layout.findViewById(R.id.varLocation);
                category = layout.findViewById(R.id.categoryCard);
                flagImage = layout.findViewById(R.id.flagImageMealDetails);


            }
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
        populateChipGroup(CategoryChipGroup, items);
    }

    @Override
    public void showInspricarionMeal(MealDto mealDto) {
        GlobalinspirationMealDto = mealDto;
        Glide.with(getContext()).load(mealDto.getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(InspricationCardImage);
        mealNameInspiration.setText(mealDto.getStrMeal());
        Glide.with(getContext()).load(CountryFlag.getFlagUrl(mealDto.getStrArea())).placeholder(R.drawable.ic_launcher_foreground).into(inspirationFlag);
        locationInspiration.setText(mealDto.getStrMeal());
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

        myAdapterArea.notifyItemChanged(GloblaMealList);
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

}