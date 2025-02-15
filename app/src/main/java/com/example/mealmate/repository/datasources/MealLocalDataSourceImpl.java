package com.example.testauth.Repository.datasources;

import android.content.Context;

import com.example.testauth.DataBase.DAO;
import com.example.testauth.DataBase.DataBase;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealLocalDataSourceImpl implements MealLocalDataSource {



    DAO dao;
    DataBase dataBase;
    Flowable<List<MealDto>> meals;
    public static MealLocalDataSource instance;

    public static  MealLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSourceImpl(context);
        }
        return instance;
    }

    public MealLocalDataSourceImpl(Context context) {
        this.dataBase = DataBase.getInstance(context);
        this.dao = dataBase.getDao();
        meals = dao.getAll();
    }


    @Override
    public Completable insert(MealDto mealDto) {
        return dao.insert(mealDto);
    }

    @Override
    public void delete(MealDto mealDto) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.delete(mealDto);
            }
        }).start();
    }

    @Override
    public Flowable<List<MealDto>> getAllLocalMeals() {
        return meals;
    }

    @Override
    public Completable updateMeal(MealDto mealDto) {
        return dao.update(mealDto);
    }

    @Override
    public Flowable<List<MealDto>> getAllIsFavorites() {
        return dao.getAllIsFavorites();
    }

    @Override
    public Flowable<List<String>> getMealIdsForDate(String date) {
        return dao.getMealIdsForDate(date);
    }
    @Override
    public Flowable<MealDto> getMealById(String id) {
        return dao.getById(id);
    }
//    @Override
//    public Flowable<List<MealDto>> getMealsForDate(String date) {
//        return dao.getMealIdsForDate(date)
//                .flatMap(mealIds ->
//                        Flowable.fromIterable(mealIds)
//                                .flatMap(dao::getById)
//                                .toList()
//                                .toFlowable()
//                )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }


    @Override
    public Flowable<List<MealDto>> getMealsByIds(List<String> mealIds) {
        return dao.getMealsByIds(mealIds);
    }

    @Override
    public Completable insertToCalender(MealsCalenderDto mealCalenderDto) {
        return dao.insertToCalender(mealCalenderDto);
    }

    @Override
    public Completable DeleteFromCalender(MealsCalenderDto mealCalenderDto) {
        return dao.DeleteFromCalender(mealCalenderDto);
    }
}
