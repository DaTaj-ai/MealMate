package com.example.testauth.ui.calender;


import android.util.Log;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Repository.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CalenderFragmentPresenter {

    private static final String TAG = "CalenderFragmentPresent";
    ICalenderFragmentView view;
    Repository mealRepository;

    public CalenderFragmentPresenter(ICalenderFragmentView view, Repository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    public Flowable<List<String>> getMealsOfData(String date) {
        return  mealRepository.getMealIdsForDate(date);
    }

    public Flowable<List<MealDto>> getMealsByIdes(List<String> mealsIds){
        return mealRepository.getMealsByIds(mealsIds);

    }
    public DatabaseReference getMealsFromFireBase(){
        return mealRepository.getMealsFromFireBase();
    }
    public DatabaseReference getCalenderFromFireBase(){
        return mealRepository.getCalenderFromFireBase();
    }
    public void insertToCalender(MealsCalenderDto mealCalenderDto){
        mealRepository.insertToCalender(mealCalenderDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                doOnComplete(()-> Log.i(TAG, "insertToCalender: Done")).doOnError(error -> Log.i(TAG, "insertToCalender: " + error.getMessage()) ).subscribe();
//        view.showSnackBar("Meal Added to Calender Successfully")
    };
    public void insertMeal(MealDto mealDto) {
        mealRepository.insertLocal(mealDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(()-> Log.i(TAG, "Meal Added to Favorite Successfully")).subscribe();
    }




}
