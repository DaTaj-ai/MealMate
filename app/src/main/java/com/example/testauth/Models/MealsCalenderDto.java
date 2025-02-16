package com.example.testauth.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "calendar",
        primaryKeys = {"mealId", "date"},
        foreignKeys = @ForeignKey(
                entity = MealDto.class,
                parentColumns = "idMeal",
                childColumns = "mealId"
        )
)
public class MealsCalenderDto {
    @NonNull
    public String mealId;

    @NonNull
    public String date; // Format: "YYYY-MM-DD"

    public MealsCalenderDto(String mealId, @NonNull String date) {
        this.mealId = mealId;
        this.date = date;
    }
    public MealsCalenderDto() {
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }
}

