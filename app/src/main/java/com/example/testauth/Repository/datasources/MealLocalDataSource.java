package com.example.testauth.Repository.datasources;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface MealLocalDataSource {
    public Completable insert (MealDto mealDto);
    public Completable delete(MealDto mealDto);

    Flowable<List<MealDto>> getAllLocalMeals();

    Completable updateMeal(MealDto mealDto);

    Flowable<MealDto> getMealById(String id);
    Flowable<List<MealDto>> getAllIsFavorites();
    Flowable<List<String>> getMealIdsForDate(String date);

    Completable insertToCalender(MealsCalenderDto mealCalenderDto);

    Completable DeleteFromCalender(MealsCalenderDto mealCalenderDto);

    Flowable<List<MealDto>> getMealsByIds(List<String> mealIds);

}
