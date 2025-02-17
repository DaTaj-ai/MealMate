package com.example.testauth.ui.search;



import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Repository.RepositoryImpl;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchFragmentPresenter {

    private ISearchFragmentUI ui;
    private RepositoryImpl repo ;

    private static final String TAG = "SearchFragmentPresenter";

    public SearchFragmentPresenter(ISearchFragmentUI ui, RepositoryImpl repo) {
        this.ui = ui;
        this.repo = repo;
    }
    public Observable<ListMealDto> searchMeals(String query) {
        return repo.getRemoteMeals(query);
    }
    public Observable<ListCategoryDto> getAllCategories(){
        return repo.getAllCategories();
    }
    public Observable<ListAreaDto> getAllAreas(){
        return repo.getAllAreas();
    }
    public Observable<ListIngredientDto> getAllIngredients(){
        return repo.getAllIngredients();
    }

    public Observable<ListMealDto> filterByArea(String areaType){
        return repo.filterByArea(areaType);
    }


//    Disposable disposable = searchQueryObservable
//            .debounce(300, TimeUnit.MILLISECONDS)
//            .distinctUntilChanged()
//            .switchMap(query ->
//                    presenter.searchMeals(query)
//                            .flatMapIterable(ListMealDto::getMeals)
//                            .filter(meal -> matchesSearchQuery(meal, query.toLowerCase()))
//                            .toList()
//                            .toObservable()
//            )
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                    filteredMeals -> {
//                        mySearchAdapter.notifyItemChanged(filteredMeals);
//                        mySearchAdapter.notifyDataSetChanged();
//                    },
//                    error -> Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
//            );


    public void setSearchQueryObservable(Observable<String> searchQueryObservable) {
    Disposable disposable = searchQueryObservable
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap(query ->
                    searchMeals(query)
                            .flatMapIterable(listMeal -> {
                                List<MealDto> meals = listMeal.getMeals();
                                return meals != null ? meals : Collections.emptyList();
                            })
                            .filter(meal -> meal != null && matchesSearchQuery(meal, query.toLowerCase()))
                            .toList()
                            .toObservable()
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    filteredMeals -> {
                        ui.showMeals(filteredMeals);
                        Log.i(TAG, "Search here  success: " + filteredMeals.size());
                    },
                    error -> {
                        ui.showSnackBar("Search failed: " + error.getMessage());
                        Log.i(TAG, "Search here  failed: " + error.getMessage());
                    }
            );
    }

    private boolean matchesSearchQuery(MealDto meal, String query) {
        return containsIgnoreCase(meal.getStrMeal(), query) ||
                containsIgnoreCase(meal.getStrCategory(), query) ||
                containsIgnoreCase(meal.getStrArea(), query) ||
                checkIngredients(meal, query);
    }


    public boolean checkIngredients(MealDto meal, String query) {
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

    public boolean containsIgnoreCase(String source, String query) {
        return source != null && source.toLowerCase().contains(query);
    }


}
