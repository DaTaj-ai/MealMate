package com.example.testauth.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testauth.Models.CategoryDto;
import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchFragment extends Fragment implements ISearchFragmentUI {

    ChipGroup countryGroup;

    ImageButton filterBtn;
    BottomSheetDialog bottomSheetDialog;

    SearchView searchView;

    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerView;
    MySearchAdapter mySearchAdapter;
    SearchFragmentPresenter presenter;
    private static final String TAG = "SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SearchFragmentPresenter(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));
        // get  meals
        Observable<ListMealDto> mealsObservable = presenter.searchMeals("");

        Observer<ListMealDto> listMealDtoObserver = new Observer<ListMealDto>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListMealDto listMealDto) {
                Log.i(TAG, "onNext: " + listMealDto.getMeals().get(0).getStrMeal());
                GloblaMealList = listMealDto.getMeals();
                mySearchAdapter.notifyItemChanged(GloblaMealList);
                mySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mealsObservable.subscribe(listMealDtoObserver);

        searchView = view.findViewById(R.id.searchView);

        filterBtn = view.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getActivity());
                View bottomSheetView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.bottom_sheet_filter, null);

                // Initialize ChipGroups
                countryGroup = bottomSheetView.findViewById(R.id.countryChipGroup);
                ChipGroup ingredientsGroup = bottomSheetView.findViewById(R.id.ingredientsChipGroup);
                ChipGroup categoryGroup = bottomSheetView.findViewById(R.id.categoryCard);

//                populateChipGroup(ingredientsGroup, getIngredientsList()); // Replace with ingredients
//                populateChipGroup(categoryGroup, getCategoryList());     // Replace with categories

                //Categoty
                Observable<ListCategoryDto> listCategoryDtoObservable = presenter.getAllCategories();
                Observer listCategotyDtoObserver = new Observer<ListCategoryDto>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListCategoryDto listCategoryDto) {
                        for (CategoryDto cat : listCategoryDto.getCategoryDtoList()) {
                            Log.i(TAG, "CategoryDto list onNext: " + cat.getStrCategory());
                        }
                        if (categoryGroup != null && listCategoryDto != null) {
                            populateChipGroup(categoryGroup, listCategoryDto.toList());
                        }


                        Log.i(TAG, " ListonNext : " + listCategoryDto.getCategoryDtoList().size());
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
                            populateChipGroup(ingredientsGroup, listIngredientDto.toList());
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

        searchView = view.findViewById(R.id.searchView);


        Observable<String> searchQueryObservable = Observable.create(emitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    emitter.onNext(newText);
                    return false;
                }
            });
            emitter.setCancellable(() -> searchView.setOnQueryTextListener(null));
        });


        Disposable disposable = searchQueryObservable
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(query ->
                        mealsObservable
                                .flatMapIterable(ListMealDto::getMeals)
                                .filter(meal -> matchesSearchQuery(meal, query.toLowerCase()))
                                .toList()
                                .toObservable()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        filteredMeals -> {
                            mySearchAdapter.notifyItemChanged(filteredMeals);
                            mySearchAdapter.notifyDataSetChanged();
                        },
                        error -> Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );


        mySearchAdapter = new MySearchAdapter(getContext(), GloblaMealList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSearch);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mySearchAdapter);

    }

    private boolean matchesSearchQuery(MealDto meal, String query) {
        return containsIgnoreCase(meal.getStrMeal(), query) ||
                containsIgnoreCase(meal.getStrCategory(), query) ||
                containsIgnoreCase(meal.getStrArea(), query) ||
                checkIngredients(meal, query);
    }

    private boolean checkIngredients(MealDto meal, String query) {
        // List of all 20 ingredient fields
        List<String> ingredients = Arrays.asList(
                meal.getStrIngredient1(),
                meal.getStrIngredient2(),
                meal.getStrIngredient3(),
                meal.getStrIngredient4(),
                meal.getStrIngredient5(),
                meal.getStrIngredient6(),
                meal.getStrIngredient7(),
                meal.getStrIngredient8(),
                meal.getStrIngredient9(),
                meal.getStrIngredient10(),
                meal.getStrIngredient11(),
                meal.getStrIngredient12(),
                meal.getStrIngredient13(),
                meal.getStrIngredient14(),
                meal.getStrIngredient15(),
                meal.getStrIngredient16(),
                meal.getStrIngredient17(),
                meal.getStrIngredient18(),
                meal.getStrIngredient19(),
                meal.getStrIngredient20()
        );

        // Check if any ingredient contains the query (null-safe)
        for (String ingredient : ingredients) {
            if (ingredient != null && ingredient.toLowerCase().contains(query)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsIgnoreCase(String source, String query) {
        return source != null && source.toLowerCase().contains(query);
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
    private void populateChipGroupCountry(ChipGroup chipGroup, List<String> items) {
        for (String item : items) {
            Chip chip = new Chip(getContext());

            chip.setChipBackgroundColorResource(R.color.chip_background_color);
            chip.setTextAppearance(R.style.ChipTextStyle);
            chip.setCheckable(true);
            chip.setCheckedIcon(null);
            //chip.setChipBackgroundColorResource(R.color.PrimaryColor); // Default background color
            chip.setText(item);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.PrimaryColor);
                    chip.setTextColor(R.color.white);
                    Log.d("Filter", "Selected: Cantury" + item);
                    presenter.filterByArea(item).doOnNext(mealDtoList -> {
                        mySearchAdapter.notifyItemChanged(mealDtoList.getMeals());
                        mySearchAdapter.notifyDataSetChanged();
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


    @Override
    public void showMeals(List<MealDto> meals) {
        for (MealDto meal : meals) {
            Log.i("SearchFragment", "showMeals: we are heeeeree  ");
            Log.i("SearchFragment", "showMeals: " + meal.getStrMeal());
        }
    }

    @Override
    public void showError(String message) {
        Log.i("SearchFragment", "showError: ");
    }


    public class MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.ViewHolder> {
        private final Context context;
        private List<MealDto> MealDtoList;

        public MySearchAdapter(Context _context, List<MealDto> users) {
            super();
            context = _context;
            MealDtoList = users;
        }

        @NonNull
        @Override
        public MySearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.fragment_inspiration_card, parent, false);
            MySearchAdapter.ViewHolder vh = new MySearchAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull MySearchAdapter.ViewHolder holder, int position) {
            holder.nameTxt.setText(MealDtoList.get(position).getStrMeal());
            Glide.with(context).load(MealDtoList.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
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
            TextView nameTxt;
            ImageView image;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                nameTxt = layout.findViewById(R.id.mealNameInspirationCard);
                image = layout.findViewById(R.id.inspirationCardImage);

            }
        }

    }
}


// get Categoty
//        Observable<ListCategoryDto> listCategoryDtoObservable = presenter.getAllCategories();
//        Observer listCategotyDtoObserver = new Observer<ListCategoryDto>() {
//            @Override
//            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//                Log.i(TAG, "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListCategoryDto listCategoryDto) {
//                for(CategoryDto cat : listCategoryDto.getCategoryDtoList() ){
//                    Log.i(TAG, "CategoryDto list onNext: " + cat.getStrCategory() );
//                }
//                if(countryGroup != null && listCategoryDto != null){
//                    populateChipGroup(countryGroup, listCategoryDto.toList());
//                }
//
//
//                Log.i(TAG, " ListonNext : " + listCategoryDto.getCategoryDtoList().size());
//            }
//
//            @Override
//            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//        listCategoryDtoObservable.subscribe(listCategotyDtoObserver);


//get Areas
//        Observable<ListAreaDto> listAreaDtoObservable = presenter.getAllAreas();
//        Observer listAreaDtoObserver = new Observer<ListAreaDto>() {
//            @Override
//            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//                Log.i(TAG, "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListAreaDto listAreaDto) {
//                Log.i(TAG, "Area onNext: " + listAreaDto.getAreaDtoList().size());
//            }
//
//            @Override
//            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                Log.i(TAG, "onError: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//        listAreaDtoObservable.subscribe(listAreaDtoObserver);

//        get ingrdeand
//        Observable<ListIngredientDto> listIngrdienDtoObservable = presenter.getAllIngredients();
//        Observer listIngrdienDtoObserver = new Observer<ListIngredientDto>() {
//            @Override
//            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//                Log.i(TAG, "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ListIngredientDto listAreaDto) {
//                Log.i(TAG, "Area onNext: " + listAreaDto.getIngredientDtoList().size());
//            }
//
//            @Override
//            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                Log.i(TAG, "onError: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//        listIngrdienDtoObservable.subscribe(listIngrdienDtoObserver);


//        filterBtn = view.findViewById(R.id.filterBtn);
//        filterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetDialog = new BottomSheetDialog(getActivity());
//                View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_filter, null);
//                bottomSheetDialog.setContentView(bottomSheetView);
//                bottomSheetDialog.show();
//            }
//        });

//        Disposable disposable = searchQueryObservable
//                .debounce(300, TimeUnit.MILLISECONDS) // Wait 300ms after user stops typing
//                .distinctUntilChanged() // Skip duplicate consecutive queries
//                .switchMap(query ->
//                        mealsObservable
//                                .flatMapIterable(ListMealDto::getMeals) // Convert List to Observable<MealDto>
//                                .filter(meal -> meal.getStrMeal().toLowerCase().contains(query.toLowerCase()))
//                                .toList()
//                                .toObservable()
//                )
//                .observeOn(AndroidSchedulers.mainThread()) // Ensure UI updates on the main thread
//                .subscribe(
//                        filteredMeals -> {
//                            mySearchAdapter.notifyItemChanged(filteredMeals);
//                            mySearchAdapter.notifyDataSetChanged();
//                        }
//                        ,
//                        error -> Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
//                );