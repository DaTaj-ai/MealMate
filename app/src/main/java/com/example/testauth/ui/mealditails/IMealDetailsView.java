package com.example.testauth.ui.mealditails;

import com.example.testauth.Models.MealDto;

public interface IMealDetailsView {
    void showSnackBar(String message);
    void showMeal(MealDto mealDto);
}
