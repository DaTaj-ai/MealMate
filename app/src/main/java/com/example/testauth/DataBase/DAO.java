package com.example.testauth.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(MealDto mealDto);

    @Delete
    Completable delete(MealDto mealDto);
    @Upsert
    Completable update(MealDto mealDto);


    @Query("Select * from meals")
    Flowable<List<MealDto>> getAll();

    @Query("Select * from meals where idMeal = :id")
    Flowable<MealDto> getById(String id);

    @Query("Select * from meals where isFavorite = 1")
    Flowable<List<MealDto>> getAllIsFavorites();

    @Query("SELECT * FROM meals WHERE idMeal IN (:mealIds)")
    Flowable<List<MealDto>> getMealsByIds(List<String> mealIds);


    @Query("SELECT mealId FROM calendar WHERE date = :date")
    Flowable<List<String>> getMealIdsForDate(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertToCalender(MealsCalenderDto mealCalenderDto);

    @Delete
    Completable DeleteFromCalender(MealsCalenderDto mealCalenderDto);

}
