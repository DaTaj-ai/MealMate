package com.example.testauth.ui.Home;

import android.util.Log;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Repository.Repository;
import com.example.testauth.Repository.RepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeContentPresenter {

    HomeContentFragment ui;
    RepositoryImpl repository;

    public HomeContentPresenter(HomeContentFragment ui, RepositoryImpl repository) {
        this.ui = ui;
        this.repository = repository;
    }

    public void getInspricarionMeal(){
        repository.getRemoteMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(meal->meal.getMeals().get(0))
                .doOnNext( meal->{ui.showInspricarionMeal(meal);}).subscribe();}

    private static final String TAG = "HomeContentPresenter";
    public void getMeals(){
        repository.getRemoteMeals().subscribe(new Observer<ListMealDto>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "getMeals + onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull ListMealDto listMealDto) {
                ui.showCategoryMeals(listMealDto.getMeals());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {

            }
        });

    }


    public void getCategotyList(){
        repository.getAllCategories().subscribe(new Observer<ListCategoryDto>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ListCategoryDto listCategoryDto) {
                List<String> local  = new ArrayList<>();
                for (int i = 0; i < listCategoryDto.getCategoryDtoList().size() ; i++) {
                    local.add(listCategoryDto.getCategoryDtoList().get(i).getStrCategory());
                    Log.i(TAG, "onNext: ");
                }
                ui.showCategory(local);
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                if (isNoInternet(e)) {
//                    ui.showNoInternetAnimation(); // No internet animation
//                } else {
//                    ui.showErrorAnimation(); // General error animation
//                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void getIngrdientsList(){
        repository.getAllIngredients().subscribe(new Observer<ListIngredientDto>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ListIngredientDto listIngredientDto) {
                List<String> local  = new ArrayList<>();
                for (int i = 0; i < listIngredientDto.getIngredientDtoList().size() ; i++) {
                    local.add(listIngredientDto.getIngredientDtoList().get(i).getStrIngredient());
                    Log.i(TAG, "onNext: " + listIngredientDto.getIngredientDtoList().get(i).getStrIngredient());
                }
                ui.showIngrdients(local);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void getAreasList(){
        repository.getAllAreas().subscribe(new Observer<ListAreaDto>()
        {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ListAreaDto listAreaDto) {
                List<String> local  = new ArrayList<>();
                for (int i = 0; i < listAreaDto.getAreaDtoList().size() ; i++) {
                    local.add(listAreaDto.getAreaDtoList().get(i).getStrArea());
                    Log.i(TAG, "onNext: ");
                }
                ui.showArea(local);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}

