package com.example.testauth.ui.mailditails;

import android.util.Log;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Repository.Repository;
import com.example.testauth.Repository.RepositoryImpl;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresentor {

    private static final String TAG = "MealDetailsPresentor";
    private IMealDetailsView view;
    private RepositoryImpl repository;

    public MealDetailsPresentor(IMealDetailsView view, RepositoryImpl repository) {
        this.view = view;
        this.repository = repository;
    }

    public void insertMeal(MealDto mealDto) {
        repository.insertLocal(mealDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(()->view.showSnackBar("Meal Added to Favorite Successfully")).subscribe();
    }

    public void deleteMeal(MealDto mealDto) {
        repository.deleteLocal(mealDto);
    }
    public void insertToCalender(MealsCalenderDto mealCalenderDto){
           repository.insertToCalender(mealCalenderDto)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread()).
                   doOnComplete(()->view.showSnackBar("Meal Added to Calender Successfully")).doOnError(error -> Log.i(TAG, "insertToCalender: " + error.getMessage()) ).subscribe();
    };

    // change this to RX
    public void insertMealToFireBase(MealDto mealDto){
        repository.insertMealToFireBase(mealDto);
    }
    public void insertClendertoFireBase(MealsCalenderDto mealsCalenderDto){
        repository.insertCalenderFireBase(mealsCalenderDto);
    }


    public DatabaseReference getMealsFromFireBase(){
        return repository.getMealsFromFireBase();
    }

}
