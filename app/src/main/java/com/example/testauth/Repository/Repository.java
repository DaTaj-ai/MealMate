package com.example.testauth.Repository;

import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Repository.datasources.MealLocalDataSource;
import com.example.testauth.Repository.datasources.MealRemoteDataSource;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public interface Repository {


    Completable insertToCalender(MealsCalenderDto mealCalenderDto);
    public Completable insertLocal(MealDto mealDto);
    public Completable deleteLocal(MealDto mealDto);

    public Flowable<List<MealDto>> getAllIsFavorites() ;
    Completable DeleteFromCalender(MealsCalenderDto mealCalenderDto);
    public Flowable<List<String>> getMealIdsForDate(String date);
    Flowable<List<MealDto>> getMealsByIds(List<String> mealIds);

    // Fire Base
    public DatabaseReference getMealsFromFireBase() ;
    public DatabaseReference getCalenderFromFireBase();
    public Observable<ListMealDto> getMealByIdRemote(String query);


    /*
    ----------------------------------------------------------------------
    $$$$$$$$$$$$$$           Filters  functions           $$$$$$$$$$$$$$$$$
    ----------------------------------------------------------------------
     */

    public Observable<ListMealDto> filterByArea(String areaType);
    public Observable<ListMealDto> filterByIngredients(String ingredientType);
    public Observable<ListMealDto> filterByCategory(String categoryType);
}
