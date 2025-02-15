package com.example.testauth.Repository.datasources;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Network.NetworkCallBack;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MealRemoteDataSource {
    public Observable<ListMealDto> getMeals();
    Observable<ListCategoryDto> getAllCategories();
    Observable<ListAreaDto> getAllAreas();

    Observable<ListIngredientDto> getAllIngredients();
    public Observable<ListMealDto> getRandomMeal();

    // Fire base
    public DatabaseReference getUserCalenderFireBase();
    public DatabaseReference getMealsFromFireBase();
    public void insertMealToFireBase(MealDto mealDto);
    void insertCalenderFireBase(MealsCalenderDto mealsCalenderDto);


}
