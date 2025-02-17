package com.example.testauth.ui.search;

import com.example.testauth.Models.MealDto;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public interface ISearchFragmentUI {

    void showMeals(List<MealDto> meals);
    void showError(String message);
    public void showSnackBar(String message);

}
