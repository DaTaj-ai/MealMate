package com.example.testauth.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;


@Database(entities = {MealDto.class, MealsCalenderDto.class}, version = 5, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    public static DataBase instance = null;

    public abstract DAO getDao();

    public static DataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "MealMateDBv1").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
