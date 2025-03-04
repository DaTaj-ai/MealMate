package com.example.testauth.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.testauth.Models.helper.CountryFlag;
import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements ISearchFragmentUI {

    ChipGroup countryGroup;
    ImageButton filterBtn;
    BottomSheetDialog bottomSheetDialog;
    SearchView searchView;
    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerView;
    MySearchAdapter mySearchAdapter;
    SearchFragmentPresenter presenter;
    ChipGroup filteredGroup;
    Observable<String> searchQueryObservable;

    private static final String TAG = "SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    void addFilterChip(Chip item) {
        filteredGroup.removeAllViews();
        this.filteredGroup.addView(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (isNetworkConnected()) {
            presenter = new SearchFragmentPresenter(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));
            // get  meals
            Disposable mealsObservable = presenter.searchMeals("").doOnNext(mealDtoList -> {
                GloblaMealList = mealDtoList.getMeals();
                mySearchAdapter.notifyItemChanged(GloblaMealList);
                mySearchAdapter.notifyDataSetChanged();
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        filterBtn = view.findViewById(R.id.filterBtn);

        filteredGroup = view.findViewById(R.id.filterCategoryCard);

        if (isNetworkConnected()) {

            filterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog = new BottomSheetDialog(getActivity());
                    View bottomSheetView = LayoutInflater.from(getActivity())
                            .inflate(R.layout.bottom_sheet_filter, null);

                    countryGroup = bottomSheetView.findViewById(R.id.countryChipGroup);
                    ChipGroup ingredientsGroup = bottomSheetView.findViewById(R.id.ingredientsChipGroup);
                    ChipGroup categoryGroup = bottomSheetView.findViewById(R.id.categoryCard);

                    //Categoty
                    Observable<ListCategoryDto> listCategoryDtoObservable = presenter.getAllCategories();
                    Observer listCategotyDtoObserver = new Observer<ListCategoryDto>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            Log.i(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListCategoryDto listCategoryDto) {
                            if (categoryGroup != null && listCategoryDto != null) {
                                populateChipGroupCategory(categoryGroup, listCategoryDto.toList());
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    };
                    listCategoryDtoObservable.subscribe(listCategotyDtoObserver);


                    // Areas
                    Observable<ListAreaDto> listAreaDtoObservable = presenter.getAllAreas();
                    Observer listAreaDtoObserver = new Observer<ListAreaDto>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            Log.i(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListAreaDto listAreaDto) {
                            Log.i(TAG, "Area onNext: " + listAreaDto.getAreaDtoList().size());
                            if (countryGroup != null && listAreaDto != null) {
                                populateChipGroupCountry(countryGroup, listAreaDto.toList());
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.i(TAG, "onError: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    };
                    listAreaDtoObservable.subscribe(listAreaDtoObserver);

                    // ingredients
                    Observable<ListIngredientDto> listIngrdienDtoObservable = presenter.getAllIngredients();
                    Observer listIngrdienDtoObserver = new Observer<ListIngredientDto>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            Log.i(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListIngredientDto listIngredientDto) {
                            if (ingredientsGroup != null && listIngredientDto != null) {
                                populateChipGroupIngredient(ingredientsGroup, listIngredientDto.toList());
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.i(TAG, "onError: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    };
                    listIngrdienDtoObservable.subscribe(listIngrdienDtoObserver);


                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                }
            });
            searchQueryObservable = Observable.create(emitter -> {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if (newText.equals(" ") || newText.equals("")) {
                            showMeals(GloblaMealList);
                        } else {
                            emitter.onNext(newText);
                            Log.i(TAG, "onQueryTextChange: " + GloblaMealList.size());
                        }
                        return false;
                    }
                });

                emitter.setCancellable(() -> searchView.setOnQueryTextListener(null));
            });
            presenter.setSearchQueryObservable(searchQueryObservable);

            // SearchAdapter setup.
            mySearchAdapter = new MySearchAdapter(getContext(), GloblaMealList);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearch);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(mySearchAdapter);

        } else {
            Snackbar.make(getView(), "No internet", Snackbar.LENGTH_LONG);
        }

    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void populateChipGroupCategory(ChipGroup chipGroup, List<String> items) {
        for (String item : items) {
            Chip chip = new Chip(getContext());
            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextAppearance(R.style.ChipTextStyle);
            chip.setCheckable(true);
            chip.setCheckedIcon(null); // Remove checkmark
            chip.setText(item);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.PrimaryColor);
                    presenter.filterByCategory(item).doOnNext(mealDtoList -> {
                        GloblaMealList.addAll(mealDtoList.getMeals());
                        mySearchAdapter.notifyItemChanged(mealDtoList.getMeals());
                        mySearchAdapter.notifyDataSetChanged();
                    }).subscribe();
                    if (chip.getParent() != null) {
                        ((ViewGroup) chip.getParent()).removeView(chip);
                    }
                    addFilterChip(chip);
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.dismiss();
                    }

                } else {
                    chip.setTextAppearance(R.style.ChipTextStyle);

                    Log.d("Filter", "Deselected: " + item);
                }
            });

            chipGroup.addView(chip);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void populateChipGroupCountry(ChipGroup chipGroup, List<String> items) {
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
                    presenter.filterByArea(item).doOnNext(mealDtoList -> {
                        GloblaMealList.addAll(mealDtoList.getMeals());
                        mySearchAdapter.notifyItemChanged(mealDtoList.getMeals());
                        mySearchAdapter.notifyDataSetChanged();
                        if (chip.getParent() != null) {
                            ((ViewGroup) chip.getParent()).removeView(chip);
                        }
                        addFilterChip(chip);
                    }).subscribe();
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.dismiss();
                    }

                } else {
                    chip.setTextAppearance(R.style.ChipTextStyle);

                    Log.d("Filter", "Deselected: " + item);
                }
            });

            chipGroup.addView(chip);
        }
    }


    @SuppressLint("ResourceAsColor")
    private void populateChipGroupIngredient(ChipGroup chipGroup, List<String> items) {
        for (String item : items) {
            Chip chip = new Chip(getContext());

            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextAppearance(R.style.ChipTextStyle);
            chip.setCheckable(true);
            chip.setCheckedIcon(null);
            chip.setTextColor(R.color.PrimaryColor);
            chip.setText(item);
            Glide.with(getContext())
                    .asDrawable()
                    .load("https://www.themealdb.com/images/ingredients/" + item + "-Small.png")
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
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.PrimaryColor);
                    chip.setTextColor(R.color.white);
                    presenter.filterByIngredient(item).doOnNext(mealDtoList -> {
                        GloblaMealList.addAll(mealDtoList.getMeals());
                        mySearchAdapter.notifyItemChanged(mealDtoList.getMeals());
                        mySearchAdapter.notifyDataSetChanged();
                        if (chip.getParent() != null) {
                            ((ViewGroup) chip.getParent()).removeView(chip);
                        }
                        addFilterChip(chip);
                    }).subscribe();
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.dismiss();
                    }

                } else {
                    chip.setTextAppearance(R.style.ChipTextStyle);

                    Log.d("Filter", "Deselected: " + item);
                }
            });
            chipGroup.addView(chip);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showMeals(List<MealDto> meals) {
        GloblaMealList.addAll(meals);
        mySearchAdapter.notifyItemChanged(meals);
        mySearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Log.i("SearchFragment", "showError: ");
    }


}

;