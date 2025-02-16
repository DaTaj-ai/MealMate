package com.example.testauth.Network;

import com.example.testauth.Models.CategoryDto;
import com.example.testauth.Models.MealDto;

import java.util.List;

public interface NetworkCallBack<T> {
    void onSuccess(List<T> data);
    void onFailure(String message);
}
