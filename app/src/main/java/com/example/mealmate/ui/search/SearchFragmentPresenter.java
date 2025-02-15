package com.example.testauth.ui.search;



import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Repository.RepositoryImpl;


import io.reactivex.rxjava3.core.Observable;

public class SearchFragmentPresenter {

    private ISearchFragmentUI ui;
    private RepositoryImpl repo ;

    private static final String TAG = "SearchFragmentPresenter";

    public SearchFragmentPresenter(ISearchFragmentUI ui, RepositoryImpl repo) {
        this.ui = ui;
        this.repo = repo;
    }
    public Observable<ListMealDto> searchMeals(String query) {
        return repo.getRemoteMeals();
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

//    @Override
//    public void onSuccess(List mealDtoList) {
//        ui.showMeals(mealDtoList);
//    }
//
//    @Override
//    public void onFailure(String message) {
//        ui.showError(message);
//    }

}
